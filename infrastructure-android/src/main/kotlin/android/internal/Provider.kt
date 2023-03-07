package com.redmadrobot.build.android.internal

import org.gradle.api.provider.Property

internal fun <T> Property<T>.ifPresent(action: (T) -> Unit) {
    if (isPresent) action(get())
}
