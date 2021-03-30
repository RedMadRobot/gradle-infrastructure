pluginManagement {
    repositories {
        // If we use SNAPSHOT version of infrastructure,
        // we should publish it to mavenLocal first
        mavenLocal()
        gradlePluginPortal()
        google()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "redmadrobot") {
                useModule("com.redmadrobot.build:infrastructure:${requested.version}")
            }
        }
    }
}

rootProject.name = "my-app"
include(":app")
