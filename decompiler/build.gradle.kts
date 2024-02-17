plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
}

group = "com.github.bkmbigo.composedecompiler"
version = "unspecified"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.kotlin.compiler)

    // Compose Dependencies
//    api(libs.androidx.compose.compiler)
    api(project(":external:androidx:compose:compiler"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
