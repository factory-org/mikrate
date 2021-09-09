plugins {
    id("ru.vyarus.mkdocs") version "2.1.1"
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
        "mkdocs:1.2.2",
        "mkdocs-material:7.2.6",
        "pygments:2.10.0",
        "pymdown-extensions:8.2",
        "mkdocs-git-revision-date-localized-plugin:0.9.3",
        "mkdocs-section-index:0.3.1"
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
