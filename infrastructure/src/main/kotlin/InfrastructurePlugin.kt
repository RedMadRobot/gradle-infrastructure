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
        project = target
        target.configure()
    }

    protected abstract fun Project.configure()
}
