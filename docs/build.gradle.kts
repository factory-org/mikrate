plugins {
    id("ru.vyarus.mkdocs") version "2.0.1"
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
        "mkdocs:1.1.2",
        "mkdocs-material:6.2.8",
        "pygments:2.7.4",
        "pymdown-extensions:8.1.1",
        "mkdocs-git-revision-date-localized-plugin:0.8"
    )
}
