description = "Plugin to make publication as simple as possible."

gradlePlugin {
    plugins {
        register("publish") {
            id = "redmadrobot.publish"
            implementationClass = "com.redmadrobot.build.publish.PublishPlugin"
        }
    }
}

repositories {
    google()
}

dependencies {
    api(projects.infrastructureBase)

    compileOnly(libs.androidGradle)
}
