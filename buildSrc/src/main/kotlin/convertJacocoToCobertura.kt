import org.gradle.api.Project
import org.w3c.dom.Element
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Path
import java.time.Instant
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


fun Project.jacocoToCobertura(file: File, sourceRoots: Set<File>, outputFile: File, sourceToFilename: Boolean = false) {
    val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder().apply {
        setEntityResolver { _, _: String ->
            InputSource(ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".toByteArray()))
        }
    }
    val jacocoDoc = builder.parse(file)
    val coberturaDoc = builder.newDocument()

    val cobertura = coberturaDoc.createElement("coverage")
    convertRoot(jacocoDoc.documentElement, cobertura, sourceRoots, this)

    if (sourceToFilename) {
        val sources = cobertura.find("sources")
        val packages = cobertura.find("packages")
        for (pkg in packages.childNodes.elementSequence()) {
            val classes = pkg.find("classes")
            classes@ for (klass in classes.childNodes.elementSequence()) {
                for (source in sources.childNodes.elementSequence()) {
                    val fullPath = Path.of(source.textContent, klass.getAttribute("filename"))
                    if (rootProject.rootDir.resolve(fullPath.toFile()).exists()) {
                        klass.setAttribute("filename", fullPath.toString())
                        continue@classes
                    }
                }

                logger.warn("Warning: File ${klass.getAttribute("filename")} cannot bo found in any source.")
            }
        }
    }

    coberturaDoc.appendChild(cobertura)
    val transformer = TransformerFactory.newInstance().newTransformer()
    transformer.transform(DOMSource(coberturaDoc), StreamResult(outputFile))
}

private fun convertRoot(source: Element, target: Element, sourceRoots: Set<File>, project: Project) {
    val timestamp =
        source.findOrNull("sessioninfo")?.getAttribute("start")?.toLong()?.div(1000)
            ?: Instant.now().epochSecond
    target.setAttribute("timestamp", timestamp.toString())
    val sources = target.createElement("sources")
    for (s in sourceRoots) {
        sources.createElement("source").textContent = s.relativeTo(project.rootProject.rootDir).path
    }

    val packages = target.createElement("packages")
    for (group in source.findAll("group")) {
        for (oldPkg in group.findAll("package")) {
            val newPkg = packages.createElement("package")
            convertPackage(oldPkg, newPkg)
        }
    }
    for (oldPkg in source.findAll("package")) {
        val newPkg = packages.createElement("package")
        convertPackage(oldPkg, newPkg)
    }

    addCounters(source, target)
}

private fun convertPackage(oldPkg: Element, newPkg: Element) {
    newPkg.setAttribute("name", oldPkg.attributes["name"]?.nodeValue?.replace('/', '.'))
    val classes = newPkg.createElement("classes")
    for (oldClass in oldPkg.findAll("class")) {
        val newClass = classes.createElement("class")
        convertClass(oldClass, newClass, oldPkg)
    }

    addCounters(oldPkg, newPkg)
}

private fun convertClass(oldClass: Element, newClass: Element, oldPkg: Element) {
    newClass.setAttribute("name", oldClass.getAttribute("name").replace('/', '.'))
    newClass.setAttribute(
        "filename",
        oldClass.getAttribute("name").replaceAfterLast('/', oldClass.getAttribute("sourcefilename"))
    )

    val allOldLines = findLines(oldPkg, newClass.getAttribute("filename"))

    val methods = newClass.createElement("methods")
    val allOldMethods = oldClass.findAll("method").toList()
    for (oldMethod in allOldMethods) {
        val oldMethodLines = methodLines(oldMethod, allOldMethods, allOldLines).toList()
        val newMethod = methods.createElement("method")
        convertMethod(newMethod, oldMethod, oldMethodLines)
    }

    addCounters(oldClass, newClass)
    val newLines = newClass.createElement("lines")
    convertLines(allOldLines, newLines)
}

private fun convertMethod(newMethod: Element, oldMethod: Element, oldLines: List<Element>) {
    newMethod.setAttribute("name", oldMethod.getAttribute("name"))
    newMethod.setAttribute("signature", oldMethod.getAttribute("desc"))

    addCounters(oldMethod, newMethod)
    val newLines = newMethod.createElement("lines")
    convertLines(oldLines, newLines)
}

fun convertLines(oldLines: List<Element>, newLines: Element) {
    for (oldLine in oldLines) {
        val mb = oldLine.getAttribute("mb").toDouble()
        val cb = oldLine.getAttribute("cb").toDouble()
        val ci = oldLine.getAttribute("ci").toInt()

        val newLine = newLines.createElement("line")
        newLine.setAttribute("number", oldLine.getAttribute("nr"))
        newLine.setAttribute("hits", ci.toString())

        if (mb + cb > 0) {
            val percentage = "${(100 * (cb / (cb + mb))).toInt()}%"
            newLine.setAttribute("branch", "true")
            newLine.setAttribute("condition-coverage", "$percentage (${cb.toInt()}/${(cb + mb).toInt()})")

            val cond = newLine.createElement("conditions").createElement("condition")
            cond.setAttribute("number", "0")
            cond.setAttribute("type", "jump")
            cond.setAttribute("coverage", percentage)
        } else {
            newLine.setAttribute("branch", "false")
        }
    }
}

private fun addCounters(source: Element, target: Element) {
    target.setAttribute("line-rate", counter(source, "LINE"))
    target.setAttribute("branch-rate", counter(source, "BRANCH"))
    target.setAttribute("complexity", counter(source, "COMPLEXITY", ::sum))
}

private fun counter(source: Element, type: String, operation: (Double, Double) -> Double = ::fraction): String {
    val counters = source.findAll("counter")
    val counter = counters.filter { it.getAttribute("type") == type }.firstOrNull() ?: return "0.0"

    val covered = counter.getAttribute("covered").toDouble()
    val missed = counter.getAttribute("missed").toDouble()

    return operation(covered, missed).toString()
}

private fun fraction(covered: Double, missed: Double) = covered / (covered + missed)

private fun sum(covered: Double, missed: Double) = covered + missed

private fun methodLines(oldMethod: Element, oldMethods: List<Element>, oldLines: List<Element>) = sequence {
    val startLine = oldMethod.getAttribute("line")?.toInt() ?: 0
    val larger = oldMethods.filter { lineIsAfter(it, startLine) }.map { it.getAttribute("line")?.toInt() ?: 0 }
    val endLine = larger.min() ?: Int.MAX_VALUE

    for (oldLine in oldLines) {
        if (oldLine.getAttribute("nr").toInt() in startLine until endLine) {
            yield(oldLine)
        }
    }
}

private fun lineIsAfter(oldMethod: Element, startLine: Int) = oldMethod.getAttribute("line")?.toInt() ?: 0 > startLine

private fun findLines(oldPkg: Element, filename: String): List<Element> {
    val lines = mutableListOf<Element>()

    for (sourceFile in oldPkg.findAll("sourcefile")) {
        if (sourceFile.getAttribute("name") == filename.substringAfterLast('/')) {
            lines.addAll(sourceFile.findAll("line"))
        }
    }

    return lines
}

private fun Element.findAll(tagName: String) = childNodes.elementSequence().filter { it.tagName == tagName }
private fun Element.findOrNull(tagName: String): Element? = findAll(tagName).firstOrNull()
private fun Element.find(tagName: String): Element = findAll(tagName).first()
private operator fun NamedNodeMap.get(name: String): Node? = getNamedItem(name)
private fun Element.createElement(tagName: String): Element {
    val new = ownerDocument.createElement(tagName)
    appendChild(new)
    return new
}

private fun NodeList.elementSequence() = sequence {
    for (i in 0 until length) {
        val node = item(i)
        if (node is Element) {
            yield(node)
        }
    }
}
