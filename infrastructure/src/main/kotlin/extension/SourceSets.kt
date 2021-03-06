package com.redmadrobot.build.extension

import com.android.SdkConstants.*
import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.gradle.BaseExtension
import com.redmadrobot.build.BUILD_TYPE_DEBUG
import com.redmadrobot.build.BUILD_TYPE_RELEASE
import com.redmadrobot.build.BUILD_TYPE_STAGING
import org.gradle.api.Incubating
import org.gradle.kotlin.dsl.get

/**
 * Adds sources from one build type to another build type with syntax `<from> to <to>`.
 * You can use constants [BUILD_TYPE_DEBUG], [BUILD_TYPE_RELEASE] and [BUILD_TYPE_STAGING]
 * for predefined build types.
 *
 * ```
 *  android {
 *      // Here we need to use debug sources in staging builds
 *      mergeSourceSets(BUILD_TYPE_DEBUG to BUILD_TYPE_STAGING)
 *  }
 * ```
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "With mergeSourceSets you're not able to add resources with the same name to both of build types.",
    replaceWith = ReplaceWith("addSharedSourceSetRoot(mapping.first, mapping.second)")
)
public fun BaseExtension.mergeSourceSets(mapping: Pair<String, String>) {
    mergeSourceSets(mapping.first, mapping.second)
}

/**
 * Adds sources from build type with name [from] to build type with name [to].
 * You can use constants [BUILD_TYPE_DEBUG], [BUILD_TYPE_RELEASE] and [BUILD_TYPE_STAGING]
 * for predefined build types.
 * ```
 *  android {
 *      // Here we need to use debug sources in staging builds
 *      mergeSourceSets(from = BUILD_TYPE_DEBUG, to = BUILD_TYPE_STAGING)
 *  }
 * ```
 */
@Deprecated(
    message = "With mergeSourceSets you're not able to add resources with the same name to both of build types.",
    replaceWith = ReplaceWith("addSharedSourceSetRoot(from, to)")
)
public fun BaseExtension.mergeSourceSets(from: String, to: String) {
    val fromSourceSet = sourceSets[from].java
    val toSourceSet = sourceSets[to].java
    toSourceSet.setSrcDirs(fromSourceSet.srcDirs + toSourceSet.srcDirs)
}

/**
 * Adds source set root with the given [name], shared between [variant1] and [variant2] source sets.
 * By default [name] is a combination of `variant1` and `variant2` names.
 * ```
 *  android {
 *      // Here we need to share sources between debug and staging builds
 *      addSharedSourceSetRoot(BUILD_TYPE_DEBUG, BUILD_TYPE_STAGING)
 *
 *      // We can specify name for the source set root if need
 *      addSharedSourceSetRoot(BUILD_TYPE_DEBUG, BUILD_TYPE_STAGING, name = "debugPanel")
 *  }
 * ```
 */
@Incubating
public fun BaseExtension.addSharedSourceSetRoot(
    variant1: String,
    variant2: String,
    name: String = "$variant1${variant2.capitalize()}"
) {
    val variant1SourceSets = sourceSets[variant1]
    val variant2SourceSets = sourceSets[variant2]
    val root = "src/$name"

    variant1SourceSets.addRoot(root)
    variant2SourceSets.addRoot(root)
}

@Suppress("UnstableApiUsage")
private fun AndroidSourceSet.addRoot(path: String) {
    java.srcDirs("$path/$FD_JAVA", "$path/kotlin")
    resources.srcDir("$path/$FD_JAVA_RES")
    res.srcDir("$path/$FD_RES")
    assets.srcDir("$path/$FD_ASSETS")
    manifest.srcFile("$path/$FN_ANDROID_MANIFEST_XML")
    aidl.srcDir("$path/$FD_AIDL")
    renderscript.srcDir("$path/$FD_RENDERSCRIPT")
    jni.srcDir("$path/$FD_JNI")
    jniLibs.srcDir("$path/jniLibs")
    shaders.srcDir("$path/shaders")
    mlModels.srcDir("$path/$FD_ML_MODELS")
}
