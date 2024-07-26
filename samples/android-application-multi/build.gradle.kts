plugins {
    // Versions of AGP and KGP are specified in buildSrc module
    id("com.redmadrobot.android-config") version "0.19"
}

// Common configurations for all modules
redmadrobot {
    android {
        minSdk = 24
        targetSdk = 34
    }
}
