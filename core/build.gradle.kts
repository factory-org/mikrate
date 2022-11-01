extra.apply {
    set("artifactName", "core")
    set("moduleName", "mikrate.core")
}

dependencies {
    api(project(":dialects:dialect-api"))
    implementation(libs.jetbrains.annotations)
}
