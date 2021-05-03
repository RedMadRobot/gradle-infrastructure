package com.redmadrobot.build

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Base plugin class.
 * Makes it possible to strict extension-functions scope to infrastructure plugins only.
 */
public abstract class InfrastructurePlugin : Plugin<Project> {

    public lateinit var project: Project

    /** @see configure */
    final override fun apply(target: Project) {
        target.requireRootProjectPlugin()
        project = target
        target.configure()
    }

    protected abstract fun Project.configure()
}

private fun Project.requireRootProjectPlugin() {
    check(rootProject.plugins.hasPlugin("redmadrobot.root-project")) {
        """
        Plugin 'redmadrobot.root-project' not found. You should apply it to your root project:

        plugins {
            id("redmadrobot.root-project") version "[LATEST_VERSION_HERE]"
        }
        """.trimIndent()
    }
}