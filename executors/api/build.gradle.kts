version = "0.1.0-SNAPSHOT"

extra.apply {
    set("artifactName", "executors-api")
    set("moduleName", "mikrate.executors.api")
}

dependencies {
    api(project(":dialects:dialect-api"))
}
