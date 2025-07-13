plugins {
    alias(libs.plugins.blossom)
}

dependencies {
    implementation(project(":strokk-config-annotations"))

    compileOnly(libs.jspecify)
    compileOnly(libs.jetbrains.annotations)

    testImplementation(libs.juint.jupiter)
    testCompileOnly(libs.jspecify)
    testCompileOnly(libs.jetbrains.annotations)
}

tasks.test {
    useJUnitPlatform()
}

sourceSets.main {
    blossom.javaSources {
        property("version", project.version.toString())
    }
}