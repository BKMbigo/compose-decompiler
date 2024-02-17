plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "ComposeDecompiler"
include("decompiler")
include("desktop")
include(":external:androidx:compose:compiler")
