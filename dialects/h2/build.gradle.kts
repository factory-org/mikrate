extra.apply {
    set("artifactName", "dialects-h2")
    set("moduleName", "mikrate.dialects.h2")
}

dependencies {
    api(project(":dialects:dialect-api"))

    testImplementation(project(":dsl"))
    testImplementation(project(":executors:jdbc"))
    testImplementation("io.kotest:kotest-runner-junit5:4.4.3")
    testImplementation("io.kotest:kotest-assertions-core:4.4.3")
    testImplementation("ch.qos.logback:logback-classic:1.2.5")
    runtimeOnly("com.h2database:h2:2.1.210")
}
