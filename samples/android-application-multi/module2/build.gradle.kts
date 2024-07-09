plugins {
    id("com.redmadrobot.android-library")
}

// Explicit API is enabled by default, but we can disable it if needed
kotlin.explicitApi = null

android {
    namespace = "com.redmadrobot.samples.module2"
}

dependencies {
    implementation(kotlin("stdlib"))
    // ...
    // Dependencies for the module here
    // ...
}
