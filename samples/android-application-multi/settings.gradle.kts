pluginManagement {
    repositories {
        // If we use SNAPSHOT version of infrastructure,
        // we should publish it to mavenLocal first
        mavenLocal()
        gradlePluginPortal()
        google()
        maven(url = "https://dl.bintray.com/redmadrobot-opensource/android")
    }
}

rootProject.name = "my-app"
include(":app", ":module1", ":module2")
