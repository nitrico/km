pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("android") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("native.cocoapods") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
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
