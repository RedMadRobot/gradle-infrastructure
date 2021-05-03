package com.redmadrobot.build.extension

/** Settings for android modules. */
public val RedmadrobotExtension.android: AndroidOptions by RedmadrobotExtension.field { AndroidOptions() }

/** Settings for android modules. */
public fun RedmadrobotExtension.android(configure: AndroidOptions.() -> Unit) {
    android.configure()
}

public class AndroidOptions internal constructor() {

    /** Minimal Android SDK that will be applied to all android modules. */
    public var minSdk: Int = 21

    /** Target Android SDK that will be applied to all android modules. Also determines compile SDK. */
    public var targetSdk: Int = 30

    /** Settings for Android test task. */
    public val test: TestOptions = TestOptions()

    /** Settings for Android test task. */
    public fun test(configure: TestOptions.() -> Unit) {
        test.run(configure)
    }
}
