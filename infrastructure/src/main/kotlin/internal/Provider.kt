package com.redmadrobot.build.internal

import org.gradle.api.provider.Property

internal fun <T : Any> Property<T>.setFinalValue(value: T) {
    set(value)
    finalizeValue()
}
