package com.redmadrobot.build.dsl

import com.android.SdkConstants.*
import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.gradle.BaseExtension
import org.gradle.api.Incubating
import org.gradle.kotlin.dsl.get

/**
 * Adds source set root with the given [name], shared between [variant1] and [variant2] source sets.
 * By default [name] is a combination of `variant1` and `variant2` names.
 * ```
 *  android {
 *      // Here we need to share sources between debug and QA builds
 *      addSharedSourceSetRoot(BUILD_TYPE_DEBUG, BUILD_TYPE_QA)
 *
 *      // We can specify name for the source set root if need
 *      addSharedSourceSetRoot(BUILD_TYPE_DEBUG, BUILD_TYPE_QA, name = "debugPanel")
 *  }
 * ```
 */
@Incubating
public fun BaseExtension.addSharedSourceSetRoot(
    variant1: String,
    variant2: String,
    name: String = "$variant1${variant2.capitalize()}",
) {
    val variant1SourceSets = sourceSets[variant1]
    val variant2SourceSets = sourceSets[variant2]
    val root = "src/$name"

    variant1SourceSets.addRoot(root)
    variant2SourceSets.addRoot(root)
}

@Suppress("UnstableApiUsage")
private fun AndroidSourceSet.addRoot(path: String) {
    java.srcDirs("$path/$FD_JAVA")
    kotlin.srcDirs("$path/$FD_JAVA", "$path/kotlin")
    resources.srcDir("$path/$FD_JAVA_RES")
    res.srcDir("$path/$FD_RES")
    assets.srcDir("$path/$FD_ASSETS")
    manifest.srcFile("$path/$FN_ANDROID_MANIFEST_XML")
    aidl.srcDir("$path/$FD_AIDL")
    renderscript.srcDir("$path/$FD_RENDERSCRIPT")
    jniLibs.srcDir("$path/jniLibs")
    shaders.srcDir("$path/shaders")
    mlModels.srcDir("$path/$FD_ML_MODELS")
}
