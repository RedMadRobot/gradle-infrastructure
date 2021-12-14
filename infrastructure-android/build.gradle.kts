plugins {
    id("gradle-plugin-commons")
}

description = "Small plugins to reduce boilerplate in Android projects' Gradle build scripts."

gradlePlugin {
    plugins {
        register("android-config") {
            id = "com.redmadrobot.android-config"
            implementationClass = "com.redmadrobot.build.android.AndroidConfigPlugin"
        }
        register("application") {
            id = "com.redmadrobot.application"
            implementationClass = "com.redmadrobot.build.android.AndroidApplicationPlugin"
        }
        register("android-library") {
            id = "com.redmadrobot.android-library"
            implementationClass = "com.redmadrobot.build.android.AndroidLibraryPlugin"
        }
        register("root-project-deprecated") {
            id = "redmadrobot.root-project"
            implementationClass = "com.redmadrobot.build.AndroidRootProjectPlugin"
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
    implementation(libs.androidGradle)
    implementation(libs.androidGradleCacheFix)
}

// TODO: Remove along with 'root-project' plugin publication
afterEvaluate {
    tasks.configureEach {
        if (name.contains("root-project-deprecated", ignoreCase = true)) {
            enabled = false
        }
    }
}
