# Non-Class-Based Testing in Gradle using JUnit Platform

## Milestone 5 and 6 Demos

### Main Demo `demo-m5-m6`

This project demonstrates a [JUnit Test Engine](https://docs.junit.org/current/user-guide/#test-engines) that uses resource files to define tests that can be run with JUnit Platform.
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

#### Reporting

After running the tests, you can find the test reports in `demo-m5-m6/build/reports/tests/test/index.html`.

The `index.html` file looks like this:
<img width="1111" height="676" alt="image" src="https://github.com/user-attachments/assets/cdf0b4ef-4ac6-41a7-af83-e37f3a8297b9" />

If you navigate to individual tests, you can find the output (from the engine) captured per test:
<img width="1291" height="378" alt="image" src="https://github.com/user-attachments/assets/7926f02e-a828-47d6-8273-9da07c45d0f0" />

#### Parallelism

This project is setup to run tests in parallel.
It will distribute tests across 2 TestWorkers.
Parallelism is currently limited to the test definition directory level - that is, each directory configured in the `testDefinitionDirs` property of the `Test` task is sent to the next available TestWorker in a round-robin fashion.
This is the same parallelism as is done for class files in class-based testing.

During test execution the engine will query a [Gradle system property](https://docs.gradle.org/current/userguide/java_testing.html#sec:test_execution) to retrieve the index of the TestWorker that ran the test.
This is then printed, and visible in the per-test output in the generated test report.
If you examine the output of tests in different directories (for example, `bar` and `baz`) you will see different worker indices.
