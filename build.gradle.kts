plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.redmadrobot.build"
description = "Small plugins to reduce boilerplate in Gradle build scripts."
version = "0.4"

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

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
    jcenter()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.1.1")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.14.2")
    implementation(kotlin("gradle-plugin", version = "1.4.20"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

val publishToBintray = "bintrayUsername" in properties && "bintrayPassword" in properties
val isSnapshot = version.toString().endsWith("-SNAPSHOT")
publishing {
    repositories {
        maven {
            name = "githubPackages"
            setUrl("https://maven.pkg.github.com/RedMadRobot/gradle-infrastructure")
            credentials(PasswordCredentials::class)
        }
        if (publishToBintray && !isSnapshot) {
            maven {
                name = "bintray"
                setUrl("https://api.bintray.com/maven/redmadrobot-opensource/android/infrastructure/")
                credentials(PasswordCredentials::class)
            }
        }
    }
}
