package com.redmadrobot.build.dsl

import com.android.build.api.dsl.BuildType
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.internal.findStringProperty
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

/** Debug build type name. */
public const val BUILD_TYPE_DEBUG: String = "debug"

/** Release build type name. */
public const val BUILD_TYPE_RELEASE: String = "release"

/**
 * QA build type name.
 * You can change this value via project property `redmadrobot.android.build.type.qa`.
 */
public var BUILD_TYPE_QA: String = "qa"
    private set

private var qaBuildTypeFinalized: Boolean = false

@OptIn(InternalGradleInfrastructureApi::class)
internal fun Project.finalizeQaBuildType() {
    if (qaBuildTypeFinalized) return
    BUILD_TYPE_QA = findStringProperty("redmadrobot.android.build.type.qa") ?: BUILD_TYPE_QA
    qaBuildTypeFinalized = true
}

/**
 * Shortcut extension field to get the predefined QA [BuildType].
 */
public val <BuildTypeT : BuildType> NamedDomainObjectContainer<BuildTypeT>.qa: BuildTypeT
    get() = getByName(BUILD_TYPE_QA)

/**
 * Shortcut extension method to allow easy access to the predefined `qa` [BuildType]:
 * ```
 * android {
 *     buildTypes {
 *         qa {
 *             // ...
 *         }
 *     }
 * }
 * ```
 */
public fun <BuildTypeT : BuildType> NamedDomainObjectContainer<BuildTypeT>.qa(
    action: BuildTypeT.() -> Unit,
): BuildTypeT = getByName(BUILD_TYPE_QA, action)
