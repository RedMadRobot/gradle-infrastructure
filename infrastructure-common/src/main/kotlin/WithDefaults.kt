package com.redmadrobot.build

import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.create
import kotlin.reflect.KClass

/**
 * All extensions in namespace 'redmadrobot' should implement this interface
 * to make it possible inherit defaults from parent project.
 */
public interface WithDefaults<T> {
    /** Sets the given [defaults] for this instance. */
    public fun setDefaults(defaults: T)
}

/** Creates extension with the given [name] and sets [defaults] if it is not `null`. */
@PublishedApi
internal inline fun <reified T : WithDefaults<T>> ExtensionContainer.createWithDefaults(
    name: String,
    defaults: T?,
    publicType: KClass<in T>? = null,
): T {
    return if (publicType == null) create(name) else (create(publicType, name, T::class) as T)
        .apply { if (defaults != null) setDefaults(defaults) }
}
