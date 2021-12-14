plugins {
    `kotlin-dsl`
}

dependencies {
    api(libs.infrastructure)
    api(libs.pluginPublish)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}
