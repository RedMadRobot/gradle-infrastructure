package com.redmadrobot.build

import com.redmadrobot.build.internal.findByName
import org.gradle.api.JavaVersion
import org.gradle.api.file.ProjectLayout
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import javax.inject.Inject

@Suppress("LeakingThis")
internal abstract class RedmadrobotExtensionImpl @Inject constructor(
    layout: ProjectLayout,
) : RedmadrobotExtension, ExtensionAware, WithDefaults<RedmadrobotExtensionImpl> {

    init {
        jvmTarget
            .convention(JavaVersion.VERSION_11)
            .finalizeValueOnRead()
        configsDir
            .convention(layout.projectDirectory.dir(StaticAnalyzerSpec.DEFAULT_CONFIGS_DIR))
            .finalizeValueOnRead()
        reportsDir
            .convention(layout.buildDirectory.dir(StaticAnalyzerSpec.DEFAULT_REPORTS_DIR))
            .finalizeValueOnRead()
    }

    override fun setDefaults(defaults: RedmadrobotExtensionImpl) {
        jvmTarget.convention(defaults.jvmTarget)
        configsDir.convention(defaults.configsDir)
        reportsDir.convention(defaults.reportsDir)
    }
}

@PublishedApi
internal val ExtensionContainer.redmadrobot: RedmadrobotExtensionImpl?
    get() = findByName<RedmadrobotExtensionImpl>(RedmadrobotExtension.NAME)
