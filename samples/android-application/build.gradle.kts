plugins {
    // Specify needed versions of AGP and KGP
    id("com.android.application") version "8.5.1" apply false
    kotlin("android") version "2.0.0" apply false

    id("com.redmadrobot.android-config") version "0.19.1"
}

// Common configurations for all modules
redmadrobot {
    android {
        minSdk = 21
        targetSdk = 34
    }
}
