description = "Small plugins to reduce boilerplate in Android projects' Gradle build scripts."

gradlePlugin {
    plugins {
        isAutomatedPublishing = false
        register("application") {
            id = "redmadrobot.application"
            implementationClass = "com.redmadrobot.build.AndroidApplicationPlugin"
        }
        register("android-library") {
            id = "redmadrobot.android-library"
            implementationClass = "com.redmadrobot.build.AndroidLibraryPlugin"
        }
    }
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    api(project(":infrastructure"))
    implementation("com.android.tools.build:gradle:4.2.2")
    implementation("gradle.plugin.org.gradle.android:android-cache-fix-gradle-plugin:2.4.3")
}
