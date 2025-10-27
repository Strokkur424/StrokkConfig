pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://eldonexus.de/repository/maven-public/")
  }
}

rootProject.name = "StrokkConfig"

sequenceOf("annotations", "processor").forEach {
  include(it)
}

if (System.getenv("SKIP_TEST") != "true") {
  include("test-plugin")
}