## [Unreleased]

- *No changes*

## [0.19.1] (2024-07-31)

### Fixes

- Fixed package of the extension `SigningConfig.fromProperties`. \
  `dsl` → `com.redmadrobot.build.dsl`

## [0.19] (2024-07-26)

> [!NOTE]
> This release removes many implicit features that couldn't be configured from outside.
> It is also a part of a process of removing the features that could be easily implemented via [pre-compiled script plugins](https://docs.gradle.org/current/userguide/implementing_gradle_plugins_precompiled.html) (for example, SDK versions and tests configuration).
> It is recommended to migrate these configurations to pre-compiled script plugins.

### Stable `addSharedSourceSetRoot` extension

Experimental extension `addSharedSourceSetRoot(...)` has been replaced with the new stable version:

```kotlin
android {
    // The old way (Deprecated)
    addSharedSourceSetRoot("debug", "qa")
    
    // The new way
    sourceSets.addSharedSourceSetRoot("debug", "qa")
}
```

### Introduce `SigningConfig.fromProperties` (Experimental)

It is common practice to store keystore credentials in `.properties` file.
This extension lets you apply configurations from a property file.

```kotlin
android {
    signingConfigs.getByName<SigningConfig>("debug") {
        fromProperties(file("cert/debug.properties"))
    }
}
```

### :warning: BREAKING CHANGES

- **common:** Deprecate `redmadrobot.jvmTarget` with deprecation level `Error`.
  Use [JVM Toolchains](https://kotl.in/gradle/jvm/toolchain) instead to specify JVM target.
  Kotlin, Android Gradle Plugin, detekt and many other tools have support for this mechanism,
  In most cases, adding this into your `build.gradle.kts` should be enough:
  ```kotlin
  kotlin {
      jvmToolchain(17)
  }
  ```
  You can also configure [automatic toolchains downloading](https://docs.gradle.org/current/userguide/toolchains.html#sub:download_repositories).
- **android:** Don't set default `minSdk` (it was `23`) and `targetSdk` (it was `33`).
  It was a poor decision to set defaults for these fields as could implicitly bump an SDK version in a project.
  If you want to use `redmadrobot.android` to align SDK versions among all modules, you should set these properties explicitly:
  ```kotlin
  redmadrobot {
      android {
          minSdk = 23
          targetSdk = 34
      }
  }
  ```
- **common:** Disable [automatic repositories adding](https://github.com/RedMadRobot/gradle-infrastructure#automatically-added-repositories) by default.
  If you rely on this feature, consider declaring required repositories explicitly, or enable it in `gradle.properties`:
  ```properties
  redmadrobot.add.repositories=true
  ```
- **android:** Don't apply [`org.gradle.android.cache-fix` plugin](https://github.com/gradle/android-cache-fix-gradle-plugin/) automatically.
  This change allows removing android-cache-fix-gradle-plugin from the project dependencies.
  See [the plugin documentation](https://github.com/gradle/android-cache-fix-gradle-plugin/?tab=readme-ov-file#applying-the-plugin) to learn how to apply this plugin to your project.
- **android:** Don't apply `proguard-android-optimize.txt` rules by default.
  If you need these rules, apply it manually:
  ```kotlin
  android {
      defaultConfig {
          proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
      }
  }
  ```
- **android:** Do not add `LOCK_ORIENTATION` and `CRASH_REPORTS_ENABLED` variables to `BuildConfig` implicitly
- **kotlin:** Don't set `allWarningsAsErrors` flag implicitly.
  It was impossible to disable this feature.
  To keep going with the old behavior, add the following code to your build script:
  ```kotlin
  val warningsAsErrors = findProperty("warningsAsErrors") == "true" || isRunningOnCi
  tasks.withType<KotlinJvmCompile>().configureEach {
      compilerOptions.allWarningsAsErrors = warningsAsErrors
  }
  ```

### Other Changes

- **android:** Use `ANDROID_BUILD_TOOLS_VERSION` env variable for `buildToolsVersion` if the option is not configured (#132)
- **android:** Make an extension `Project.collectProguardFiles` public
- **android:** Add the option `redmadrobot.android.ndkVersion` to specify NDK version for all android modules
- **android:** Remove the workaround for Explicit API enabling as the issue has been [fixed](https://youtrack.jetbrains.com/issue/KT-37652) in Kotlin 1.9
- **android:** Remove disabling of build features `aidl`, `renderScript` and `buildConfig` as they are already disabled by default in new versions of AGP
- **kotlin:** Deprecate accessor `kotlinCompile`.
  It is recommended to use `kotlin.compilerOptions { ... }` to configure compilation instead.
- Update Gradle to `8.9`

### Dependencies

| Dependency            |   Minimal version   |
|-----------------------|:-------------------:|
| Gradle                |    `7.4` → `8.0`    |
| Kotlin Gradle Plugin  | `1.7.10` → `1.9.0`  |
| Android Gradle Plugin |  `7.4.0` → `8.4.0`  |

infrastructure-kotlin:
- [Kotlin](https://kotlinlang.org/docs/whatsnew20.html) `1.8.10` → `2.0.0`

infrastructure-android:
- [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin) `7.4.2` → `8.5.1`
- Remove `android-cache-fix-gradle-plugin` from dependencies
- Remove `com.android.tools:common` from dependencies

infrastructure-detekt:
- [Detekt](https://github.com/detekt/detekt/releases/tag/v1.23.6) `1.22.0` → `1.23.6`
- [JGit](https://projects.eclipse.org/projects/technology.jgit/releases/6.10.0) `6.4.0` → `6.10.0`

## [0.18.1] (2023-04-18)

- Fix compatibility with Gradle lower than 8.0 (#127)
- Update Gradle to 8.1

## [0.18] (2023-03-07)

### Kotlin Gradle Plugin and Android Gradle Plugin removed from transitive dependencies

> :warning: **BREAKING CHANGE!**

It should be simple to change KGP and APG versions, no matter what versions were used on gradle-infrastructure compilation. Previously it was hard to update gradle-infrastructure without updating AGP and KGP, and also it was hard to downgrade AGP or KGP if it was needed.

Since now, KGP and AGP removed from transitive dependencies, and you should add it to your project manually. You can use two different approaches to do it:

1. Add AGP and KGP to top-level `build.gradle.kts` with `apply false`
   ```kotlin
   // (root)/build.gradle.kts

   plugins {
       // Use `apply false` in the top-level build.gradle file to add a Gradle 
       // plugin as a build dependency but not apply it to the current (root) project.
       // Here you can specify desired AGP and KGP versions to use.
       id("com.android.application") version "7.4.2" apply false
       id("org.jetbrains.kotlin.android") version "1.8.10" apply false
   }
   ```

2. If you have `buildSrc` or some other module containing build logic, you can add AGP and KGP to `dependencies` of this module:
   ```kotlin
   // (root)/buildSrc/build.gradle.kts
   
   dependencies {
       // Here you can specify desired AGP and KGP versions to use.
       implementation(kotlin("gradle-plugin", version = "1.8.10"))
       implementation("com.android.tools.build:gradle:7.4.2")
   }
   ```

Minimal required AGP and KGP will always be specified in README.

### BREAKING CHANGES

- **android:** Fixed obfuscation on QA builds with AGP 7.2+ (#120)
- **android:** Default `targetSdk` changed from `32` to `33`
- **android:** Don't set `targetSdk` in library modules. This field is deprecated and doesn't take any effect since AGP 7.4 ([b/230625468](https://issuetracker.google.com/issues/230625468#comment5))
- **android:** Removed default `resourceConfigurations`. Use `resourceConfigurations.add("ru")` if you want to keep old behavior (#115)
- Change default target JVM from 1.8 to 11 (property `redmadrobot.jvmTarget`)

### Other changes

- **android:** Removed workaround for [b/215407138](https://issuetracker.google.com/issues/215407138) that is fixed in AGP 7.4
- **publish:** More detailed description for the case when plugin cannot recognize project type (#116)
- Update Gradle to 8.0.2

### Dependencies

infrastructure-kotlin:
- [Kotlin](https://github.com/JetBrains/kotlin/releases/tag/v1.8.0) `1.7.10` → `1.8.10`

infrastructure-android:
- [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin#7-4-0) `7.2.1` → `7.4.2` (:warning: Minimal required AGP version is `7.4.0`)
- [Android cache fix Gradle plugin](https://github.com/gradle/android-cache-fix-gradle-plugin/releases/tag/v2.7.0) `2.5.6` → `2.7.0`

infrastructure-detekt:
- [Detekt](https://github.com/detekt/detekt/releases/tag/v1.22.0) `1.21.0` → `1.22.0`
- [JGit](https://projects.eclipse.org/projects/technology.jgit/releases/6.4.0) `6.2.0` → `6.4.0`


## [0.17] (2022-07-29)

### Changed

- **android:** Default `minSdk` changed from `21` to `23` (**POTENTIALLY BREAKING CHANGE**)
- **android:** Change target JDK for plugins from 8 to 11
- Update Gradle to 7.5

### Fixed

- Make flag `redmadrobot.add.repositories` work also for `com.redmadrobot.detekt` plugin (#104)

### Dependencies

- [Kotlin Gradle Plugin](https://kotlinlang.org/docs/whatsnew17.html#updates-in-the-kotlin-gradle-plugin-api) `1.6.10` → `1.7.10`
- [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin#7-2-0) `7.1.3` → `7.2.1`
- [Android cache fix Gradle plugin](https://github.com/gradle/android-cache-fix-gradle-plugin/releases/tag/v2.5.6) `2.5.1` → `2.5.6`
- [Detekt](https://github.com/detekt/detekt/releases/tag/v1.21.0) `1.20.0` → `1.21.0`
- [JGit](https://projects.eclipse.org/projects/technology.jgit/releases/6.2.0) `6.1.0` → `6.2.0`

## [0.16.2] (2022-06-17)

### Fixed

- Defaults from parent project are not applied to project (#107)

## [0.16.1] (2022-06-17)

### Added

- Ability to disable automatic repositories adding using project property `redmadrobot.add.repositories` (#104)

### Fixed

- Extension `test` will no longer expose internal type `TestOptionsImpl` (#105)

## [0.16] (2022-04-25)

### Added

- **publish:** Check if a publication can be configured automatically for the project
- **android:** Add files from `proguard/` directory to `consumerProguardFiles` in `android-library` modules (#101)

### Changed

- **android:** Default target API changed to 32
- **android:** Lint results location now configured only for `application` modules.
  Lint running will no longer conflict in `android-library` modules (#102)
- **publish:** Migrate android-library publication to the [new publishing API](https://developer.android.com/reference/tools/gradle-api/7.1/com/android/build/api/dsl/LibraryPublishing) introduced in AGP 7.1
- Revert change from **0.15** when `isRunningOnCi` were made extension on `Project`

### Housekeeping

- Added `@InternalGradleInfrastructureApi` annotation to all API that not intended for public use
- Added missing KDocs for all public classes

### Dependencies

- [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin#7-1-0) `7.0.4` → `7.1.3`
- [Android cache fix Gradle plugin](https://github.com/gradle/android-cache-fix-gradle-plugin/releases/tag/v2.5.1) `2.4.6` → `2.5.1`
- [detekt](https://github.com/detekt/detekt/releases/tag/v1.20.0) `1.19.0` → `1.20.0`
- [plugin-publish-plugin](https://plugins.gradle.org/plugin/com.gradle.plugin-publish/0.21.0) `0.19.0` → `0.21.0`
- [JGit](https://projects.eclipse.org/projects/technology.jgit/releases/6.1.0) `6.0.0` → `6.1.0`

## [0.15] (2022-01-20)

### Added

- Shortcuts for easy access to QA build type:
  ```kotlin
  android {
      buildTypes {
          qa {
              // ...
          }
      }
  }
  ```
- Apply Lint settings to android-library modules (#98)
- Configuration caching support (experimental)
- Version catalogs publication via `com.redmadrobot.publish`

### Changed

- **Breaking change:** property `isRunningOnCi` changed to an extension-property on `Project` for configuration caching support
- Switch to experimental API added in AGP 7.0.0
- Changed QA build type fallbacks from `[release]` to `[debug, release]`

### Fixed

- Fixed error when trying to override `jvmTarget` or `android` options or in subprojects
- Use `jvmTarget` for java target and source compatibility (#93)

### Dependencies

- Android Gradle Plugin 7.0.3 -> 7.0.4
- Android cache fix Gradle plugin 2.4.5 -> 2.4.6

## [0.14] (2021-12-27)

### Dependencies

- Android Gradle Plugin 4.2.2 -> 7.0.3 
- Android cache fix Gradle plugin 2.4.3 -> 2.4.5
- Kotlin 1.5.31 -> 1.6.10
- JGit 5.12.0 -> 6.0.0

### Added

- Added workaround for the case when detekt ignores kotlin sources when run on Android project with type resolution (detekt/detekt#4177)

## [0.13] (2021-12-13)

### Change plugins naming convention

> **Breaking change!**

Plugins group changed from `redmadrobot` to `com.redmadrobot`.
This change allows us to publish infrastructure plugins to [Gradle Plugins Portal](https://plugins.gradle.org/) and make it easier to add plugins to project.

Changing `resolutionStrategy` in `settings.gradle.kts` is not needed anymore.
To make migration easier, it is allowed to apply plugins with deprecated IDs if you've specified `resolutionStrategy`, but it will throw a warning to console.

### Switch to multi-modular structure

> **Breaking change!**

<details>
<summary>Why multi-modular structure?</summary>

Until now plugins' structure inside module `infrastructure` was looking like this:

[![](https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggQlRcbnJvb3RbXCJyb290LXByb2plY3RcIl1cbmtvdGxpbi1saWJyYXJ5IC0tPiByb290XG5kZXRla3QgLS0-IHJvb3RcbnB1Ymxpc2ggLS0-IHJvb3RcbmFwcGxpY2F0aW9uIC0tPiByb290XG5hbmRyb2lkLWxpYnJhcnkgLS0-IHJvb3QiLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOnRydWUsImF1dG9TeW5jIjp0cnVlLCJ1cGRhdGVEaWFncmFtIjp0cnVlfQ)](https://mermaid-js.github.io/mermaid-live-editor/edit/#eyJjb2RlIjoiZ3JhcGggQlRcbnJvb3RbXCJyb290LXByb2plY3RcIl1cbmtvdGxpbi1saWJyYXJ5IC0tPiByb290XG5kZXRla3QgLS0-IHJvb3RcbnB1Ymxpc2ggLS0-IHJvb3RcbmFwcGxpY2F0aW9uIC0tPiByb290XG5hbmRyb2lkLWxpYnJhcnkgLS0-IHJvb3QiLCJtZXJtYWlkIjoie1xuICBcInRoZW1lXCI6IFwiZGVmYXVsdFwiXG59IiwidXBkYXRlRWRpdG9yIjp0cnVlLCJhdXRvU3luYyI6dHJ1ZSwidXBkYXRlRGlhZ3JhbSI6dHJ1ZX0)

Plugin `root-project` was used to add configurations for all others plugins.
With such structure all plugins should know about `root-project` plugin to access configs, and `root-project` should also know about all plugins to hold their configs.
It is possible only if all plugins are declared in the same module with `root-project`.
So it was the main stopping factor from breaking `infrastructure` to several independent modules.

Multi-modular structure gives important benefits to us:

- Allows modules to be updated independently, allowing users to pick what version of each module they want to use.
- Makes `infrastructure` more scalable.
  Because it is not more required to change `root-project` when adding a new plugin.
- Enables to use only "what you want" and don't bring extra dependencies to project.
---
</details>

Since now plugins are separated to modules:

[![](https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggQlRcbnN1YmdyYXBoIGluZnJhc3RydWN0dXJlLWFuZHJvaWRcbiAgICBhbmRyb2lkLWNvbmZpZ1tjb20ucmVkbWFkcm9ib3QuYW5kcm9pZC1jb25maWddXG4gICAgY29tLnJlZG1hZHJvYm90LmFwcGxpY2F0aW9uIC0tPiBhbmRyb2lkLWNvbmZpZ1xuICAgIGNvbS5yZWRtYWRyb2JvdC5hbmRyb2lkLWxpYnJhcnkgLS0-IGFuZHJvaWQtY29uZmlnXG5lbmRcblxuc3ViZ3JhcGggaW5mcmFzdHJ1Y3R1cmUtZGV0ZWt0XG4gICAgY29tLnJlZG1hZHJvYm90LmRldGVrdFxuZW5kXG5cbnN1YmdyYXBoIGluZnJhc3RydWN0dXJlLWtvdGxpblxuICAgIGtvdGxpbi1jb25maWdbY29tLnJlZG1hZHJvYm90LmtvdGxpbi1jb25maWddXG4gICAgY29tLnJlZG1hZHJvYm90LmtvdGxpbi1saWJyYXJ5IC0tPiBrb3RsaW4tY29uZmlnXG5lbmRcblxuc3ViZ3JhcGggaW5mcmFzdHJ1Y3R1cmUtcHVibGlzaFxuICAgIHB1Ymxpc2gtY29uZmlnW2NvbS5yZWRtYWRyb2JvdC5wdWJsaXNoLWNvbmZpZ11cbiAgICBjb20ucmVkbWFkcm9ib3QucHVibGlzaCAtLT4gcHVibGlzaC1jb25maWdcbmVuZFxuXG5hbmRyb2lkLWNvbmZpZyAtLT4ga290bGluLWNvbmZpZyIsIm1lcm1haWQiOnsidGhlbWUiOiJkZWZhdWx0In0sInVwZGF0ZUVkaXRvciI6dHJ1ZSwiYXV0b1N5bmMiOnRydWUsInVwZGF0ZURpYWdyYW0iOnRydWV9)](https://mermaid-js.github.io/mermaid-live-editor/edit/#eyJjb2RlIjoiZ3JhcGggQlRcbnN1YmdyYXBoIGluZnJhc3RydWN0dXJlLWFuZHJvaWRcbiAgICBhbmRyb2lkLWNvbmZpZ1tjb20ucmVkbWFkcm9ib3QuYW5kcm9pZC1jb25maWddXG4gICAgY29tLnJlZG1hZHJvYm90LmFwcGxpY2F0aW9uIC0tPiBhbmRyb2lkLWNvbmZpZ1xuICAgIGNvbS5yZWRtYWRyb2JvdC5hbmRyb2lkLWxpYnJhcnkgLS0-IGFuZHJvaWQtY29uZmlnXG5lbmRcblxuc3ViZ3JhcGggaW5mcmFzdHJ1Y3R1cmUtZGV0ZWt0XG4gICAgY29tLnJlZG1hZHJvYm90LmRldGVrdFxuZW5kXG5cbnN1YmdyYXBoIGluZnJhc3RydWN0dXJlLWtvdGxpblxuICAgIGtvdGxpbi1jb25maWdbY29tLnJlZG1hZHJvYm90LmtvdGxpbi1jb25maWddXG4gICAgY29tLnJlZG1hZHJvYm90LmtvdGxpbi1saWJyYXJ5IC0tPiBrb3RsaW4tY29uZmlnXG5lbmRcblxuc3ViZ3JhcGggaW5mcmFzdHJ1Y3R1cmUtcHVibGlzaFxuICAgIHB1Ymxpc2gtY29uZmlnW2NvbS5yZWRtYWRyb2JvdC5wdWJsaXNoLWNvbmZpZ11cbiAgICBjb20ucmVkbWFkcm9ib3QucHVibGlzaCAtLT4gcHVibGlzaC1jb25maWdcbmVuZFxuXG5hbmRyb2lkLWNvbmZpZyAtLT4ga290bGluLWNvbmZpZyIsIm1lcm1haWQiOiJ7XG4gIFwidGhlbWVcIjogXCJkZWZhdWx0XCJcbn0iLCJ1cGRhdGVFZGl0b3IiOnRydWUsImF1dG9TeW5jIjp0cnVlLCJ1cGRhdGVEaWFncmFtIjp0cnVlfQ)

Plugin `root-project` is deprecated now and will be removed in further versions.
Extensions are added to project via `*-config` plugins (exception is `detekt` plugin, so it is designed to be applied to root project).

If you want to get the same behavior as applying `root-project`, you can apply all config plugins to the root project:

```kotlin
apply {
    id("com.redmadrobot.android-config") version "0.13"
    id("com.redmadrobot.publish-config") version "0.13"
    id("com.redmadrobot.detekt") version "0.13"
}
```

### Configs behavior changes

- `*-config` plugins can be applied to any project.
  Config from inner project will inherit options from outer project.
- Options `redmadrobot.android.test` are inherited from `redmadrobot.test`

### Dependencies

- Kotlin 1.5.30 -> 1.5.31
- Detekt 1.18.1 -> 1.19.0

### Added

- Added repository extension `jitpack()` (#82)

### Changed

- **Breaking change!** Repositories `ossrh` and `ossrhSnapshots` now use host `s01.oss.sonatype.org` by default.
  To keep legacy host `oss.sonatype.org`, use `ossrh(LEGACY)` instead.
- **Breaking change!** Directory with ProGuard rules now intended to be in `application` project instead of root project.
- Default `targetSdk` changed from 30 to 31.
- Gradle updated to 7.3.1

### Fixed

- Non-Kotlin modules are excluded from checking that the module contains detekt [#81](https://github.com/RedMadRobot/gradle-infrastructure/issues/81)

## [0.12.2] (2021-11-15)

### Fixed

- Fixed proguard file detection which was broken after file extensions check was added.

## [0.12.1] (2021-09-07)

### Fixed

- Fixed regex to create `detekt[Variant]All`, `detektBaseline[Variant]All` when infrastructure root project differs from Gradle root project.

## [0.12] (2021-09-01)

### Dependencies

- Kotlin Gradle Plugin 1.5.20 -> 1.5.30
- Android Gradle Plugin 4.2.1 -> 4.2.2
- Android cache fix Gradle plugin 2.4.0 -> 2.4.3
- detekt 1.17.1 -> 1.18.1

### Added

- Plugin `redmadrobot.root-project` now can be applied to any project that should be considered as "root" project for **gradle-infrastructure**.
  It may be useful for projects where you need to apply **gradle-infrastructure** only to particular subprojects.
- Added Detekt tasks `detekt[Variant]All` with type resolution
- Added the `detektBaselineAll` task to create a baseline file for Detekt rules.
- Added `detektBaseline[Variant]All` tasks to create a baseline file for Detekt rules with type resolution.
- Added option `redmadrobot.jvmTarget` to specify target JVM for Kotlin and Java compilers.
- Added option `redmadrobot.android.testTasksFilter` to filter tasks that should be run on ':test'.

### Changed

- Android libraries' publication moved back to `infrastructure` from `infrastructure-android`.
- Use only files with extension `.pro` from `proguard` directory as inputs for R8.

### Housekeeping

- Kotlin API version changed from 1.3 to 1.4
- Gradle 7.1.1 -> 7.2

## [0.11] (2021-07-06)

### Dependencies

- Kotlin Gradle Plugin 1.5.10 -> 1.5.20
- JGit 5.11.0 -> 5.12.0

### Added

- Added ability to configure `compileSdk` separately from `targetSdk`
- Added ability to configure `buildToolsVersion` for all android modules

### Housekeeping

- Update Gradle 7.0.2 -> 7.1.1
- Added KDoc comments to the all plugins

## [0.10] (2021-05-31)

### Lazy Configuration

> **Breaking change!**

`RedmadrobotExtension` migrated to [lazy properties API](https://docs.gradle.org/current/userguide/lazy_configuration.html#lazy_properties).
Now all extension fields are properties.
You should use method `.set(value)` instead of assignment operator `=` to assign value.

```diff
redmadrobot {
    android {
-       minApi = 26
+       minApi.set(26)
    }
}
```

This change helps us to check the Redmadrobot extension not used before it was configured.

### Plugins no more add Kotlin dependencies

> **Breaking change!**

Previously plugins `kotlin-library`, `android-library` and `application` used to add `kotlin-stdlib-jdk8` and `kotlin-test` dependencies by default.
It was a problem because:

1. Sometimes you don't want to add these dependencies
2. Or want to add it with different configuration (for example `compileOnly` instead of `implementation`)

**gradle-infrastructure** should add only options valid for all our projects or default options that can be changed if needed.
Default applied dependencies can't be removed if needed, so they should not be applied by default.

#### Another problem is `redmadrobot.kotlinVersion`.

> Option `redmadrobot.kotlinVersion` is deprecated and will not take any effect.

We've introduced this option to make it possible to change version of default kotlin dependencies.
This option affects only dependencies added by gradle-infrastructure, not all Kotlin dependencies, and it is confusing.
Moreover, this version does not affect Kotlin Gradle Plugin because it uses version specified in gradle-infrastructure at compilation time.

A more convenient way to align the Kotlin version for all dependencies including transitive ones is to use `kotlin-bom`:

```kotlin
dependencies {
    // Align versions of all Kotlin components 
    implementation(platform(kotlin("bom", version = "1.5.10")))

    // Now you can add Kotlin components without version
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit5"))
}
```

### Added

- Specified default location for Detekt baseline: `$configsDir/detekt/baseline.xml`
- Added the ability to check the Detekt only on changed files ([#40](https://github.com/RedMadRobot/gradle-infrastructure/issues/40)).

### Changed

- Update Gradle 7.0 -> 7.0.2
- Flag `publishing.signArtifacts` affects all artifacts not only the main one
- Method `test.useJunit()` can take lambda to configure JUnit framework
- Accessor `redmadrobot.android` can be used without imports
- `RedmadrobotExtension` is [extension-aware](https://docs.gradle.org/current/dsl/org.gradle.api.plugins.ExtensionAware.html) since now
- **Breaking change!** All extensions and constants moved to package `com.redmadrobot.build.dsl` to make it possible to import all extensions via single import

### Fixed

- Publication not configured when `redmadrobot.publish` applied before other plugins
- Look at flag `gradlePlugin.isAutomatedPublishing` when configuring gradle plugin publication

### Dependencies

- Update Kotlin 1.4.32 -> 1.5.10
- Update AGP 4.1.3 -> 4.2.1
- Update Detekt 1.16.0 -> 1.17.1

## [0.9] (2021-05-04)

### infrastructure-android

Module `infrastructure` no more depends on AGP and doesn't require the `google()` repository to be applied.

Plugins `redmadrobot.application` and `redmadrobot.android-library` was moved to `infrastructure-android` module. 
You should specify it in settings.gradle.kts to be able to use it:

```diff
resolutionStrategy {
    eachPlugin {
        if (requested.id.namespace == "redmadrobot") {
-            useModule("com.redmadrobot.build:infrastructure:${requested.version}")
+            useModule("com.redmadrobot.build:infrastructure-android:${requested.version}")
        }
    }
}
```

> **Breaking change!**
> If you use `redmadrobot.android` in the root project, you should add the following import:
>
> ```kotlin
> import com.redmadrobot.build.extension.android
> ```

### QA build type name

`BUILD_TYPE_STAGING` superseded with `BUILD_TYPE_QA`.
To keep backward compatibility you can configure QA build type name using project property in `gradle.properties`:

```properties
redmadrobot.android.build.type.qa=staging
```

### Added

- Apply [android-cache-fix plugin](https://github.com/gradle/android-cache-fix-gradle-plugin) to android projects (#44)

### Changed

- Updated Gradle 6.8.3 -> 7.0
- Completely removed `jcenter` from repositories (#36)

### Fixed

- Flag `warningsAsErrors` now should work.
  It is enabled by default on CI.

## [0.8.2] (2021-03-30)

### JCenter only for exclusive content

JCenter is at the end of life and should mot be used anymore.
Now `jcenter()` used in `gradle-infrastructure` only for exclusive content that not migrated to Maven Central yet.
You can get errors like this:

```
> Could not resolve all files for configuration ':app:debugRuntimeClasspath'.
   > Could not find com.xwray:groupie:2.7.2
     Searched in the following locations:
       - ...
     Required by:
         project :app > com.xwray:groupie:2.7.2
```

To avoid these errors, declare `jcenter` repository in your build script and configure it to be used only for missing dependencies.

```kotlin
repositories {
    jcenter {
        content {
            // It is useful to add a link to the issue about migration from JCenter
            // https://github.com/lisawray/groupie/issues/384
            includeModule("com.xwray", "groupie")
        }
    }
}
```

### Housekeeping

- Detekt 1.15.0 -> 1.16.0
- Kotlin 1.4.31 -> 1.4.32
- AGP 4.1.2 -> 4.1.3

## [0.8.1] (2021-02-28)

### Fixed

- Add back `jcenter()` for `detekt-formatting`

### Housekeeping

- Kotlin updated to 1.4.31

## [0.8] (2021-02-25)

### POM Configuration

Now you can configure common for all modules POM properties in `redmadrobot.publishing` extension.
There are extension-functions to cover common configuration use-cases.

```kotlin
redmadrobot {
    publishing {
        pom {
            // Configure <url>, <scm> and <issueManagement> tags for GitHub project by its name
            setGitHubProject("RedMadRobot/gradle-infrastructure")
            
            licenses { 
                mit() // Add MIT license
            }
            
            developers {
                // Shorthand to add a developer
                developer(id = "jdoe", name = "John Dow", email = "john@doe.com")
            }
        }
    }
}
```

Use `publishing` extension in module build script to configure POM for this module.
Take publication name from `PUBLICATION_NAME` constant.

```kotlin
publishing {
    publications {
        getByName<MavenPublication>(PUBLICATION_NAME) {
            pom {
                // Configure POM here
            }
        }
    }
}

// or even shorter
publishing.publications.getByName<MavenPublication>(PUBLICATION_NAME) {
    pom {
        // Configure POM here
    }
}
```

### Added

- Add javadoc publication for kotlin libraries and gradle plugins

### Fixed

- Fixed gradle plugins signing

## [0.7] (2021-02-25)

### JCenter dropped

> **Breaking change!** Removed adding `jcenter()` repository, replaced with `mavenCentral()`.
> Check all your dependencies can be resolved. If you still want `jcenter`, add it manually. 

Replace `rmrBintray(...)` with `ossrh()` or `ossrhSnapshots()`:
```diff
publishing {
    repositories {
-        if (!isSnapshotVersion) rmrBintray("infrastructure")
+        if (isReleaseVersion) ossrh()
    }
}
```

To be able to publish artifacts to OSSRH you should sign the artifacts.
Signing can be enabled in extension `redmadrobot.publishing`:

```kotlin
redmadrobot {
    publishing {
        signArtifacts = true    // false by default
        useGpgAgent = true      // true by default
    }
}
```

Read [Signing Plugin](https://docs.gradle.org/current/userguide/signing_plugin.html#signing_plugin) docs for more information about signing configuration.

Pull request: #35

### Added

- New extension `isReleaseVersion` opposite to existing `isSnapshotVersion`

### Fixed

- Fix Gradle plugins publication (#29)

### Housekeeping

- Update Gradle to 6.8.3
- Update Kotlin to 1.4.30
- Update AGP to 4.1.2
- Update dependencies in samples
- Added CHANGELOG.md :)

[unreleased]: https://github.com/RedMadRobot/gradle-infrastructure/compare/main..develop
[0.19.1]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.19..v0.19.1
[0.19]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.18.1..v0.19
[0.18.1]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.18..v0.18.1
[0.18]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.17..v0.18
[0.17]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.16.2..v0.17
[0.16.2]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.16.1..v0.16.2
[0.16.1]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.16..v0.16.1
[0.16]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.15..v0.16
[0.15]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.14..v0.15
[0.14]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.13..v0.14
[0.13]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.12.2...v0.13
[0.12.2]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.12.1...v0.12.2
[0.12.1]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.12...v0.12.1
[0.12]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.11...v0.12
[0.11]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.10...v0.11
[0.10]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.9...v0.10
[0.9]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.8.2...v0.9
[0.8.2]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.8.1..v0.8.2
[0.8.1]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.8...v0.8.1
[0.8]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.7...v0.8
[0.7]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.6...v0.7
