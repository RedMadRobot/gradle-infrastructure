enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "gradle-infrastructure"

include(
    ":infrastructure-android",
    ":infrastructure-kotlin",
    ":infrastructure-detekt",
    ":infrastructure-publish",
    ":infrastructure-common",
)
