plugins {
    id("redmadrobot.root-project") version "0.8.2"
}

// Common configurations for all modules
redmadrobot {
    android {
        minSdk = 24
        targetSdk = 30
    }
}
