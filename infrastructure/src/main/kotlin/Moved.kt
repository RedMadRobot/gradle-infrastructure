package com.redmadrobot.build

import com.redmadrobot.build.dsl.kotlinCompile
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("kotlinCompile(configure)", "com.redmadrobot.build.dsl.kotlinCompile"),
)
public inline fun Project.kotlinCompile(crossinline configure: KotlinCompile.() -> Unit) {
    kotlinCompile(configure)
}
