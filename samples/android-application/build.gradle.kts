plugins {
    // Specify needed versions of AGP and KGP
    id("com.android.application") version "9.0.0" apply false
    kotlin("android") version "2.2.0" apply false

    id("com.redmadrobot.android-config") version "0.20.0-SNAPSHOT"
}

// Common configurations for all modules
redmadrobot {
    android {
        minSdk = 21
        targetSdk = 34
    }
}
