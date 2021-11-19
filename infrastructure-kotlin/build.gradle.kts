description = "Plugin with defaults for Kotlin projects."

gradlePlugin {
    plugins {
        isAutomatedPublishing = false
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
    api(projects.infrastructureBase)
    api(kotlin("gradle-plugin",  version = libs.versions.kotlin.get()))
}
