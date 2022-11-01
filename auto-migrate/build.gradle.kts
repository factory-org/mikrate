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
    testImplementation(libs.kotlinx.coroutines.reactive)
    testImplementation(libs.bundles.kotest)
    testRuntimeOnly(libs.database.jdbc.sqlite)
    testRuntimeOnly(libs.database.r2dbc.h2)
    testRuntimeOnly(libs.database.jdbc.h2)
}
