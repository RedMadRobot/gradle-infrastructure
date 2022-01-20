plugins {
    id("com.redmadrobot.android-config") version "0.15"
}

// Common configurations for all modules
redmadrobot {
    android {
        minSdk.set(24)
        targetSdk.set(31)
    }
}
