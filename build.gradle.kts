import com.diffplug.gradle.spotless.SpotlessPlugin

plugins {
  id("java-library")
  id("maven-publish")
  alias(libs.plugins.spotless)
}

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

  java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
  }

  repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
  }

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
          if (version.toString().endsWith("-SNAPSHOT")) {
            setUrl("https://eldonexus.de/repository/maven-snapshots/")
          } else {
            setUrl("https://eldonexus.de/repository/maven-releases/")
          }
        }
      }

      publications.create<MavenPublication>("maven") {
        from(components["java"])
        withBuildIdentifier()
      }
    }
  }
}
