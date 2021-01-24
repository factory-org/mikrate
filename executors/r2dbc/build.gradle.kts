base.archivesBaseName = "mikrate-executors-r2dbc"
group = "factory.mikrate"
version = "0.1.0-SNAPSHOT"
val moduleName = "mikrate.executors.r2dbc"

plugins {
    kotlin("jvm")
}

repositories {
    jcenter()
}

dependencies {
    api(project(":executors:api"))

    api("io.r2dbc:r2dbc-spi:0.8.3.RELEASE")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.2")
}

tasks.compileJava {
    inputs.property("moduleName", moduleName)
    options.compilerArgs = listOf(
        "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
    )
}
