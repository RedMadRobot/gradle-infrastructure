package com.redmadrobot.build

import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.internal.findByName
import com.redmadrobot.build.internal.parents
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import kotlin.reflect.KClass

/**
 * Base plugin class.
 * Makes it possible to strict extension-functions scope to infrastructure plugins only.
 */
public abstract class InfrastructurePlugin : Plugin<Project> {

    @InternalGradleInfrastructureApi
    public lateinit var project: Project
        private set

    protected lateinit var redmadrobotExtension: RedmadrobotExtension
        private set

    @PublishedApi
    @InternalGradleInfrastructureApi
    internal val redmadrobotExtensions: Sequence<RedmadrobotExtensionImpl>
        get() = project.parents.mapNotNull { it.extensions.redmadrobot }

    /** @see configure */
    @OptIn(InternalGradleInfrastructureApi::class)
    final override fun apply(target: Project) {
        project = target
        redmadrobotExtension = project.extensions.obtainRedmadrobotExtension()
        target.configure()
    }

    @InternalGradleInfrastructureApi
    protected abstract fun Project.configure()

    /** Creates an extension for the plugin in namespace 'redmadrobot' and returns it. */
    @InternalGradleInfrastructureApi
    protected inline fun <reified T : WithDefaults<T>> createExtension(
        name: String,
        publicType: KClass<in T>? = null,
    ): T {
        val defaults = getDefaultExtensions(name, type = T::class)
        return (redmadrobotExtension as ExtensionAware).extensions.createWithDefaults(name, defaults, publicType)
    }

    @InternalGradleInfrastructureApi
    protected fun <T : WithDefaults<T>> getDefaultExtensions(name: String, type: KClass<T>): T? {
        return redmadrobotExtensions
            .mapNotNull { it.extensions.findByName(name, type) }
            .firstOrNull()
    }

    @InternalGradleInfrastructureApi
    private fun ExtensionContainer.obtainRedmadrobotExtension(): RedmadrobotExtension {
        return redmadrobot
            ?: createWithDefaults(RedmadrobotExtension.NAME, redmadrobotExtensions.firstOrNull())
    }
}
