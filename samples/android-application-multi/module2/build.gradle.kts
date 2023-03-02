plugins {
    id("com.redmadrobot.android-library")
}

// Explicit API is enabled by default, but we can disable it if need
kotlin.explicitApi = null

android {
    namespace = "com.redmadrobot.samples.module2"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // ...
    // Dependencies for the module here
    // ...
}
