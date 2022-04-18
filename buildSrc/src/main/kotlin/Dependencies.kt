object AndroidX {
    const val activityCompose = "androidx.activity:activity-compose:1.4.0"
    const val lifecycleViewmodelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1"
    const val activityKtx = "androidx.activity:activity-ktx:1.6.0-alpha01"
    const val appcompat = "androidx.appcompat:appcompat:1.4.1"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.3"
    const val coreKtx = "androidx.core:core-ktx:1.7.0"
    const val securityCrypto = "androidx.security:security-crypto:1.0.0" // for EncryptedSharedPreferences

    object Compose {
        const val version = "1.1.1"
        const val animation = "androidx.compose.animation:animation:${version}"
        const val foundation = "androidx.compose.foundation:foundation:${version}"
        const val material = "androidx.compose.material:material:${version}"
        const val materialIconsCore = "androidx.compose.material:material-icons-core:${version}"
        const val materialIconsExtended = "androidx.compose.material:material-icons-extended:${version}"
        const val ui = "androidx.compose.ui:ui:${version}"
        const val uiTooling = "androidx.compose.ui:ui-tooling:${version}" // Tooling support (Previews, etc.)
        const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4:${version}" // UI Tests
    }
}

object Coil {
    private const val version = "2.0.0-rc03"
    const val coil = "io.coil-kt:coil:$version"
    const val coilCompose = "io.coil-kt:coil-compose:$version"
}

object GradlePlugins {
    const val buildKonfig = "com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.11.0"
    const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:0.42.0"
}

object Google {
    const val material = "com.google.android.material:material:1.6.0-beta01"
}

object Koin { // https://insert-koin.io/docs/setup/v3.2
    private const val version = "3.1.6"
    const val core = "io.insert-koin:koin-core:$version"
    const val android = "io.insert-koin:koin-android:$version"
    const val test = "io.insert-koin:koin-test:$version"
}

object Kotlin {
    const val version = "1.6.10"
}

object KotlinX {
    object Coroutines {
        const val version = "1.6.1"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${version}"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${version}"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${version}"
    }
    const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.3.2"
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2"
}

object Ktor {
    private const val version = "2.0.0"
    const val clientCore = "io.ktor:ktor-client-core:$version"
    const val clientAndroid = "io.ktor:ktor-client-android:$version"
    const val clientIos = "io.ktor:ktor-client-ios:$version"
    const val clientContentNegotiation = "io.ktor:ktor-client-content-negotiation:$version"
    const val clientLogging = "io.ktor:ktor-client-logging:$version"
    const val clientSerialization = "io.ktor:ktor-client-serialization:$version"
    const val serializationKotlinXJson = "io.ktor:ktor-serialization-kotlinx-json:$version"
}

object TouchLab {
    const val kermit = "co.touchlab:kermit:1.0.0"
    object MultiplatformSettings {
        const val multiplatformSettings = "com.russhwolf:multiplatform-settings:0.8.1"
        const val test = "com.russhwolf:multiplatform-settings-test:0.8.1"
    }
}
