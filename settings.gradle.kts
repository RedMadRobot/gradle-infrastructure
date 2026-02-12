plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "gradle-infrastructure"

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

include(
    ":infrastructure-android",
    ":infrastructure-kotlin",
    ":infrastructure-detekt",
    ":infrastructure-publish",
    ":infrastructure-common",
)
