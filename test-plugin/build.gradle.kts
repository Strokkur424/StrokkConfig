plugins {
    alias(libs.plugins.run.paper)
    alias(libs.plugins.blossom)
}

dependencies {
    compileOnly(project(":strokk-config-annotations"))
    annotationProcessor(project(":strokk-config-processor"))

    compileOnly(libs.paper.api)
}

tasks.runServer {
    minecraftVersion(libs.versions.minecraft.get())
    jvmArgs("-Xmx3G", "-Xms3G", "-Dcom.mojang.eula.agree=true")
}

tasks.processResources {
    filesMatching("paper-plugin.yml") {
        expand(
            "version" to version,
            "mcVersion" to libs.versions.minecraft.get()
        )
    }
}

sourceSets.main {
    blossom.javaSources {
        property("configurate", libs.versions.configurate.get())
    }
}