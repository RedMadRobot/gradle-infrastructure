pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven(url = "https://dl.bintray.com/redmadrobot-opensource/android")
    }
}

rootProject.name = "my-app"
include(":app")
