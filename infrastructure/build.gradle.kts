plugins {
    `kotlin-dsl`
}

description = "Small plugins to reduce boilerplate in Gradle build scripts."

gradlePlugin {
    plugins {
        register("root-project") {
            id = "redmadrobot.root-project"
            implementationClass = "com.redmadrobot.build.RootProjectPlugin"
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
    mavenCentral()
    google()
}

dependencies {
    api(kotlin("gradle-plugin", version = "1.4.32"))
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.16.0")
    implementation("com.android.tools.build:gradle:4.1.3")
}
