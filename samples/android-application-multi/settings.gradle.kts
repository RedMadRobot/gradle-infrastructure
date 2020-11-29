pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven(url = "https://dl.bintray.com/redmadrobot-opensource/android")
    }

    resolutionStrategy {
        eachPlugin {
            // It may be useful to set infrastructure version here.
            // In this case you don't need to specify it for each of plugins.
            if (requested.id.namespace == "redmadrobot") {
                useModule("com.redmadrobot.build:infrastructure:0.4.1")
            }
        }
    }
}

rootProject.name = "my-app"
include(":app", ":module1", ":module2")
