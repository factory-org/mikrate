version = "0.1.0-SNAPSHOT"

extra.apply {
    set("artifactName", "core")
    set("moduleName", "mikrate.core")
}

dependencies {
    api(project(":dialects:dialect-api"))
}
