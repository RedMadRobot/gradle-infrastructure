## [Unreleased]

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
            // Configure <url>, <scm> and <issueManagement> tags for GitHub project by it's name
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

[unreleased]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.8.1...main
[0.8.1]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.8...v0.8.1
[0.8]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.7...v0.8
[0.7]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.6...v0.7
