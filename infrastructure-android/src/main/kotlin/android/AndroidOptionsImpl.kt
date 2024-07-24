package com.redmadrobot.build.android

import com.redmadrobot.build.WithDefaults
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.kotlin.TestOptions
import com.redmadrobot.build.kotlin.TestOptionsImpl
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.create
import javax.inject.Inject

@OptIn(InternalGradleInfrastructureApi::class)
@Suppress("LeakingThis")
internal abstract class AndroidOptionsImpl @Inject constructor(
    providers: ProviderFactory,
) : AndroidOptions, WithDefaults<AndroidOptionsImpl> {

    private val testOptions: TestOptionsImpl
    private var areTestDefaultsSet = false

    init {
        minSdk
            .finalizeValueOnRead()
        targetSdk
            .finalizeValueOnRead()
        compileSdk
            .convention(targetSdk.map(Int::toString))
            .finalizeValueOnRead()
        buildToolsVersion
            .convention(providers.environmentVariable("ANDROID_BUILD_TOOLS_VERSION"))
            .finalizeValueOnRead()
        ndkVersion
            .convention(providers.environmentVariable("ANDROID_NDK_VERSION"))
            .finalizeValueOnRead()
        testTasksFilter
            .convention { taskProvider -> taskProvider.name.endsWith("ReleaseUnitTest") }
            .finalizeValueOnRead()

        testOptions = (this as ExtensionAware).extensions
            .create(
                publicType = TestOptions::class,
                name = "test",
                instanceType = TestOptionsImpl::class,
            ) as TestOptionsImpl
    }

    override fun setDefaults(defaults: AndroidOptionsImpl) {
        minSdk.convention(defaults.minSdk)
        targetSdk.convention(defaults.targetSdk)
        compileSdk.convention(defaults.compileSdk)
        buildToolsVersion.convention(defaults.buildToolsVersion)
        ndkVersion.convention(defaults.ndkVersion)
        testTasksFilter.convention(defaults.testTasksFilter)
        setTestDefaults(defaults.testOptions)
    }

    internal fun setTestDefaults(defaults: TestOptionsImpl) {
        if (areTestDefaultsSet) return
        areTestDefaultsSet = true
        testOptions.setDefaults(defaults)
    }
}
