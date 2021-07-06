package com.redmadrobot.build.extension

import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.android as _android

/** Settings for android modules. */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("Property `android` can be used without import. Please remove import manually")
public val RedmadrobotExtension.android: AndroidOptions
    get() = _android

/** Settings for android modules. */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("Function `android { }` can be used without import. Please remove import manually")
public fun RedmadrobotExtension.android(configure: AndroidOptions.() -> Unit) {
    _android(configure)
}

public interface AndroidOptions : TestOptionsSpec {

    /** Minimal Android SDK to use across all android modules. */
    public val minSdk: Property<Int>

    /** Target Android SDK to use across all android modules. */
    public val targetSdk: Property<Int>

    /**
     * Compile Android SDK to use across all android modules.
     * Format: `android-XX` where `XX` is required API level.
     * Uses [targetSdk] as API level if not configured.
     */
    public val compileSdk: Property<String>

    /**
     * Build Tools version to use across all android modules.
     * Uses default version for current Android Gradle Plugin if not configured.
     */
    public val buildToolsVersion: Property<String>

    public companion object {
        internal const val DEFAULT_MIN_API = 21
        internal const val DEFAULT_TARGET_API = 30
    }
}
