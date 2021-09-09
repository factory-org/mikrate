plugins {
    jacoco
}

extra.apply {
    set("artifactName", "dsl")
    set("moduleName", "mikrate.dsl")
}

dependencies {
    api(project(":core"))
    implementation("org.jetbrains:annotations:22.0.0")

    // Tests
    testImplementation(project(":dialects:generic"))
    testImplementation(project(":dialects:postgres"))
    testImplementation(project(":dialects:sqlite"))
    testImplementation("io.kotest:kotest-runner-junit5:4.6.2")
    testImplementation("io.kotest:kotest-assertions-core:4.6.2")
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }

    doLast {
        val dest = reports.xml.outputLocation.get().asFile
        jacocoToCobertura(
            dest,
            kotlin.sourceSets.main.get().kotlin.srcDirs,
            dest.parentFile.resolve("${dest.nameWithoutExtension}.cobertura.xml"),
            sourceToFilename = true
        )
    }
}
