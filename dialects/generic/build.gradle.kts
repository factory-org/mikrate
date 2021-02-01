version = "0.1.0-SNAPSHOT"

extra.apply {
    set("artifactName", "dialects-generic")
    set("moduleName", "mikrate.dialects.generic")
}

dependencies {
    api(project(":dialects:dialect-api"))
}
