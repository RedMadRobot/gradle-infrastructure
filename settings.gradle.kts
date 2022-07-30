enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "gradle-infrastructure"

include(
    ":infrastructure-android",
    ":infrastructure-kotlin",
    ":infrastructure-detekt",
    ":infrastructure-publish",
    ":infrastructure-common",
)
