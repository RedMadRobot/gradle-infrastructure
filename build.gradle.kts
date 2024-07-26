plugins {
    id("com.redmadrobot.detekt")
    id("com.redmadrobot.publish-config")
    alias(libs.plugins.versions)
}

redmadrobot {
    jvmTarget = JavaVersion.VERSION_11
}

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
    }
}
