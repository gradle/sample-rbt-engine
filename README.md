# Non-Class-Based Testing in Gradle using JUnit Platform

## Milestone 8 Demos

### Main Demo `demo-m8`

This demo uses the same Test Engine as used in the M5 and M6 demo - with one minor adjustment to properly mark the discovered non-class-based tests as having `FileSource`.

This project contains a demonstration consumer build in `/demo-m8` and a resource-based `TestEngine` in `/engine`.

The selected test definition files are filtered in various ways to execute only a subset of tests defined in those files.
Filtering is done against the _relative path_ of the test definition file against the _project root directory_.
Filtering is only possible at the test definition file level, not at the individual test case level.
A leading `/` is optional.
Unix-style path separators (`/`) are used regardless of the host OS.

The project defines a number of different test suites (and associated test tasks) that demonstrate different ways to select and filter tests defined in the resource files.
See the `build.gradle.kts` file in the `demo-m8` project for details on how each filter configured.

Run the demo project using `./gradlew :demo-m8:<TASK> --rerun --info`.
You can replace `<TASK>` with any of the following:

| Task                                  | Description                                                                                                     |
|---------------------------------------|-----------------------------------------------------------------------------------------------------------------|
| `test`                                | Runs all tests defined in the resource files.                                                                   |
| `excludeMoreDefs`                     | Excludes every test under `/more-definitions`                                                                   |
| `includeOnlyMoreDefs`                 | Includes only the tests under `/more-definitions`                                                               |
| `includeOnlyNumericTestsNotInASubDir` | Includes only the tests named `tests-<NUMBER>.xml` not with any ancestor directory containing `sub` in its name |

Info about which tests are executed will be visible in the console output, and in the generated HTML reports.

### Dynamic Tests Using a `HierarchicalTestEngine` Demo `demo-m8-hierarchical`

This demo uses the same Test Engine as used in the M5 and M6 hierarchical testing demo - with the same minor adjustment to properly mark the discovered non-class-based tests as having `FileSource`.

The project defines a number of different test suites (and associated test tasks) that demonstrate different ways to select and filter tests defined in the resource files.
See the `build.gradle.kts` file in the `demo-m8-hierarchical` project for details on how each filter configured.

Run the demo project using `./gradlew :demo-m8-hierarchical:<TASK> --rerun --info`.
You can replace `<TASK>` with any of the following:

| Task                                                   | Description                                                                                                                              |
|--------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| `test`                                                 | Runs all tests defined in the resource files.                                                                                            |
| `excludeMoreDateTests`                                 | Excludes every test in `/src/test/definitions/more-date-tests.rbt`                                                                       |
| `includeOnlyMoreDateTests`                             | Includes only the tests in `/src/test/definitions/more-date-tests.rbt`                                                                   |
| `includeOnlyDateTestsButExcludeMoreDateTestsAndSubDir` | Includes only the tests in files with names containing `date-tests` not in `more-definitions` or with any ancestor directory named `sub` |
