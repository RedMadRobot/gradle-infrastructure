package com.redmadrobot.build

import org.gradle.api.JavaVersion
import org.gradle.api.file.ProjectLayout

@Suppress("LeakingThis")
internal abstract class RedmadrobotExtensionImpl : RedmadrobotExtension {

    fun setDefaults(layout: ProjectLayout) {
        jvmTarget
            .convention(JavaVersion.VERSION_1_8)
            .finalizeValueOnRead()
        configsDir
            .convention(layout.projectDirectory.dir(StaticAnalyzerSpec.DEFAULT_CONFIGS_DIR))
            .finalizeValueOnRead()
        reportsDir
            .convention(layout.buildDirectory.dir(StaticAnalyzerSpec.DEFAULT_REPORTS_DIR))
            .finalizeValueOnRead()
    }
}
