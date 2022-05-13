plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
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

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
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

    with(Google.Accompanist) {
        implementation(swiperefresh)
        implementation(systemuicontroller)
    }

    with(Koin) {
        implementation(core)
        implementation(androidxCompose)
        testImplementation(test)
    }

    with(KotlinX) {
        compileOnly(datetime) // needed for run.startDate
    }

    implementation("io.github.raamcosta.compose-destinations:core:1.5.2-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.5.2-beta")
}
