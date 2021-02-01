base.archivesBaseName = "mikrate-dsl"
group = "factory.mikrate"
version = "0.1.0-SNAPSHOT"
val moduleName = "mikrate.dsl"

plugins {
    kotlin("jvm")
}

repositories {
    jcenter()
}

dependencies {
    implementation(project(":core"))
    implementation("org.jetbrains:annotations:20.1.0")

    // Tests
    testImplementation(project(":dialects:generic"))
    testImplementation(project(":dialects:postgres"))
    testImplementation(project(":dialects:sqlite"))
    testImplementation("io.kotest:kotest-runner-junit5:4.3.2")
    testImplementation("io.kotest:kotest-assertions-core:4.3.2")
    //testImplementation("io.kotest:kotest-extensions-testcontainers:4.3.2")
    //testImplementation("ch.qos.logback:logback-classic:1.2.3")
    //testImplementation("org.postgresql:postgresql:42.2.16")
}

tasks.compileJava {
    inputs.property("moduleName", moduleName)
    options.compilerArgs = listOf(
        "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
    )
}
