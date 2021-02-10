extra.apply {
    set("artifactName", "executors-jdbc")
    set("moduleName", "mikrate.executors.jdbc")
}

dependencies {
    api(project(":executors:executor-api"))
}
