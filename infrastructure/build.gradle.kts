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
}

dependencies {
    api(kotlin("gradle-plugin", version = "1.5.0"))
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.17.1")
    // TODO: Remove after Kotlin 1.5.10 release
    //  https://youtrack.jetbrains.com/issue/KT-46368
    implementation("dev.zacsweers:kgp-150-leak-patcher:1.1.0")
}
