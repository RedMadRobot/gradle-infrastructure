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
    public var minSdk: Int = 21

    /** Target Android SDK that will be applied to all android modules. Also determines compile SDK. */
    public var targetSdk: Int = 30

    /** Settings for Android test task. */
    public val test: TestOptions = objects.newInstance()

    /** Settings for Android test task. */
    public fun test(configure: TestOptions.() -> Unit) {
        test.run(configure)
    }
}
