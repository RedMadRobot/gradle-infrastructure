plugins {
    id("redmadrobot.root-project") version "0.10"
}

// Common configurations for all modules
redmadrobot {
    android {
        minSdk.set(24)
        targetSdk.set(30)
    }
}
