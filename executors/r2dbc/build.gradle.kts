version = "0.1.0-SNAPSHOT"

extra.apply {
    set("artifactName", "executors-r2dbc")
    set("moduleName", "mikrate.executors.r2dbc")
}

dependencies {
    api(project(":executors:executor-api"))

    api("io.r2dbc:r2dbc-spi:0.8.3.RELEASE")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.2")
}
