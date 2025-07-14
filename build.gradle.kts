import com.diffplug.gradle.spotless.SpotlessPlugin

plugins {
    id("java-library")
    id("maven-publish")
    alias(libs.plugins.spotless)
}

group = "net.strokkur"
version = System.getenv("VERSION") ?: "1.0.0"

allprojects {
    apply {
        plugin<SpotlessPlugin>()
    }

    spotless {
        java {
            licenseHeaderFile(rootProject.file("HEADER"))
            target("**/*.java")
        }
    }
}

subprojects {
    apply {
        plugin<JavaLibraryPlugin>()
    }

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    version = rootProject.version
    group = rootProject.group

    if (name.contains("annotations")) {
        java {
            withSourcesJar()
            withJavadocJar()
        }
    }

    if (name.contains("processor") || name.contains("annotations")) {
        apply {
            plugin<MavenPublishPlugin>()
        }

        publishing {
            repositories {
                maven {
                    authentication {
                        credentials(PasswordCredentials::class) {
                            username = System.getenv("NEXUS_USERNAME")
                            password = System.getenv("NEXUS_PASSWORD")
                        }
                    }

                    name = "EldoNexus"
                    setUrl("https://eldonexus.de/repository/maven-releases/")
                }
            }

            publications.create<MavenPublication>("maven") {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()

                from(components["java"])
            }
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
