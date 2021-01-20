base.archivesBaseName = "mikrate-core"
group = "factory.mikrate"
version = "0.1.0-SNAPSHOT"
val moduleName = "mikrate.core"

plugins {
    kotlin("jvm")
}

repositories {
    jcenter()
}

tasks.compileJava {
    inputs.property("moduleName", moduleName)
    options.compilerArgs = listOf(
        "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
    )
}
