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
    ":dialects:generic"
)

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
