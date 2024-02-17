import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
}

group = "com.github.bkmbigo.composedecompiler.desktop"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

@OptIn(ExperimentalComposeLibrary::class)
dependencies {
    implementation(project(":decompiler"))

    implementation(compose.material3)
    implementation(compose.materialIconsExtended)

    implementation(compose.components.resources)
    implementation(compose.desktop.components.splitPane)

    implementation(compose.desktop.currentOs) {
        exclude("org.jetbrains.compose.material")

    }

    // Other dependencies
    implementation(libs.fifesoft.rsyntaxtextarea)
    implementation(libs.fifesoft.rstaui)

//    implementation(libs.mpfilepicker) {
//        // WHY DOES THIS LIBRARY EXPORT ITS DEPENDENCIES!!!!!!!!
//        exclude("org.jetbrains.compose.material")
//    }

    val lwjglVersion = libs.versions.lwjgl.get()

    listOf("lwjgl", "lwjgl-tinyfd").forEach { lwjglDep ->
        implementation("org.lwjgl:${lwjglDep}:${lwjglVersion}")
        listOf(
            "natives-windows",
            "natives-windows-x86",
            "natives-windows-arm64",
            "natives-macos",
            "natives-macos-arm64",
            "natives-linux",
            "natives-linux-arm64",
            "natives-linux-arm32",
        ).forEach { native ->
            runtimeOnly("org.lwjgl:${lwjglDep}:${lwjglVersion}:${native}")
        }
    }

}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Compose Decompiler"
            packageVersion = "1.0.0"
        }
    }
}
