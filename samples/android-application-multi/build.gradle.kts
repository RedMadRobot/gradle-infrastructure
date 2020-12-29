plugins {
    id("redmadrobot.root-project") version "0.6"
}

// Common configurations for all modules
redmadrobot {
    android {
        minSdk = 24
        targetSdk = 30
    }
}
