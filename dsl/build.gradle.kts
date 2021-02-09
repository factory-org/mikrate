version = "0.1.0-SNAPSHOT"

extra.apply {
    set("artifactName", "dsl")
    set("moduleName", "mikrate.dsl")
}

dependencies {
    api(project(":core"))
    compileOnly("org.jetbrains:annotations:20.1.0")

    // Tests
    testImplementation(project(":dialects:generic"))
    testImplementation(project(":dialects:postgres"))
    testImplementation(project(":dialects:sqlite"))
    testImplementation("io.kotest:kotest-runner-junit5:4.3.2")
    testImplementation("io.kotest:kotest-assertions-core:4.3.2")
}
