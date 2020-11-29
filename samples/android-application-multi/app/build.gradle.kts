plugins {
    id("redmadrobot.application")
}

// Plugin "redmadrobot.application" configures build types, SDK versions, proguard and so on.
// We only should configure applicationId and version name and code.
android {
    defaultConfig {
        applicationId = "com.redmadrobot.samples"
        versionCode = 1
        versionName = "1.0"
        // If we need any additional configurations we still can add it.
        // For example we need to run instrumentation tests.
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    // Kotlin already added as dependency to the project

    implementation(project(":module1"))
    implementation(project(":module2"))

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.1")

    testImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
