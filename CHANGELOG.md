## [Unreleased]

### Publication to JCenter dropped

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

[unreleased]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.6...main
