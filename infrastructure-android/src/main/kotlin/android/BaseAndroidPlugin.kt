@file:Suppress("UnstableApiUsage") // We want to use new APIs

package com.redmadrobot.build.android

import com.redmadrobot.build.android.internal.*
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.internal.addRepositoriesIfNeed
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply

/**
 * Base plugin with common configurations for both application and library modules.
 * @see AndroidLibraryPlugin
 * @see AndroidApplicationPlugin
 */
public abstract class BaseAndroidPlugin internal constructor(
    private val androidPluginId: String,
) : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val configPlugin = plugins.apply(AndroidConfigPlugin::class)
            applyBaseAndroidPlugin(androidPluginId, configPlugin)
            configure(configPlugin)
        }
    }

    internal abstract fun Project.configure(configPlugin: AndroidConfigPlugin)

    private fun Project.applyBaseAndroidPlugin(pluginId: String, configPlugin: AndroidConfigPlugin) {
        apply { plugin(pluginId) }

        // AGP 9+ auto-applies KGP when it's present in the classpath, registering the 'kotlin'
        // extension. Apply kotlin-android only if it hasn't been applied yet to avoid the conflict.
        if (extensions.findByName("kotlin") == null) {
            apply { plugin("kotlin-android") }
        }

        configureRepositories()
    }
}

/** Universal function to set compile SDK even if it is a preview version. */
internal fun CommonExtension.setCompileSdkVersion(version: String) {
    val intVersion = version.toIntOrNull()
    if (intVersion != null) {
        compileSdk = intVersion
    } else {
        compileSdkPreview = version
    }
}

/** Apply common Android options that are available on [CommonExtension] in AGP 9+. */
@OptIn(InternalGradleInfrastructureApi::class)
internal fun CommonExtension.applyCommonAndroidOptions(options: AndroidOptions) {
    options.compileSdk.ifPresent { setCompileSdkVersion(it) }
    options.buildToolsVersion.ifPresent { buildToolsVersion = it }
    options.ndkVersion.ifPresent { ndkVersion = it }
    options.minSdk.ifPresent { defaultConfig.minSdk = it }
}

/** Filter unit tests to be run with the 'test' task. */
internal fun Project.filterTestTaskDependencies(options: AndroidOptions) {
    afterEvaluate {
        val testTask = tasks.findByName("test") ?: return@afterEvaluate
        val testTasksFilter = options.testTasksFilter.get()
        testTask.setDependsOn(testTask.dependsOn.filter { it !is TaskProvider<*> || testTasksFilter(it) })
    }
}

@OptIn(InternalGradleInfrastructureApi::class)
private fun Project.configureRepositories() {
    addRepositoriesIfNeed {
        mavenCentral()
        google()
    }
}
