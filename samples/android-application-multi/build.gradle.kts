import com.redmadrobot.build.extension.android

plugins {
    id("redmadrobot.root-project") version "0.10-SNAPSHOT"
}

// Common configurations for all modules
redmadrobot {
    android {
        minSdk = 24
        targetSdk = 30
    }
}
