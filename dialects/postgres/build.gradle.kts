extra.apply {
    set("artifactName", "dialects-postgres")
    set("moduleName", "mikrate.dialects.postgres")
}

dependencies {
    api(project(":dialects:dialect-api"))

    testImplementation(project(":dsl"))
    testImplementation(project(":executors:jdbc"))
    testImplementation("io.kotest:kotest-runner-junit5:4.4.3")
    testImplementation("io.kotest:kotest-assertions-core:4.4.3")
    testImplementation("io.kotest:kotest-extensions-testcontainers:4.4.3")
    testImplementation("ch.qos.logback:logback-classic:1.2.5")
    testRuntimeOnly("org.postgresql:postgresql:42.2.23")
}
