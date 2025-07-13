dependencies {
    implementation(project(":strokk-config-annotations"))

    compileOnly(libs.jspecify)
    compileOnly(libs.jetbrains.annotations)
}

dependencies {
    testImplementation(libs.juint.jupiter)
}

tasks.test {
    useJUnitPlatform()
}