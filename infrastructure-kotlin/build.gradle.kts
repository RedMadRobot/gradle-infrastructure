description = "Plugin with defaults for Kotlin projects."

gradlePlugin {
    plugins {
        register("kotlin-config") {
            id = "com.redmadrobot.kotlin-config"
            implementationClass = "com.redmadrobot.build.kotlin.KotlinConfigPlugin"
        }
        register("kotlin-library") {
            id = "com.redmadrobot.kotlin-library"
            implementationClass = "com.redmadrobot.build.kotlin.KotlinLibraryPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.infrastructureCommon)
    api(libs.kotlinGradle)
}
