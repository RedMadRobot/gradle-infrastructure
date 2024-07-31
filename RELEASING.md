# Releasing

**Release:**

1. Ensure the repository is up to date and checkout the `develop` branch.

2. [Update version](#version-update) and remove `-SNAPSHOT` suffix

3. Update the `CHANGELOG.md`:
   1. Replace `Unreleased` section with the release version
   2. Add a link to the diff between the previous and the new version to the bottom of the changelog
   3. Add a new empty `Unreleased` section on the top of the file

4. Commit and push the changes.
   ```bash
   git commit -am "version: [version here]"
   git push
   ```

5. Merge the `develop` branch into `main` using fast-forward. 

6. Create a release tag on the `main` branch:
   ```bash
   # Push the release tag
   git tag v[version]
   git push origin v[version]
   ```
   Release tag push triggers a release workflow on the CI which uploads release artifacts to Maven Central and creates a GitHub release.

**After release:**

1. Rename [milestone](https://github.com/RedMadRobot/gradle-infrastructure/milestones) "Next" to the released version and close it.
2. Create a new milestone with the name "Next."
3. In the `develop` branch [update version](#version-update) to the next potential version with suffix `-SNAPSHOT`.
4. Commit and push.
   ```bash
   git commit -am "version: [version here]-SNAPSHOT"
   git push
   ```

## Version Update

1. Update a version in `gradle.properties`
2. Update a version used in samples in `build.gradle.kts`
3. It the version is not a snapshot, update the version in `README.md`
