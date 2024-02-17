import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
    google()
}

tasks.withType(KotlinCompile::class.java).configureEach {
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-Xjvm-default=all"
        )
    }
}

dependencies {
    compileOnly(libs.kotlin.compiler)
}
