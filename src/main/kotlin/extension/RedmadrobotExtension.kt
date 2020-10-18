package com.redmadrobot.build.extension

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.getByType

open class RedmadrobotExtension(objects: ObjectFactory) {

    companion object {
        const val NAME = "redmadrobot"

        // Relative to root project directory.
        internal const val DEFAULT_CONFIGS_DIR = "configs/"

        // Relative to root project build directory.
        internal const val DEFAULT_REPORTS_DIR = "reports/"
    }

    /** Kotlin version that should be used for all projects. */
    var kotlinVersion: String = "1.4.10"

    /** Directory where stored configs for static analyzers. */
    val configsDir: DirectoryProperty = objects.directoryProperty()

    /** Directory where will be stored static analyzers reports. */
    val reportsDir: DirectoryProperty = objects.directoryProperty()

    /** Settings for android modules. */
    val android: AndroidSettings = AndroidSettings()

    fun android(configure: AndroidSettings.() -> Unit) {
        android.run(configure)
    }
}

class AndroidSettings {

    /** Minimal Android SDK that will be applied to all android modules. */
    var minSdk: Int = 21

    /** Target Android SDK that will be applied to all android modules. */
    var targetSdk: Int = 30
}

internal val Project.redmadrobot: RedmadrobotExtension
    get() = rootProject.extensions.getByType()
