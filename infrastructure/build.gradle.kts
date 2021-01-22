import com.redmadrobot.build.extension.*

plugins {
    `kotlin-dsl`
    id("redmadrobot.publish")
    id("redmadrobot.kotlin-library")
}

group = "com.redmadrobot.build"
description = "Small plugins to reduce boilerplate in Gradle build scripts."
version = "0.7-SNAPSHOT"

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

gradlePlugin {
    plugins {
        register("root-project") {
            id = "redmadrobot.root-project"
            implementationClass = "com.redmadrobot.build.RootProjectPlugin"
        }
        register("application") {
            id = "redmadrobot.application"
            implementationClass = "com.redmadrobot.build.AndroidApplicationPlugin"
        }
        register("android-library") {
            id = "redmadrobot.android-library"
            implementationClass = "com.redmadrobot.build.AndroidLibraryPlugin"
        }
        register("kotlin-library") {
            id = "redmadrobot.kotlin-library"
            implementationClass = "com.redmadrobot.build.KotlinLibraryPlugin"
        }
        register("publish") {
            id = "redmadrobot.publish"
            implementationClass = "com.redmadrobot.build.PublishPlugin"
        }
        register("detekt") {
            id = "redmadrobot.detekt"
            implementationClass = "com.redmadrobot.build.DetektPlugin"
        }
    }
}

repositories {
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.1.1")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.15.0")
    implementation(kotlin("gradle-plugin", version = "1.4.30"))
}

publishing {
    repositories {
        if (isRunningOnCi) githubPackages("RedMadRobot/gradle-infrastructure")
        if (!isSnapshotVersion && credentialsExist("bintray")) rmrBintray("infrastructure")
    }
}
