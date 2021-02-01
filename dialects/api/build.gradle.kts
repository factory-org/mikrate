base.archivesBaseName = "mikrate-dialects-api"
group = "factory.mikrate"
version = "0.1.0-SNAPSHOT"
val moduleName = "mikrate.dialects.api"

plugins {
    kotlin("jvm")
}

repositories {
    jcenter()
}

tasks.compileJava {
    inputs.property("moduleName", moduleName)
    options.compilerArgs = listOf(
        "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
    )
}
