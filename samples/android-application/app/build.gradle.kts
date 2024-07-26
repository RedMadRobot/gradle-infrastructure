import com.redmadrobot.build.dsl.*

plugins {
    id("com.redmadrobot.application")
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
        // For example we need to run instrumentation tests.
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // If we need to share sources between two build types,
    // we can add shared source set root.
    // In this case will be created directory "debugQa"
    // that will be included to debug and QA source sets.
    sourceSets.addSharedSourceSetRoot(BUILD_TYPE_DEBUG, BUILD_TYPE_QA)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Align Kotlin version across all dependencies
    implementation(platform(kotlin("bom", version = "2.0.0")))

    // Kotlin components can be added without version specifying
    implementation(kotlin("stdlib"))

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
