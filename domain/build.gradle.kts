plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    // Kotlin standard library
    implementation(libs.kotlin.stdlib.jdk8)

    // Coroutines (if your interfaces/use cases use Flow/suspend functions)
    implementation(libs.kotlinx.coroutines.core)

    // Kotlinx Serialization runtime (if your domain models are @Serializable)
    implementation(libs.kotlinx.serialization.json)

    // Room annotations (only annotations, not runtime or compiler)
    implementation(libs.androidx.room.common) // For @Entity, @PrimaryKey annotations
}