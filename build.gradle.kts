import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21" apply false
}

subprojects {
    if (!file("src/main/kotlin").exists()) {
        return@subprojects
    }

    val sub = this

    apply<KotlinPluginWrapper>()
    apply<MavenPublishPlugin>()

    group = "factory.mikrate"

    afterEvaluate {
        configure<BasePluginConvention> {
            archivesBaseName = "mikrate-${sub.extra["artifactName"]}"
        }
    }

    configure<JavaPluginExtension> {
        modularity.inferModulePath.set(true)

        withSourcesJar()

        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }

    configure<KotlinJvmProjectExtension> {
        explicitApi()
    }

    repositories {
        jcenter()
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()
        }

        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "11"
            }
        }

        val deleteCachedClasses by registering {
            inputs.file("src/main/java/module-info.java")
            doLast {
                val moduleModified = file("src/main/java/module-info.java").lastModified()
                if (file("$buildDir/classes/kotlin").exists()) {
                    fileTree("$buildDir/classes/kotlin").forEach {
                        if (moduleModified > it.lastModified()) {
                            it.delete()
                        }
                    }
                }
            }
        }

        getByName<KotlinCompile>("compileKotlin") {
            dependsOn(deleteCachedClasses)
        }

        named<JavaCompile>("compileJava") {
            inputs.property("moduleName", sub.extra["moduleName"])
            val outputPath =
                sub.extensions.getByName<SourceSetContainer>("sourceSets").named("main").get().output.asPath
            options.compilerArgs = listOf(
                "--patch-module", "${sub.extra["moduleName"]}=$outputPath"
            )
        }
    }

    configure<PublishingExtension> {
        publications {
            register<MavenPublication>("maven") {
                afterEvaluate {
                    artifactId = "mikrate-${sub.extra["artifactName"]}"
                }

                pom {
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("cromefire_")
                            name.set("Cromefire_")
                            email.set("cromefire_@outlook.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://gitlab.com/factory-org/tools/mikrate.git")
                        developerConnection.set("scm:git:ssh://git@gitlab.com/factory-org/tools/mikrate.git")
                        url.set("https://gitlab.com/factory-org/tools/mikrate")
                    }
                }
            }
        }
        repositories {
            maven("https://gitlab.com/api/v4/projects/23743161/packages/maven") {
                name = "gitlab"

                credentials(HttpHeaderCredentials::class.java) {
                    name = "Job-Token"
                    value = System.getenv("CI_JOB_TOKEN") ?: ""
                }
                authentication {
                    register("header", HttpHeaderAuthentication::class.java)
                }
            }
        }
    }
}
