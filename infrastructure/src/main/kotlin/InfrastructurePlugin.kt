package com.redmadrobot.build

import com.redmadrobot.build.extension.RedmadrobotExtension
import com.redmadrobot.build.internal.findInfrastructureRootProject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * Base plugin class.
 * Makes it possible to strict extension-functions scope to infrastructure plugins only.
 */
public abstract class InfrastructurePlugin : Plugin<Project> {

    public lateinit var project: Project
        private set

    protected lateinit var infrastructureRootProject: Project
        private set

    protected val redmadrobotExtension: RedmadrobotExtension
        get() = infrastructureRootProject.extensions.getByType()

    /** @see configure */
    final override fun apply(target: Project) {
        infrastructureRootProject = target.requireInfrastructureRootProject()
        project = target
        target.configure()
    }

    protected abstract fun Project.configure()
}

private fun Project.requireInfrastructureRootProject(): Project {
    return checkNotNull(findInfrastructureRootProject()) {
        """
        Plugin 'redmadrobot.root-project' is not applied to any of projects in hierarchy.
        You should apply it to the project you want to define as "root":

        plugins {
            id("redmadrobot.root-project") version "[LATEST_VERSION_HERE]"
        }
        """.trimIndent()
    }
}
