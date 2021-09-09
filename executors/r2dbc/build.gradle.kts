extra.apply {
    set("artifactName", "executors-r2dbc")
    set("moduleName", "mikrate.executors.r2dbc")
}

dependencies {
    api(project(":executors:executor-api"))

    api("io.r2dbc:r2dbc-spi:0.8.5.RELEASE")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.2")
}
