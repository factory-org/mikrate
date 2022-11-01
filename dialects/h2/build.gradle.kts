val kotestVersion: String by project
val logbackVersion: String by project

extra.apply {
    set("artifactName", "dialects-h2")
    set("moduleName", "mikrate.dialects.h2")
}

dependencies {
    api(project(":dialects:dialect-api"))

    testImplementation(project(":dsl"))
    testImplementation(project(":executors:jdbc"))
    testImplementation(libs.bundles.kotest)
    runtimeOnly(libs.database.h2)
}
