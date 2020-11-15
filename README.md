# infrastructure <GitHub path="RedMadRobot/gradle-infrastructure"/>
[![Version](https://img.shields.io/bintray/v/redmadrobot-opensource/android/infrastructure?style=flat-square)][bintray] [![Build Status](https://img.shields.io/github/workflow/status/RedMadRobot/gradle-infrastructure/CI/main?style=flat-square)][ci] [![License](https://img.shields.io/github/license/RedMadRobot/gradle-infrastructure?style=flat-square)][license]

Small plugins to reduce boilerplate in Gradle build scripts.

---
<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


- [Installation](#installation)
- [Plugins](#plugins)
  - [kotlin-library](#kotlin-library)
  - [application and android-library](#application-and-android-library)
  - [publish](#publish)
  - [detekt](#detekt)
- [Usage](#usage)
  - [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)
  - [Tests failed - `No value has been specified for property 'localResourcesFile'`](#tests-failed---no-value-has-been-specified-for-property-localresourcesfile)
  - [Build failed on CI - `No version of NDK matched the requested version`](#build-failed-on-ci---no-version-of-ndk-matched-the-requested-version)
- [Contributing](#contributing)
- [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Installation

Add repositories to `settings.gradle.kts`:
```kotlin
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven(url = "https://dl.bintray.com/redmadrobot-opensource/android")
    }
}
```

Then you can apply any of plugins where you need:
```kotlin
plugins {
    id("redmadrobot.kotlin-library") version "0.3"
    id("redmadrobot.publish") version "0.3"
    id("redmadrobot.detekt") version "0.3"
}
```

<details>
  <summary>Alternatively you can specify plugin resolution strategy and set infrastructure version there</summary>

```kotlin
// settings.gradle.kts
pluginManagement {
    //...

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "redmadrobot") {
                useModule("com.redmadrobot.build:infrastructure:0.3")
            }
        }
    }
}

// build.gradle.kts
plugins {
    id("redmadrobot.kotlin-library")
    id("redmadrobot.publish")
    id("redmadrobot.detekt")
}
```
</details>

## Plugins

### kotlin-library

Common configurations for pure Kotlin libraries.

- Applies plugin `kotlin`
- Specifies `jvmTarget` 1.8
- Adds repository `jcenter`
- Adds `kotlin-stdlib-jdk8` as dependency with `api` scope
- Adds `kotlin-test` and `junit` as test dependencies
- Enables [explicit API mode][explicit-api]

### application and android-library

Common configurations for Android libraries and application.

Both:
- Specifies `jvmTarget` and `compatibility` 1.8
- Specifies default compile, min and target SDK
- Disables `aidl`, `renderScript` and `shaders` [build-features]
- Adds `kotlin/` dirs to source sets
- Adds repositories `jcenter` and `google`
- Adds `kotlin-stdlib-jdk8` as dependency
- Adds `kotlin-test` and `junit` as test dependency

Library:
- Applies plugin `com.android.library`
- Disables `buildConfig` and `resValues` [build-features] 
- Enables [explicit API mode][explicit-api]

Application:
- Applies plugin `com.android.application`
- Adds all proguard files from `proguard` folder
- Configures `debug`, `staging` and `release` build types
- Adds `LOCK_ORIENTATION` and `CRASH_REPORTS_ENABLED` BuildConfig variables which `false` only for `debug` build type

### publish

Common publish configurations for both Android and Kotlin libraries.

- Applies plugin `maven-publish`
- Adds sources to publication

You should specify publishing repositories manually. You can also use [predicates] for publication:
```kotlin
publishing {
    repositories {
        // Unconditional publication
        rmrNexus()
        // Publication with conditions
        if (isRunningOnCi) githubPackages("RedMadRobot/gradle-infrastructure")
        if (!isSnapshotVersion && credentialsExist("bintray")) rmrBintray("infrastructure")
    }
}
```

### detekt

- Adds repository `jcenter`
- Adds `detekt` and `detekt-formatting`
- Configures additional tasks:
  - `detektAll` - Runs over whole code base
  - `detektFormat` - Reformats whole code base

## Usage

### Configuration

You can configure the plugins with an extension named `redmadrobot`.
Look for available properties with description in [RedmadrobotExtension].

The extension should be configured in root project.
```kotlin
// root project build.gradle.kts

redmadrobot {
    configsDir.set(file("path/to/configs/"))
}
```

## Troubleshooting

### Tests failed - `No value has been specified for property 'localResourcesFile'`

```
A problem was found with the configuration of task ':mylib:generateReleaseUnitTestStubRFile' (type 'GenerateLibraryRFileTask').
> No value has been specified for property 'localResourcesFile'.
```

It is a [known bug](https://issuetracker.google.com/issues/161586464) in AGP 4.1.0 caused when `androidResources` is disabled.
As workaround, you can enable this build feature for module:
```kotlin
android {
    // TODO: Remove when bug in AGP will be fixed.
    //  https://issuetracker.google.com/issues/161586464
    buildFeatures.androidResources = true
}
```

### Build failed on CI - `No version of NDK matched the requested version`

```
Execution failed for task ':app:stripDebugDebugSymbols'.
> No version of NDK matched the requested version 21.0.6113669. Versions available locally: 21.1.6352462
```

It is because NDK version on CI differs from a requested version.
You can change requested version by setting `android.ndkVersion`.

Plugins `redmadrobot.android-library` and `redmadrobot.application` by default apply NDK version from env variable `ANDROID_NDK_VERSION` if it set.

## Contributing
Merge requests are welcome.
For major changes, please open an issue first to discuss what you would like to change.

## License

[MIT][license]

[RedmadrobotExtension]: src/main/kotlin/extension/RedmadrobotExtension.kt
[predicates]: src/main/kotlin/extension/PublishingPredicates.kt

[bintray]: https://bintray.com/redmadrobot-opensource/android/infrastructure
[ci]: https://github.com/RedMadRobot/gradle-infrastructure/actions
[license]: LICENSE

[build-features]: https://developer.android.com/reference/tools/gradle-api/com/android/build/api/dsl/BuildFeatures
[explicit-api]: https://kotlinlang.org/docs/reference/whatsnew14.html#explicit-api-mode-for-library-authors
