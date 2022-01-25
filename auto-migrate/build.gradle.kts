extra.apply {
    set("artifactName", "automigrate")
    set("moduleName", "mikrate.automigrate")
}

dependencies {
    api(project(":executors:executor-api"))
    api(project(":dialects:dialect-api"))
    api(project(":core"))

    testImplementation(project(":dialects:sqlite"))
    testImplementation(project(":dialects:h2"))
    testImplementation(project(":executors:jdbc"))
    testImplementation(project(":executors:r2dbc"))
    testImplementation(project(":dsl"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.6.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.1.0")
    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testRuntimeOnly("org.xerial:sqlite-jdbc:3.36.0.2")
    testRuntimeOnly("io.r2dbc:r2dbc-h2:0.9.0.RELEASE")
    testRuntimeOnly("com.h2database:h2:2.1.210")
}
