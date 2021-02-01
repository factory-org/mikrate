base.archivesBaseName = "mikrate-dialects-generic"
group = "factory.mikrate"
version = "0.1.0-SNAPSHOT"
val moduleName = "mikrate.dialects.generic"

plugins {
    kotlin("jvm")
}

repositories {
    jcenter()
}

dependencies {
    api(project(":dialects:dialect-api"))
}

tasks.compileJava {
    inputs.property("moduleName", moduleName)
    options.compilerArgs = listOf(
        "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
    )
}
