# Resource-Based Testing in JUnit 5

## Milestone 4 Demo

This project demonstrates a [JUnit Test Engine](https://docs.junit.org/current/user-guide/#test-engines) that uses resource files to define tests that can be run with JUnit Platform.
The resource files are executed directly by Gradle, there is no need for any test classes to exist.

This project contains a demonstration consumer build in `/demo-m4` and a resource-based `TestEngine` in `/engine`.

Run the demo project using `./gradlew test --rerun` and you'll see the resource-based tests are discovered, as the engine logs them to the console like this:

```text
Gradle Test Executor 48 STANDARD_OUT
    10:53:14.117 INFO  o.g.r.engine.ResourceBasedTestEngine - Discovering tests with engine: [engine:rbt-engine] using selectors:
        DirectorySelector [path = '/Users/ttresansky/Projects/sample-rbt-engine/demo-m4/src/test/definitions']
    10:53:14.125 INFO  o.g.r.engine.ResourceBasedTestEngine - Found test definitions in: /Users/ttresansky/Projects/sample-rbt-engine/demo-m4/src/test/definitions/tests.xml
    10:53:14.129 INFO  o.g.r.engine.ResourceBasedTestEngine - Found test definitions in: /Users/ttresansky/Projects/sample-rbt-engine/demo-m4/src/test/definitions/sub/more-tests.xml
    10:53:14.130 INFO  o.g.r.engine.ResourceBasedTestEngine - Found test definitions in: /Users/ttresansky/Projects/sample-rbt-engine/demo-m4/src/test/definitions/sub2/subsub/even-more-tests.xml
```
