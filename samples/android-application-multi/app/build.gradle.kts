plugins {
    id("com.redmadrobot.application")
    convention.jvm
}

// Plugin "com.redmadrobot.application" configures build types, SDK versions, proguard and so on.
// We only should configure applicationId and version name and code.
android {
    namespace = "com.redmadrobot.samples"

    defaultConfig {
        applicationId = "com.redmadrobot.samples"
        versionCode = 1
        versionName = "1.0"
        // If we need any additional configurations we still can add it.
        // For example, specify instrumentation tests runner.
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    // Align Kotlin version across all dependencies
    implementation(platform(kotlin("bom", version = "2.0.0")))

    // Kotlin components can be added without version specifying
    implementation(kotlin("stdlib"))

    implementation(project(":module1"))
    implementation(project(":module2"))

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    testImplementation(kotlin("test-junit5"))
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}
