extra.apply {
    set("artifactName", "dialects-generic")
    set("moduleName", "mikrate.dialects.generic")
}

dependencies {
    api(project(":dialects:dialect-api"))
}
