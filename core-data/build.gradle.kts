plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.fetch.test.core.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":domain"))

    // Android KTX
    implementation(libs.androidx.core.ktx)

    // Ktor Client for HTTP requests
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    // Room Database for local persistence
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Kotlinx Serialization for data class serialization (used by Ktor)
    implementation(libs.kotlinx.serialization.json)

    // DataStore Preferences
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core.android)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
}