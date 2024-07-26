import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id("com.redmadrobot.kotlin-library")
    id("convention.publish")
    id("org.gradle.kotlin.kotlin-dsl")
}

kotlin {
    // Keep gradle-infrastructure compatible with older versions of Gradle.
    // Language version should be in sync with the one used in Gradle
    // https://docs.gradle.org/current/userguide/compatibility.html#kotlin
    compilerOptions {
        apiVersion = KotlinVersion.KOTLIN_1_8
        languageVersion = KotlinVersion.KOTLIN_1_8
    }
}
