import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.codingfeline.buildkonfig")
    id("com.github.ben-manes.versions") // ./gradlew dependencyUpdates -Drevision=release
}

// CocoaPods requires the podspec to have a version.
version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "15.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Koin.core)
                implementation(KotlinX.Coroutines.core)
                implementation(KotlinX.datetime)
                with(Ktor) {
                    implementation(clientCore)
                    implementation(clientContentNegotiation)
                    implementation(clientLogging)
                    implementation(clientSerialization)
                    implementation(serializationKotlinXJson)
                }
                api(TouchLab.kermit)
                implementation(TouchLab.MultiplatformSettings.multiplatformSettings)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(AndroidX.securityCrypto)
                implementation(Koin.android)
                implementation(Ktor.clientAndroid)
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(Ktor.clientIos)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = AndroidConfig.Sdk.compile
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AndroidConfig.Sdk.min
        targetSdk = AndroidConfig.Sdk.target
    }
}

buildkonfig {
    packageName = "dev.miguelmoreno.km"

    defaultConfigs {
        buildConfigField(
            STRING,
            "STRAVA_CLIENT_SECRET",
            findProperty("STRAVA_CLIENT_SECRET") as? String
                ?: throw Exception("STRAVA_CLIENT_SECRET is not set")
        )
    }
}
