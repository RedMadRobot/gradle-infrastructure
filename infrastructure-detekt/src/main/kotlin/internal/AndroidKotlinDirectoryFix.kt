package com.redmadrobot.build.detekt.internal

import com.android.build.gradle.BaseExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named

/**
 * Fix issue when Detekt ignores kotlin source directory if it is not added to javaDirectories in AGP.
 * See: https://github.com/detekt/detekt/issues/4177
 */
internal fun Project.applyAndroidSourceFixToAllProjects() {
    allprojects {
        afterEvaluate {
            if (isKotlinAndroidProject && hasDetektPlugin) fixDetektAndroidSources()
        }
    }
}

// Here are second afterEvaluate. Fix will not work without it
private fun Project.fixDetektAndroidSources() = afterEvaluate {
    val android = extensions.getByType<BaseExtension>()
    android.variants?.forEach { variant ->
        tasks.named<Detekt>("detekt${variant.name.capitalize()}") {
            setSource(variant.sourceSets.map { it.kotlinDirectories })
        }
        tasks.named<DetektCreateBaselineTask>("detektBaseline${variant.name.capitalize()}") {
            setSource(variant.sourceSets.map { it.kotlinDirectories })
        }
    }
}
