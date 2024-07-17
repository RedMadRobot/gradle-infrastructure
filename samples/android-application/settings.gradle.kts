@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        // If we use SNAPSHOT version of infrastructure,
        // we should publish it to mavenLocal first
        mavenLocal()
        gradlePluginPortal()
        google {
            content {
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
                includeGroupAndSubgroups("androidx")
            }
        }
    }
}

// Configure repositories for all subprojects in one place
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS

    repositories {
        google {
            content {
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
                includeGroupAndSubgroups("androidx")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "android-application"
include(":app")
