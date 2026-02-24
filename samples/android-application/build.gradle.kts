plugins {
    // Specify needed versions of AGP and KGP
    id("com.android.application") version "9.0.0" apply false
    kotlin("android") version "2.2.0" apply false

    id("com.redmadrobot.android-config") version "0.20.1"
    id("com.redmadrobot.detekt") version "0.20.1"
}

// Common configurations for all modules
redmadrobot {
    android {
        minSdk = 21
        targetSdk = 34
    }
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    dependencies {
        "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
    }
}
