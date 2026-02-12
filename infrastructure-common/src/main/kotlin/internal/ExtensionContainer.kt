package com.redmadrobot.build.internal

import org.gradle.api.plugins.ExtensionContainer
import kotlin.reflect.KClass

@Suppress("extension_shadowed_by_member")
@PublishedApi
internal inline fun <reified T : Any> ExtensionContainer.findByName(name: String): T? {
    return findByName(name = name, type = T::class)
}

@Suppress("extension_shadowed_by_member")
@PublishedApi
internal fun <T : Any> ExtensionContainer.findByName(name: String, type: KClass<T>): T? {
    return findByName(name)?.let {
        if (type.isInstance(it)) {
            @Suppress("UNCHECKED_CAST")
            it as T
        } else {
            error(
                "Element '$name' of type '${it::class.java.name}' from container '$this' " +
                    "cannot be cast to '${type.qualifiedName}'."
            )
        }
    }
}
