plugins {
    jacoco
}

extra.apply {
    set("artifactName", "dsl")
    set("moduleName", "mikrate.dsl")
}

dependencies {
    api(project(":core"))
    implementation("org.jetbrains:annotations:20.1.0")

    // Tests
    testImplementation(project(":dialects:generic"))
    testImplementation(project(":dialects:postgres"))
    testImplementation(project(":dialects:sqlite"))
    testImplementation("io.kotest:kotest-runner-junit5:4.3.2")
    testImplementation("io.kotest:kotest-assertions-core:4.3.2")
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
        html.isEnabled = true
    }

    doLast {
        val dest = reports.xml.destination
        jacocoToCobertura(
            dest,
            kotlin.sourceSets.main.get().kotlin.srcDirs,
            dest.parentFile.resolve("${dest.nameWithoutExtension}.cobertura.xml"),
            sourceToFilename = true
        )
    }
}
