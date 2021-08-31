import com.redmadrobot.build.dsl.BUILD_TYPE_DEBUG
import com.redmadrobot.build.dsl.BUILD_TYPE_QA
import com.redmadrobot.build.dsl.addSharedSourceSetRoot

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

    // If we need to share sources between two build types,
    // we can add shared source set root.
    // In this case will be created directory "debugQa"
    // that will be included to debug and QA source sets.
    addSharedSourceSetRoot(BUILD_TYPE_DEBUG, BUILD_TYPE_QA)
}

dependencies {
    // Align Kotlin version across all dependencies
    implementation(platform(kotlin("bom", version = "1.5.30")))

    // Kotlin components can be added without version specifying
    implementation(kotlin("stdlib-jdk8"))

    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

    testImplementation(kotlin("test-junit5"))
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}
