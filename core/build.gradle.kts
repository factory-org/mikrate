extra.apply {
    set("artifactName", "core")
    set("moduleName", "mikrate.core")
}

dependencies {
    api(project(":dialects:dialect-api"))
    implementation("org.jetbrains:annotations:20.1.0")
}
