plugins {
    id("com.redmadrobot.detekt")
    id("com.redmadrobot.publish-config")
    alias(libs.plugins.versions)
}

val detektFormatting = libs.detekt.formatting

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    dependencies {
        detektPlugins(detektFormatting)
    }
}
