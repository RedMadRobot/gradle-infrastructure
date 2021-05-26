plugins {
    id("redmadrobot.android-library")
}

// Explicit API is enabled by default, but we can disable it if need
kotlin.explicitApi = null

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // ...
    // Dependencies for the module here
    // ...
}
