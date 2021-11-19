pluginManagement {
    repositories {
        // If we use SNAPSHOT version of infrastructure,
        // we should publish it to mavenLocal first
        mavenLocal()
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "android-application-multi"
include(":app", ":module1", ":module2")
