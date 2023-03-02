plugins {
    // Specify needed versions of AGP and KGP
    id("com.android.application") version "7.4.2" apply false
    kotlin("android") version "1.8.10" apply false

    id("com.redmadrobot.android-config") version "0.18-SNAPSHOT"
}

// Common configurations for all modules
redmadrobot {
    android {
        minSdk.set(24)
        targetSdk.set(33)
    }
}
