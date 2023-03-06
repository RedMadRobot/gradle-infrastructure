plugins {
    id("gradle-plugin-commons")
}

description = "Plugin to simplify common publication scenarios."

gradlePlugin {
    plugins {
        register("publish-config") {
            id = "com.redmadrobot.publish-config"
            displayName = "Infrastructure Publish Plugin config"
            description = "Configs for com.redmadrobot.publish"
            implementationClass = "com.redmadrobot.build.publish.PublishConfigPlugin"
            tags("publish")
        }
        register("publish") {
            id = "com.redmadrobot.publish"
            displayName = "Infrastructure Publish Plugin"
            description = project.description
            implementationClass = "com.redmadrobot.build.publish.PublishPlugin"
            tags("publish")
        }
    }
}

repositories {
    google()
}

dependencies {
    api(projects.infrastructureCommon)

    compileOnly(libs.androidTools.gradle)
}
