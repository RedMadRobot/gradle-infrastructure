plugins {
    id("com.redmadrobot.android-library")
    convention.jvm
}

android {
    namespace = "com.redmadrobot.samples.module1"
}

dependencies {
    implementation(kotlin("stdlib"))
    // ...
    // Dependencies for the module here
    // ...
}
