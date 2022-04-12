pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("android")               version kotlinVersion apply false
        kotlin("multiplatform")         version kotlinVersion apply false
        kotlin("native.cocoapods")      version kotlinVersion apply false
        kotlin("plugin.serialization")  version kotlinVersion apply false
        id("com.github.ben-manes.versions") version "0.42.0"
    }
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "km"
include(":androidApp")
include(":shared")
