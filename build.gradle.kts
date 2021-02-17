import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.dokka.gradle.GradleDokkaSourceSetBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

plugins {
    kotlin("jvm") version "1.4.21" apply false
    id("org.jetbrains.dokka") version "1.4.20.2-dev-62"
}

val snapshotVersion = "0.1.0"

repositories {
    jcenter()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/dokka/dev")
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
        configure<BasePluginConvention> {
            archivesBaseName = "mikrate-${sub.extra["artifactName"]}"
        }
    }

    configure<JavaPluginExtension> {
        @Suppress("UnstableApiUsage")
        modularity.inferModulePath.set(true)

        @Suppress("UnstableApiUsage")
        withSourcesJar()
        @Suppress("UnstableApiUsage")
        withJavadocJar()

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    configure<KotlinJvmProjectExtension> {
        explicitApi()
    }

    repositories {
        mavenCentral()
        jcenter()  // TODO: Remove in the future
        maven("https://maven.pkg.jetbrains.space/kotlin/p/dokka/dev")
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
            jdkVersion.set(11)
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
                jdkVersion.set(11)
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
