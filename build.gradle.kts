import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.dokka.gradle.GradleDokkaSourceSetBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.dokka)
}

val snapshotVersion = "0.1.5"

repositories {
    mavenCentral()
}

subprojects {
    if (!file("src/main/kotlin").exists() || name == "buildSrc") {
        return@subprojects
    }

    val sub = this
    val ciTag = System.getenv("CI_COMMIT_TAG")

    apply<KotlinPluginWrapper>()
    apply<MavenPublishPlugin>()
    apply<DokkaPlugin>()

    group = "factory.mikrate"
    version = if (!ciTag.isNullOrBlank() && ciTag.startsWith("v")) {
        ciTag.substring(1)
    } else {
        "$snapshotVersion-SNAPSHOT"
    }

    afterEvaluate {
        // Gemnasium doesn't like it: https://gitlab.com/gitlab-org/gitlab/-/issues/340463
        @Suppress("DEPRECATION")
        configure<BasePluginConvention> {
            archivesBaseName = "mikrate-${sub.extra["artifactName"]}"
        }
    }

    configure<JavaPluginExtension> {
        modularity.inferModulePath.set(true)

        withSourcesJar()
        withJavadocJar()

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    configure<KotlinJvmProjectExtension> {
        explicitApi()
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    repositories {
        mavenCentral()
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
                val buildDirectory = layout.buildDirectory.get()
                val kotlinClassDir = buildDirectory.file("classes/kotlin")
                if (kotlinClassDir.asFile.exists()) {
                    fileTree(kotlinClassDir).forEach {
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

        val dokkaConfig: GradleDokkaSourceSetBuilder.() -> Unit = {
            sourceLink {
                val dir = sub.file("src/main/kotlin")
                localDirectory.set(dir)
                val url =
                    "https://gitlab.com/factory-org/tools/mikrate/-/tree/master/${dir.relativeTo(rootDir)}"
                remoteUrl.set(URL(url))
                remoteLineSuffix.set("#L")
            }
            skipEmptyPackages.set(true)
            jdkVersion.set(17)
            skipDeprecated.set(true)
            reportUndocumented.set(true)
        }

        named<DokkaTask>("dokkaHtml") {
            dokkaSourceSets.named("main", dokkaConfig)
        }

        named<DokkaTaskPartial>("dokkaHtmlPartial") {
            moduleName.set(sub.path.substring(1).replace(":", "."))
            dokkaSourceSets.named("main", dokkaConfig)
        }

        named<DokkaTask>("dokkaJavadoc") {
            dokkaSourceSets.named("main") {
                skipEmptyPackages.set(true)
                jdkVersion.set(17)
                skipDeprecated.set(true)
            }
        }

        named<Javadoc>("javadoc") {
            isEnabled = false
        }

        named<Jar>("javadocJar") {
            from(named("dokkaJavadoc").get().outputs)
        }
    }

    configure<PublishingExtension> {
        publications {
            register<MavenPublication>("maven") {
                afterEvaluate {
                    artifactId = "mikrate-${sub.extra["artifactName"]}"
                }

                from(components["java"])

                pom {
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
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
