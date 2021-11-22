package com.redmadrobot.build

import org.gradle.api.Plugin
import org.gradle.api.Project

@Deprecated("Use plugin 'com.redmadrobot.android-config' instead.")
public class AndroidRootProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        error(
            """
            WARNING: Plugin 'redmadrobot.root-project' is deprecated and will be removed soon.
            Replace 'com.redmadrobot.build:infrastructure-android' with 'com.redmadrobot.build:infrastructure'
            to get further information about how to migrate to new plugins.
            Also, please read release notes about plugins' structure changes:
            https://github.com/RedMadRobot/gradle-infrastructure/releases/tag/v0.13
            """.trimIndent(),
        )
    }
}
