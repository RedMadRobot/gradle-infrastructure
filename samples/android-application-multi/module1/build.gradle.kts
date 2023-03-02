plugins {
    id("com.redmadrobot.android-library")
}

android {
    namespace = "com.redmadrobot.samples.module1"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // ...
    // Dependencies for the module here
    // ...
}
