extra.apply {
    set("artifactName", "dialects-sqlite")
    set("moduleName", "mikrate.dialects.sqlite")
}

dependencies {
    api(project(":dialects:dialect-api"))
    testImplementation(project(":dsl"))
    testImplementation("org.jetbrains:annotations:22.0.0")
    testImplementation("org.xerial:sqlite-jdbc:3.36.0.2")
    testImplementation("io.kotest:kotest-runner-junit5:4.6.1")
    testImplementation("io.kotest:kotest-assertions-core:4.6.1")
}

tasks {
    compileTestKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xopt-in=kotlin.contracts.ExperimentalContracts")
        }
    }
}
