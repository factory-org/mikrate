version = "0.1.0-SNAPSHOT"

extra.apply {
    set("artifactName", "automigrate")
    set("moduleName", "mikrate.automigrate")
}

dependencies {
    api(project(":executors:executor-api"))
    api(project(":dialects:dialect-api"))
    api(project(":core"))
}
