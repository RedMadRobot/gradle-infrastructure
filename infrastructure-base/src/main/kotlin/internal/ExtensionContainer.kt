package com.redmadrobot.build.internal

import org.gradle.api.plugins.ExtensionContainer

@Suppress("extension_shadowed_by_member")
@PublishedApi
internal inline fun <reified T : Any> ExtensionContainer.findByName(name: String): T? {
    return findByName(name)?.let {
        it as? T
            ?: error(
                "Element '$name' of type '${it::class.java.name}' from container '$this' " +
                    "cannot be cast to '${T::class.qualifiedName}'.",
            )
    }
}
