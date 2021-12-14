plugins {
    id("gradle-plugin-commons")
}

description = "Plugin to simplify common publication scenarios."

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
