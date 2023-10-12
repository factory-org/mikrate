extra.apply {
    set("artifactName", "dialects-sqlite")
    set("moduleName", "mikrate.dialects.sqlite")
}

dependencies {
    api(project(":dialects:dialect-api"))
    testImplementation(project(":dsl"))
    testImplementation(libs.jetbrains.annotations)
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.database.jdbc.sqlite)
}

tasks {
    compileTestKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-opt-in=kotlin.contracts.ExperimentalContracts")
        }
    }
}
