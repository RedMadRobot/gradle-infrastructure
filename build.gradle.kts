plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.redmadrobot.build"
version = "0.2-SNAPSHOT"

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
    implementation("com.android.tools.build:gradle:4.1.0")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.14.1")
    implementation(kotlin("gradle-plugin", version = "1.4.10"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            name = "rmrNexus"
            setUrl("https://nexus.redmadrobot.com/repository/android/")
            credentials(PasswordCredentials::class)
        }
    }
}
