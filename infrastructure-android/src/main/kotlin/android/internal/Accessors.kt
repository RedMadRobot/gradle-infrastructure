@file:Suppress("UnstableApiUsage") // We want to use new APIs

package com.redmadrobot.build.android.internal

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.redmadrobot.build.android.AndroidOptions
import com.redmadrobot.build.kotlin.TestOptions
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.getByName

internal fun <T : CommonExtension<*, *, *, *>> Project.android(configure: T.() -> Unit) {
    extensions.configure("android", configure)
}

@JvmName("androidFinalizeDslCommon")
internal fun Project.androidFinalizeDsl(configure: CommonExtension<*, *, *, *>.() -> Unit) {
    androidFinalizeDsl<CommonExtension<*, *, *, *>>(configure)
}

internal fun <T : CommonExtension<*, *, *, *>> Project.androidFinalizeDsl(
    configure: T.() -> Unit,
) {
    extensions.getByName<AndroidComponentsExtension<T, *, *>>("androidComponents")
        .finalizeDsl(configure)
}

internal val AndroidOptions.test: TestOptions
    get() = (this as ExtensionAware).extensions.getByName<TestOptions>("test")
