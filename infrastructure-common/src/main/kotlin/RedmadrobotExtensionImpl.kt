package com.redmadrobot.build

import com.redmadrobot.build.internal.findByName
import org.gradle.api.file.ProjectLayout
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import javax.inject.Inject

@Suppress("LeakingThis")
internal abstract class RedmadrobotExtensionImpl @Inject constructor(
    layout: ProjectLayout,
) : RedmadrobotExtension, ExtensionAware, WithDefaults<RedmadrobotExtensionImpl> {

    init {
        configsDir
            .convention(layout.projectDirectory.dir(StaticAnalyzerSpec.DEFAULT_CONFIGS_DIR))
            .finalizeValueOnRead()
        reportsDir
            .convention(layout.buildDirectory.dir(StaticAnalyzerSpec.DEFAULT_REPORTS_DIR))
            .finalizeValueOnRead()
    }

    override fun setDefaults(defaults: RedmadrobotExtensionImpl) {
        configsDir.convention(defaults.configsDir)
        reportsDir.convention(defaults.reportsDir)
    }
}

@PublishedApi
internal val ExtensionContainer.redmadrobot: RedmadrobotExtensionImpl?
    get() = findByName<RedmadrobotExtensionImpl>(RedmadrobotExtension.NAME)
