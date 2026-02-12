plugins {
    id("com.redmadrobot.detekt")
    id("com.redmadrobot.publish-config")
    alias(libs.plugins.versions)
}

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    dependencies {
        detektPlugins(libs.detekt.formatting)
    }
}
