plugins {
    `kotlin-dsl`
}

dependencies {
    api(libs.infrastructure.detekt)
    api(libs.infrastructure.kotlin)
    api(libs.infrastructure.publish)
    api(libs.pluginPublish)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}
