package com.redmadrobot.build

import com.redmadrobot.build.extension.RedmadrobotExtension
import com.redmadrobot.build.extension.RedmadrobotExtensionImpl
import com.redmadrobot.build.internal.findByName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.create

/**
 * Base plugin class.
 * Makes it possible to strict extension-functions scope to infrastructure plugins only.
 */
public abstract class InfrastructurePlugin : Plugin<Project> {

    public lateinit var project: Project
        private set

    protected lateinit var redmadrobotExtension: RedmadrobotExtension
        private set

    /** @see configure */
    final override fun apply(target: Project) {
        project = target
        redmadrobotExtension = project.extensions.obtainRedmadrobotExtension()
        target.configure()
    }

    protected abstract fun Project.configure()

    /** Shorthand to create an extension for the plugin in namespace 'redmadrobot'. */
    protected inline fun <reified T : Any> createExtension(name: String): T {
        return redmadrobotExtension.extensions.create(name)
    }

    private fun ExtensionContainer.obtainRedmadrobotExtension(): RedmadrobotExtension {
        return findByName<RedmadrobotExtension>(RedmadrobotExtension.NAME)
            ?: create<RedmadrobotExtensionImpl>(RedmadrobotExtension.NAME)
                .apply { setDefaults(project.layout) }
    }
}
