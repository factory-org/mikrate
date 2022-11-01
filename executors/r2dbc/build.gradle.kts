extra.apply {
    set("artifactName", "executors-r2dbc")
    set("moduleName", "mikrate.executors.r2dbc")
}

dependencies {
    api(project(":executors:executor-api"))

    api(libs.r2dbc)
    implementation(libs.kotlinx.coroutines.reactor)
}
