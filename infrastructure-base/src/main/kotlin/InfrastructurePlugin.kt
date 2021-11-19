package com.redmadrobot.build

import com.redmadrobot.build.internal.findByName
import com.redmadrobot.build.internal.parents
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer

/**
 * Base plugin class.
 * Makes it possible to strict extension-functions scope to infrastructure plugins only.
 */
public abstract class InfrastructurePlugin : Plugin<Project> {

    public lateinit var project: Project
        private set

    protected lateinit var redmadrobotExtension: RedmadrobotExtension
        private set

    @PublishedApi
    internal val redmadrobotExtensions: Sequence<RedmadrobotExtensionImpl>
        get() = project.parents.mapNotNull { it.extensions.redmadrobot }

    /** @see configure */
    final override fun apply(target: Project) {
        project = target
        redmadrobotExtension = project.extensions.obtainRedmadrobotExtension()
        target.configure()
    }

    protected abstract fun Project.configure()

    /** Creates an extension for the plugin in namespace 'redmadrobot' and returns it. */
    protected inline fun <reified T : WithDefaults<T>> createExtension(name: String): T {
        val defaults = redmadrobotExtensions
            .mapNotNull { it.extensions.findByName<T>(name) }
            .firstOrNull()
        return redmadrobotExtension.extensions.createWithDefaults(name, defaults)
    }

    private fun ExtensionContainer.obtainRedmadrobotExtension(): RedmadrobotExtension {
        return redmadrobot
            ?: createWithDefaults(RedmadrobotExtension.NAME, redmadrobotExtensions.firstOrNull())
    }
}
