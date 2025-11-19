# Non-Class-Based Testing in Gradle using JUnit Platform

## Milestone 5 and 6 Demos

### Main Demo `demo-m5-m6`

This project demonstrates a (https://docs.junit.org/current/user-guide/#test-engines[JUnit)[Test Engine] that uses resource files to define tests that can be run with JUnit Platform.
The resource files are executed directly by Gradle, there is no need for any test classes to exist.

This project contains a demonstration consumer build in `/demo-m5-m6` and a resource-based `TestEngine` in `/engine`.

Run the demo project using `./gradlew :demo-m5-m6:test --rerun` and you'll see the resource-based tests are executed, as the engine logs them to the console during execution like this:

```text
18:37:30.320 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Discovering tests with engine: [engine:rbt-engine] using selectors:
	DirectorySelector [path = '/Users/ttresansky/Projects/sample-rbt-engine/demo-m5-m6/src/test/definitions']
18:37:30.328 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Found test definitions in: /Users/ttresansky/Projects/sample-rbt-engine/demo-m5-m6/src/test/definitions/tests.xml
18:37:30.339 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Found test definitions in: /Users/ttresansky/Projects/sample-rbt-engine/demo-m5-m6/src/test/definitions/sub/more-tests.xml
18:37:30.340 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Found test definitions in: /Users/ttresansky/Projects/sample-rbt-engine/demo-m5-m6/src/test/definitions/sub2/subsub/even-more-tests.xml
18:37:30.364 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Executing tests with engine: [engine:rbt-engine]
18:37:30.366 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Executing test: Test [file=tests.xml, name=foo]
18:37:30.367 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Executing test: Test [file=tests.xml, name=bar]
18:37:30.368 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Executing test: Test [file=more-tests.xml, name=baz]
18:37:30.368 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Executing test: Test [file=even-more-tests.xml, name=other]
```

This output is provided by the `TestEngine` itself upon executing a test.
See `ResourceBasedTestEngine.java#execute(ExecutionRequest)` defined in the `/engine` project, for details.
Adding a Gradle `TestListener` to the test task would also allow you to receive notifications as tests are executed.

The tests are defined in XML files located in `src/test/definitions` of the demo project.
They are the only files needed to define these tests, and are the only files present in that project.

After running the tests, you can find the test reports in `demo-m5-m6/build/reports/tests/test/index.html`.