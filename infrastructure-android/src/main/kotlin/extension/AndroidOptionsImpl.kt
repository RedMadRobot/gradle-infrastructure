package com.redmadrobot.build.extension

import com.redmadrobot.build.kotlin.TestOptions
import com.redmadrobot.build.kotlin.TestOptionsImpl
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.newInstance
import javax.inject.Inject

@Suppress("LeakingThis")
internal abstract class AndroidOptionsImpl @Inject constructor(objects: ObjectFactory) : AndroidOptions {

    override val test: TestOptions = objects.newInstance<TestOptionsImpl>()

    override fun test(configure: TestOptions.() -> Unit) {
        test.run(configure)
    }

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
    }
}
