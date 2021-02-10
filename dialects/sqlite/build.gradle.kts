extra.apply {
    set("artifactName", "dialects-sqlite")
    set("moduleName", "mikrate.dialects.sqlite")
}

dependencies {
    api(project(":dialects:dialect-api"))
}
