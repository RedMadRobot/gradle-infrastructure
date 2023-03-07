## Release Notes

ADD RELEASE NOTES HERE

## Checklist

Prepare:

- [ ] Version in root `build.gradle.kts` changed, `-SNAPSHOT` removed
- [ ] Version in `README.md` updated
- [ ] Version in samples changed
- [ ] Compatibility table in `README.md` updated
- [ ] `README.md` contains valid and fresh information
- [ ] Add version with date to `CHANGELOG.md`, also add link to diff

Release:

- [ ] Run `publish.sh`
- [ ] Publish release on [OSSRH](https://s01.oss.sonatype.org/)
- [ ] Rebase `main` on `develop`
- [ ] Publish release with release notes on GitHub

After:

- [ ] Bump version in `develop` branch, add `-SNAPSHOT` to it
- [ ] Change infrastructure version in samples
- [ ] Rename and close milestone. Create new milestone with name `Next`
