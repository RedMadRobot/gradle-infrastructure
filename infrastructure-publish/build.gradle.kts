description = "Plugin to make publication as simple as possible."

gradlePlugin {
    plugins {
        register("publish-config") {
            id = "com.redmadrobot.publish-config"
            implementationClass = "com.redmadrobot.build.publish.PublishConfigPlugin"
        }
        register("publish") {
            id = "com.redmadrobot.publish"
            implementationClass = "com.redmadrobot.build.publish.PublishPlugin"
        }
    }
}

repositories {
    google()
}

dependencies {
    api(projects.infrastructureCommon)

    compileOnly(libs.androidGradle)
}
