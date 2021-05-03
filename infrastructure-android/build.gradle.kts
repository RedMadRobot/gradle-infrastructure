plugins {
    `kotlin-dsl`
}

description = "Small plugins to reduce boilerplate in Android projects' Gradle build scripts."

gradlePlugin {
    plugins {
        register("application") {
            id = "redmadrobot.application"
            implementationClass = "com.redmadrobot.build.AndroidApplicationPlugin"
        }
        register("android-library") {
            id = "redmadrobot.android-library"
            implementationClass = "com.redmadrobot.build.AndroidLibraryPlugin"
        }
        register("publish") {
            id = "redmadrobot.publish"
            implementationClass = "com.redmadrobot.build.AndroidPublishPlugin"
        }
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.1.3")
    api(project(":infrastructure"))
}
