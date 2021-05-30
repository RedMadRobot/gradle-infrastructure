package com.redmadrobot.build.extension

import com.android.build.gradle.BaseExtension
import com.redmadrobot.build.dsl.BUILD_TYPE_DEBUG
import com.redmadrobot.build.dsl.BUILD_TYPE_QA
import com.redmadrobot.build.dsl.BUILD_TYPE_RELEASE
import com.redmadrobot.build.dsl.addSharedSourceSetRoot
import org.gradle.kotlin.dsl.get

/**
 * Adds sources from one build type to another build type with syntax `<from> to <to>`.
 * You can use constants [BUILD_TYPE_DEBUG], [BUILD_TYPE_RELEASE] and [BUILD_TYPE_QA]
 * for predefined build types.
 *
 * ```
 *  android {
 *      // Here we need to use debug sources in QA builds
 *      mergeSourceSets(BUILD_TYPE_DEBUG to BUILD_TYPE_QA)
 *  }
 * ```
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "With mergeSourceSets you're not able to add resources with the same name to both of build types.",
    replaceWith = ReplaceWith(
        "addSharedSourceSetRoot(mapping.first, mapping.second)",
        "com.redmadrobot.build.dsl.addSharedSourceSetRoot",
    ),
)
public fun BaseExtension.mergeSourceSets(mapping: Pair<String, String>) {
    mergeSourceSets(mapping.first, mapping.second)
}

/**
 * Adds sources from build type with name [from] to build type with name [to].
 * You can use constants [BUILD_TYPE_DEBUG], [BUILD_TYPE_RELEASE] and [BUILD_TYPE_QA]
 * for predefined build types.
 * ```
 *  android {
 *      // Here we need to use debug sources in QA builds
 *      mergeSourceSets(from = BUILD_TYPE_DEBUG, to = BUILD_TYPE_QA)
 *  }
 * ```
 */
@Deprecated(
    message = "With mergeSourceSets you're not able to add resources with the same name to both of build types.",
    replaceWith = ReplaceWith(
        "addSharedSourceSetRoot(from, to)",
        "com.redmadrobot.build.dsl.addSharedSourceSetRoot",
    ),
)
public fun BaseExtension.mergeSourceSets(from: String, to: String) {
    val fromSourceSet = sourceSets[from].java
    val toSourceSet = sourceSets[to].java
    toSourceSet.setSrcDirs(fromSourceSet.srcDirs + toSourceSet.srcDirs)
}

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith(
        "addSharedSourceSetRoot(variant1, variant2, name)",
        "com.redmadrobot.build.dsl.addSharedSourceSetRoot",
    ),
)
public fun BaseExtension.addSharedSourceSetRoot(
    variant1: String,
    variant2: String,
    name: String = "$variant1${variant2.capitalize()}",
) {
    addSharedSourceSetRoot(variant1, variant2, name)
}
