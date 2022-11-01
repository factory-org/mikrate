extra.apply {
    set("artifactName", "dialects-postgres")
    set("moduleName", "mikrate.dialects.postgres")
}

dependencies {
    api(project(":dialects:dialect-api"))

    testImplementation(project(":dsl"))
    testImplementation(project(":executors:jdbc"))
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.kotest.testcontainers)
    testRuntimeOnly(libs.database.postgres)
}
