extra.apply {
    set("artifactName", "dialects-postgres")
    set("moduleName", "mikrate.dialects.postgres")
}

dependencies {
    api(project(":dialects:dialect-api"))

    testImplementation(project(":dsl"))
    testImplementation(project(":executors:jdbc"))
    testImplementation("io.kotest:kotest-runner-junit5:4.3.2")
    testImplementation("io.kotest:kotest-assertions-core:4.3.2")
    testImplementation("io.kotest:kotest-extensions-testcontainers:4.3.2") {
        constraints {
            implementation("org.testcontainers:testcontainers:1.15.1") {
                because("https://github.com/testcontainers/testcontainers-java/issues/3574")
            }
        }
    }
    testImplementation("ch.qos.logback:logback-classic:1.2.3")
    testRuntimeOnly("org.postgresql:postgresql:42.2.16")
}
