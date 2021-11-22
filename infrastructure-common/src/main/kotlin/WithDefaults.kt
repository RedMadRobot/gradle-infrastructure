package com.redmadrobot.build

import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.create

/**
 * All extensions in namespace 'redmadrobot' should implement this interface
 * to make it possible inherit defaults from parent project.
 */
public interface WithDefaults<T> {
    public fun setDefaults(defaults: T)
}

/** Creates extension with the given [name] and sets [defaults] if it is not `null`. */
@PublishedApi
internal inline fun <reified T : WithDefaults<T>> ExtensionContainer.createWithDefaults(
    name: String,
    defaults: T?,
): T {
    return create<T>(name)
        .apply { if (defaults != null) setDefaults(defaults) }
}
