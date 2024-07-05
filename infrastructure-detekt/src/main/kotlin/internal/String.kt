package com.redmadrobot.build.detekt.internal

internal fun String.capitalized() = this.replaceFirstChar { it.uppercaseChar() }
