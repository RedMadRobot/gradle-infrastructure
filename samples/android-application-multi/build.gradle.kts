plugins {
    id("redmadrobot.root-project") version "1.0-SNAPSHOT"
}

// Common configurations for all modules
redmadrobot {
    android {
        minSdk.set(24)
        targetSdk.set(30)
    }
}
