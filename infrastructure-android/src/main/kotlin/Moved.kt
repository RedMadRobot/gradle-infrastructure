package com.redmadrobot.build

import com.redmadrobot.build.dsl.BUILD_TYPE_DEBUG
import com.redmadrobot.build.dsl.BUILD_TYPE_QA
import com.redmadrobot.build.dsl.BUILD_TYPE_RELEASE

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("BUILD_TYPE_DEBUG", "com.redmadrobot.build.dsl.BUILD_TYPE_DEBUG"),
)
public val BUILD_TYPE_DEBUG: String by ::BUILD_TYPE_DEBUG

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("BUILD_TYPE_RELEASE", "com.redmadrobot.build.dsl.BUILD_TYPE_RELEASE"),
)
public val BUILD_TYPE_RELEASE: String by ::BUILD_TYPE_RELEASE

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("BUILD_TYPE_QA", "com.redmadrobot.build.dsl.BUILD_TYPE_QA"),
)
public val BUILD_TYPE_QA: String by ::BUILD_TYPE_QA

/** Superseded with [BUILD_TYPE_QA]. */
@Deprecated(
    message = "Use BUILD_TYPE_QA instead. You can configure QA build type name to keep backward compatibility.",
    replaceWith = ReplaceWith("BUILD_TYPE_QA", "com.redmadrobot.build.dsl.BUILD_TYPE_QA"),
)
public val BUILD_TYPE_STAGING: String by ::BUILD_TYPE_QA
