plugins {
    id("com.redmadrobot.android-config") version "0.16.1"
}

// Common configurations for all modules
redmadrobot {
    android {
        minSdk.set(24)
        targetSdk.set(31)
    }
}
