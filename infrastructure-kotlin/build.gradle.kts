description = "Plugin with defaults for Kotlin projects."

gradlePlugin {
    plugins {
        register("kotlin-config") {
            id = "redmadrobot.kotlin-config"
            implementationClass = "com.redmadrobot.build.kotlin.KotlinConfigPlugin"
        }
        register("kotlin-library") {
            id = "redmadrobot.kotlin-library"
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
