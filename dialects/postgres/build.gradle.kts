version = "0.1.0-SNAPSHOT"

extra.apply {
    set("artifactName", "dialects-postgres")
    set("moduleName", "mikrate.dialects.postgres")
}

dependencies {
    api(project(":dialects:dialect-api"))
}

sourceSets.main
