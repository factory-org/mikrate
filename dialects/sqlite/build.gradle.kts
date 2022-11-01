extra.apply {
    set("artifactName", "dialects-sqlite")
    set("moduleName", "mikrate.dialects.sqlite")
}

dependencies {
    api(project(":dialects:dialect-api"))
    testImplementation(project(":dsl"))
    testImplementation(libs.jetbrains.annotations)
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.database.sqlite)
}

tasks {
    compileTestKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xopt-in=kotlin.contracts.ExperimentalContracts")
        }
    }
}
