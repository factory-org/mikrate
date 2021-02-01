version = "0.1.0-SNAPSHOT"

extra.apply {
    set("artifactName", "dialects-sqlite")
    set("moduleName", "mikrate.dialects.sqlite")
}

dependencies {
    api(project(":dialects:dialect-api"))
}
