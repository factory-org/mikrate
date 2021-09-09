extra.apply {
    set("artifactName", "automigrate")
    set("moduleName", "mikrate.automigrate")
}

dependencies {
    api(project(":executors:executor-api"))
    api(project(":dialects:dialect-api"))
    api(project(":core"))

    testImplementation(project(":dialects:sqlite"))
    testImplementation(project(":executors:jdbc"))
    testImplementation(project(":dsl"))
    testImplementation("org.xerial:sqlite-jdbc:3.36.0.2")
    testImplementation("io.kotest:kotest-runner-junit5:4.6.1")
    testImplementation("io.kotest:kotest-assertions-core:4.6.1")
}
