rootProject.name = "mikrate"

include(
    ":dsl",
    ":core",
    ":executors:api",
    ":executors:jdbc",
    ":executors:r2dbc",
    ":auto-migrate",
    ":docs",
    ":dialects:api",
    ":dialects:postgres",
    ":dialects:sqlite",
    ":dialects:h2",
    ":dialects:generic",
)

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    versionCatalogs {
        create("libs") {
            val kotlin = version("kotlin", "1.7.20")
            val kotest = version("kotest", "5.5.3")

            library("logback", "ch.qos.logback:logback-classic:1.4.4")
            library("jetbrains-annotations", "org.jetbrains:annotations:23.0.0")
            library("r2dbc", "io.r2dbc:r2dbc-spi:0.8.5.RELEASE")

            library("kotest-junit5", "io.kotest", "kotest-runner-junit5").versionRef(kotest)
            library("kotest-assertions", "io.kotest", "kotest-assertions-core").versionRef(kotest)
            library("kotest-testcontainers", "io.kotest.extensions:kotest-extensions-testcontainers:1.3.4")

            library("database-h2", "com.h2database:h2:2.1.212")
            library("database-postgres", "org.postgresql:postgresql:42.2.26")
            library("database-sqlite", "org.xerial:sqlite-jdbc:3.36.0.2")

            library("kotlinx-coroutines-reactor", "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.2")

            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef(kotlin)
            plugin("kotlin-dokka", "org.jetbrains.dokka").versionRef(kotlin)
            plugin("mkdocs", "ru.vyarus.mkdocs").version("3.0.0")

            bundle("kotest", listOf("kotest-junit5", "kotest-assertions", "logback"))
        }
    }
}

project(":dialects:api").name = "dialect-api"
project(":executors:api").name = "executor-api"

val isCiServer = System.getenv().containsKey("CI")

buildCache {
    val urlString: String? = System.getenv("BUILD_CACHE_URL")
    if (urlString != null) {
        remote<HttpBuildCache> {
            url = uri(urlString)
            credentials {
                username = System.getenv("BUILD_CACHE_USER")
                password = System.getenv("BUILD_CACHE_PASSWORD")
            }
            isPush = isCiServer
        }
    }
    local {
        isEnabled = !isCiServer
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/dokka/dev")
    }
}
