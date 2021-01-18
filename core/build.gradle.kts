import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

base.archivesBaseName = "mikrate-core"
group = "factory.mikrate"
version = "0.1.0-SNAPSHOT"
val moduleName = "mikrate.core"

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

java {
    modularity.inferModulePath.set(true)
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
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

    compileKotlin {
        dependsOn(deleteCachedClasses)
    }

    compileJava {
        inputs.property("moduleName", moduleName)
        options.compilerArgs = listOf(
            "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
        )
    }
}

publishing {
    publications {
        withType<MavenPublication> {
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
            }
        }
    }
    repositories {
        maven("https://gitlab.com/api/v4/projects/23743161/packages/maven") {
            name = "GitLab"

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
