package com.redmadrobot.build.android

import com.redmadrobot.build.WithDefaults
import com.redmadrobot.build.kotlin.TestOptionsImpl
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.create

@Suppress("LeakingThis")
internal abstract class AndroidOptionsImpl : AndroidOptions, WithDefaults<AndroidOptionsImpl> {

    private val testOptions: TestOptionsImpl
    private var areTestDefaultsSet = false

    init {
        minSdk
            .convention(AndroidOptions.DEFAULT_MIN_API)
            .finalizeValueOnRead()
        targetSdk
            .convention(AndroidOptions.DEFAULT_TARGET_API)
            .finalizeValueOnRead()
        compileSdk
            .convention(targetSdk.map { "android-$it" })
            .finalizeValueOnRead()
        buildToolsVersion
            .finalizeValueOnRead()
        testTasksFilter
            .convention { taskProvider -> taskProvider.name.endsWith("ReleaseUnitTest") }
            .finalizeValueOnRead()

        testOptions = (this as ExtensionAware).extensions.create("test")
    }

    override fun setDefaults(defaults: AndroidOptionsImpl) {
        minSdk.convention(defaults.minSdk)
        targetSdk.convention(defaults.targetSdk)
        compileSdk.convention(defaults.compileSdk)
        buildToolsVersion.convention(defaults.buildToolsVersion)
        testTasksFilter.convention(defaults.testTasksFilter)
        setTestDefaults(defaults.testOptions)
    }

    internal fun setTestDefaults(defaults: TestOptionsImpl) {
        if (areTestDefaultsSet) return
        areTestDefaultsSet = true
        testOptions.setDefaults(defaults)
    }
}
