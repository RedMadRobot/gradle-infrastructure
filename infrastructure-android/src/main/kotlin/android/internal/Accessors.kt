package com.redmadrobot.build.android.internal

import com.android.build.api.dsl.CommonExtension
import com.redmadrobot.build.android.AndroidOptions
import com.redmadrobot.build.kotlin.TestOptions
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.getByName

@Suppress("UnstableApiUsage")
internal fun <T : CommonExtension<*, *, *, *>> Project.android(configure: T.() -> Unit) {
    extensions.configure("android", configure)
}

internal val AndroidOptions.test: TestOptions
    get() = (this as ExtensionAware).extensions.getByName<TestOptions>("test")
