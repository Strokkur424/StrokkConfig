pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://eldonexus.de/repository/maven-public/")
    }
}

rootProject.name = "StrokkConfig"

sequenceOf("annotations", "processor", "test-plugin").forEach {
    include("strokk-config-$it")
    project(":strokk-config-$it").projectDir = rootDir.resolve(it)
}