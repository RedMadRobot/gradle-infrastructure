package com.redmadrobot.build.extension

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.newInstance
import javax.inject.Inject
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

public abstract class AndroidOptions @Inject constructor(objects: ObjectFactory) {

    /** Minimal Android SDK that will be applied to all android modules. */
    public abstract val minSdk: Property<Int>

    /** Target Android SDK that will be applied to all android modules. Also determines compile SDK. */
    public abstract val targetSdk: Property<Int>

    /** Settings for Android test task. */
    public val test: TestOptions = objects.newInstance()

    /** Settings for Android test task. */
    public fun test(configure: TestOptions.() -> Unit) {
        test.run(configure)
    }

    init {
        minSdk
            .convention(DEFAULT_MIN_API)
            .finalizeValueOnRead()
        targetSdk
            .convention(DEFAULT_TARGET_API)
            .finalizeValueOnRead()
    }

    private companion object {
        const val DEFAULT_MIN_API = 21
        const val DEFAULT_TARGET_API = 30
    }
}
