// Members from package org.gradle.kotlin.dsl are imported by default,
// so we use hack to make field android available without import.
@file:Suppress("PackageDirectoryMismatch")

package org.gradle.kotlin.dsl

import com.redmadrobot.build.android.AndroidOptions
import com.redmadrobot.build.android.AndroidOptionsImpl
import com.redmadrobot.build.extension.RedmadrobotExtension

/**
 * Settings for android modules.
 * @see AndroidOptions
 */
public val RedmadrobotExtension.android: AndroidOptions by RedmadrobotExtension.extensionProperty<AndroidOptionsImpl>()

/**
 * Settings for android modules.
 * @see AndroidOptions
 */
public fun RedmadrobotExtension.android(configure: AndroidOptions.() -> Unit) {
    android.configure()
}
