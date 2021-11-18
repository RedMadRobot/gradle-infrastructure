gradlePlugin {
    plugins {
        isAutomatedPublishing = false
        register("root-project") {
            id = "redmadrobot.root-project"
            implementationClass = "com.redmadrobot.build.RootProjectPlugin"
        }
    }
}
