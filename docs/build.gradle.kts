plugins {
    id("ru.vyarus.mkdocs") version "3.0.0"
    idea
}

idea {
    module {
        resourceDirs = setOf(file("src"))
    }
}

mkdocs {
    sourcesDir = "src"
    publish.docPath = ""
}

python {
    pip(
        "mkdocs:1.4.1",
        "mkdocs-material:8.5.7",
        "pygments:2.13.0",
        "pymdown-extensions:9.7",
        "mkdocs-git-revision-date-localized-plugin:1.1.0",
        "mkdocs-section-index:0.3.4"
    )
}

tasks {
    mkdocsBuild {
        inputs.file("src/mkdocs.yml")
        inputs.dir("src/docs")
    }
    mkdocsServe {
        inputs.file("src/mkdocs.yml")
        inputs.dir("src/docs")
    }
}
