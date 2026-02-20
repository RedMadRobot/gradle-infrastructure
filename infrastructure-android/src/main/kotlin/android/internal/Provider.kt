package com.redmadrobot.build.android.internal

import org.gradle.api.provider.Property

internal inline fun <T : Any> Property<T>.ifPresent(action: (T) -> Unit) {
    if (isPresent) action(get())
}
