package com.redmadrobot.build

import com.redmadrobot.build.internal.findByName
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create
import kotlin.properties.ReadOnlyProperty

/** Extension used as a namespace for all infrastructure plugins' extensions. */
public interface RedmadrobotExtension : StaticAnalyzerSpec, ExtensionAware {

    /**
     * JVM version to be used as a target by Kotlin and Java compilers.
     * By default, used 1.8.
     */
    public val jvmTarget: Property<JavaVersion>

    public companion object {
        /** Extension name. */
        public const val NAME: String = "redmadrobot"

        /**
         * Provides delegate to add an extra property to [RedmadrobotExtensionImpl].
         *
         * It may be useful to use package `org.gradle.kotlin.dsl` for delegated properties because
         * members from this package are imported by default and declared property can be used without import.
         * This package is used by Gradle for generated accessors.
         *
         * ```
         * package org.gradle.kotlin.dsl
         *
         * val RedmadrobotExtension.android: AndroidOptions by RedmadrobotExtension.extensionProperty()
         * ```
         */
        public inline fun <reified V : Any> extensionProperty(): ReadOnlyProperty<RedmadrobotExtension, V> {
            return ReadOnlyProperty { thisRef, property ->
                thisRef.extensions.findByName<V>(property.name)
                    ?: thisRef.extensions.create(property.name)
            }
        }
    }
}
