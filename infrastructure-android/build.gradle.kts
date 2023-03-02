plugins {
    id("gradle-plugin-commons")
}

description = "Small plugins to reduce boilerplate in Android projects' Gradle build scripts."

redmadrobot {
    jvmTarget.set(JavaVersion.VERSION_11)
}

gradlePlugin {
    plugins {
        register("android-config") {
            id = "com.redmadrobot.android-config"
            displayName = "red_mad_robot Android config"
            description = "Configs for com.redmadrobot.application and com.redmadrobot.android-library"
            implementationClass = "com.redmadrobot.build.android.AndroidConfigPlugin"
        }
        register("application") {
            id = "com.redmadrobot.application"
            displayName = "red_mad_robot Android Application Plugin"
            description = "Plugin to reduce boilerplate in Android applications build scripts"
            implementationClass = "com.redmadrobot.build.android.AndroidApplicationPlugin"
        }
        register("android-library") {
            id = "com.redmadrobot.android-library"
            displayName = "red_mad_robot Android Library Plugin"
            description = "Plugin to reduce boilerplate in Android libraries build scripts"
            implementationClass = "com.redmadrobot.build.android.AndroidLibraryPlugin"
        }
    }
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    api(projects.infrastructureKotlin)

    compileOnly(libs.androidTools.gradle) // Should be provided by project
    compileOnly(libs.androidTools.common) // Should be provided via AGP

    implementation(libs.androidGradleCacheFix)
}
