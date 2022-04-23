plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = AndroidConfig.Sdk.compile
    defaultConfig {
        applicationId = AndroidConfig.AppId
        minSdk = AndroidConfig.Sdk.min
        targetSdk = AndroidConfig.Sdk.target
        versionCode = AndroidConfig.Version.code
        versionName = AndroidConfig.Version.name
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    lint {
        warningsAsErrors = true
        abortOnError = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = AndroidX.Compose.version
    }
}

dependencies {
    implementation(project(":shared"))

    with(AndroidX) {
        implementation(activityCompose)
    }

    with(AndroidX.Compose) {
        implementation(animation)
        implementation(ui)
        implementation(uiTooling)
        implementation(foundation)
        implementation(material3)
        implementation(materialIconsCore)
        implementation(materialIconsExtended)
        androidTestImplementation(uiTestJunit4)
    }

    with(Coil) {
        implementation(coilCompose)
    }

    with(Koin) {
        implementation(core)
        implementation(android)
        testImplementation(test)
    }
}
