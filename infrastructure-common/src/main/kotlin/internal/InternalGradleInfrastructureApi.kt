package com.redmadrobot.build.internal

/**
 * Public API marked with this annotation is effectively **internal**, which means
 * it should not be used outside of `gradle-infrastructure`.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@RequiresOptIn(level = RequiresOptIn.Level.ERROR)
public annotation class InternalGradleInfrastructureApi
