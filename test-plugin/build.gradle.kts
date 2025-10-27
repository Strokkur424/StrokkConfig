plugins {
  alias(libs.plugins.run.paper)
  alias(libs.plugins.blossom)
}

dependencies {
  compileOnly(project(":annotations"))
  annotationProcessor(project(":processor"))

  compileOnly(libs.paper.api)
  compileOnly(libs.configurate.hocon)
  compileOnly(libs.configurate.yaml)
}

tasks.runServer {
  minecraftVersion(libs.versions.minecraft.get())
  jvmArgs("-Xmx3G", "-Xms3G", "-Dcom.mojang.eula.agree=true")
}

tasks.processResources {
  val version = project.version
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