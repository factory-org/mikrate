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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val kotlin = version("kotlin", "1.9.10")
            val kotlinxCoroutines = version("kotlinx-coroutines", "1.7.3")
            val kotest = version("kotest", "5.7.2")
            val dokka = version("dokka", "1.9.0")

            library("logback", "ch.qos.logback:logback-classic:1.4.11")
            library("jetbrains-annotations", "org.jetbrains:annotations:24.0.1")
            library("r2dbc", "io.r2dbc:r2dbc-spi:1.0.0.RELEASE")

            library("kotest-junit5", "io.kotest", "kotest-runner-junit5").versionRef(kotest)
            library("kotest-assertions", "io.kotest", "kotest-assertions-core").versionRef(kotest)
            library("kotest-testcontainers", "io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")

            library("database-r2dbc-h2", "io.r2dbc:r2dbc-h2:1.0.0.RELEASE")
            library("database-jdbc-h2", "com.h2database:h2:2.2.224")
            library("database-jdbc-postgres", "org.postgresql:postgresql:42.6.0")
            library("database-jdbc-sqlite", "org.xerial:sqlite-jdbc:3.43.0.0")

            library("kotlinx-coroutines-reactor", "org.jetbrains.kotlinx", "kotlinx-coroutines-reactor").versionRef(kotlinxCoroutines)
            library("kotlinx-coroutines-reactive", "org.jetbrains.kotlinx", "kotlinx-coroutines-reactive").versionRef(kotlinxCoroutines)

            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef(kotlin)
            plugin("kotlin-dokka", "org.jetbrains.dokka").versionRef(dokka)
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
