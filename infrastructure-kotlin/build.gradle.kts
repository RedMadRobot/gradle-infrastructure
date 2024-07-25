plugins {
    id("gradle-plugin-commons")
}

description = "Defaults for Kotlin projects"

gradlePlugin {
    plugins {
        register("kotlin-config") {
            id = "com.redmadrobot.kotlin-config"
            displayName = "Infrastructure Kotlin Plugin config"
            description = "Configs for com.redmadrobot.kotlin-library"
            implementationClass = "com.redmadrobot.build.kotlin.KotlinConfigPlugin"
            tags("kotlin")
        }
        register("kotlin-library") {
            id = "com.redmadrobot.kotlin-library"
            displayName = "Infrastructure Kotlin Plugin"
            description = project.description
            implementationClass = "com.redmadrobot.build.kotlin.KotlinLibraryPlugin"
            tags("kotlin", "library")
        }
    }
}

dependencies {
    api(projects.infrastructureCommon)
    compileOnlyApi(libs.kotlinGradle) // Should be provided by a project
}
