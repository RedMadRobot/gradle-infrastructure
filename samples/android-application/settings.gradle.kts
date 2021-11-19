pluginManagement {
    repositories {
        // If we use SNAPSHOT version of infrastructure,
        // we should publish it to mavenLocal first
        mavenLocal()
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "android-application"
include(":app")
