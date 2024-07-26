plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.infrastructure.detekt)
    implementation(libs.infrastructure.kotlin)
    implementation(libs.infrastructure.publish)
    implementation(libs.pluginPublish)
    implementation(libs.mavenPublishPlugin)
    implementation(libs.gradle.kotlinDsl)
    implementation(libs.kotlinGradle)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}
