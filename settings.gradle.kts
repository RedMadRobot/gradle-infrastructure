enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "redmadrobot") {
                useModule("com.redmadrobot.build:infrastructure:${requested.version}")
            }
        }
    }
}

rootProject.name = "gradle-infrastructure"

include(
    ":infrastructure",
    ":infrastructure-android",
    ":infrastructure-kotlin",
    ":infrastructure-detekt",
    ":infrastructure-publish",
    ":infrastructure-common",
)

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    versionCatalogs {
        create("libs")
    }
}
