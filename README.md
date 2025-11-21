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

After running the tests, you can find the HTML test reports in `demo-m5-m6/build/reports/tests/test/index.html` and the XML test reports in `demo-m5-m6/build/test-results/TEST-Gradle-Test-Run--demo-m5-m6-test.xml`.

The `index.html` file looks like this:
<img width="1083" height="769" alt="image" src="https://github.com/user-attachments/assets/c1aa204b-6e9b-41f9-ae12-9d5a396f0471" />

If you navigate to individual tests, you can find the output (from the engine) captured per test:
<img width="1291" height="378" alt="image" src="https://github.com/user-attachments/assets/7926f02e-a828-47d6-8273-9da07c45d0f0" />

The simple test engine here discovers one `TestDescriptor` for each `<test>` element in each XML file and prefixes the file name to it.  A most sophisticated engine could also define a nested hierarchy of tests by directory and file if desired.

#### Parallelism

This project is setup to run tests in parallel.
It will distribute tests across 2 TestWorkers.
Parallelism is currently limited to the test definition directory level - that is, each directory configured in the `testDefinitionDirs` property of the `Test` task is sent to the next available TestWorker in a round-robin fashion.
This is the same parallelism as is done for class files in class-based testing.

During test execution the engine will query a [Gradle system property](https://docs.gradle.org/current/userguide/java_testing.html#sec:test_execution) to retrieve the index of the TestWorker that ran the test.
This is then printed, and visible in the per-test output in the generated test report.
If you examine the output of tests in different directories (for example, `bar` and `baz`) you will see different worker indices.

### Failing and Skipped Tests Demo `demo-m5-m6-failing`

This project is similar to the main demo, and uses the same engine, but uses some hard-coded test names that the engine will fail and skip in order to demonstrate failing and skipped tests.

Run the demo project using `./gradlew :demo-m5-m6-failing:test --rerun` and you'll see the task fail with a link to the generated test report:

```text
> Task :demo-m5-m6-failing:test FAILED
Test definitions directory does not exist: /Users/ttresansky/Projects/sample-rbt-engine/demo-m5-m6-failing/src/test/more-definitions

UnknownClass.failing-tests.xml : fails-on-purpose FAILED
    java.lang.RuntimeException at ResourceBasedTestEngine.java:65

4 tests completed, 1 failed, 1 skipped

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':demo-m5-m6-failing:test'.
> There were failing tests. See the report at: file:///Users/ttresansky/Projects/sample-rbt-engine/demo-m5-m6-failing/build/reports/tests/test/index.html
```

The test report will show the failed and skipped tests like this:
<img width="1077" height="642" alt="image" src="https://github.com/user-attachments/assets/a3caf84d-cd00-41c8-a767-1c473611fdb3" />

You can view tests:
<img width="1101" height="699" alt="image" src="https://github.com/user-attachments/assets/61800479-d06a-4bba-b333-3c1662857225" />

And navigate to the failing test to see the reason:
<img width="1074" height="706" alt="image" src="https://github.com/user-attachments/assets/0a40d565-7e3e-4dc0-a42a-741c3b46d029" />

### Dynamic Tests Using a `HierarchicalTestEngine` Demo `demo-m5-m6-hierarchical`

This project uses a more sophisticated engine that implements the `HierarchicalTestEngine` interface to define a simple hierarchy of tests.
The hierarchy is defined as follows: each file in the test definition directories is a `CONTAINER` that should contain 2 lines, an ISO-8601 date and a number of days.

At execution time, dynamic tests will be created and ran for the number of days specified, starting on the given day.
They will all succeed.

Run the demo project using `./gradlew :demo-m5-m6-hierarchical:test --rerun` and you'll see these tests dynamically created and executed:

```text
Gradle Test Executor 6 STANDARD_OUT
    16:03:19.121 [Test worker] INFO org.gradle.rbt.engine.HierarchicalResourceBasedTestEngine -- Discovering tests with engine: [engine:rbt-hierarchical-engine] using selectors:
        DirectorySelector [path = '/Users/ttresansky/Projects/sample-rbt-engine/demo-m5-m6-hierarchical/src/test/definitions']
    16:03:19.128 [Test worker] INFO org.gradle.rbt.engine.HierarchicalResourceBasedSelectorResolver -- Found test definitions in: /Users/ttresansky/Projects/sample-rbt-engine/demo-m5-m6-hierarchical/src/test/definitions/date-tests.rbt
    16:03:19.129 [Test worker] INFO org.gradle.rbt.engine.HierarchicalResourceBasedSelectorResolver -- Found test definitions in: /Users/ttresansky/Projects/sample-rbt-engine/demo-m5-m6-hierarchical/src/test/definitions/more-date-tests.rbt

date-tests.rbt > 2025-11-01 STANDARD_OUT
    16:03:19.164 [Test worker] INFO org.gradle.rbt.descriptor.DynamicDayTestDescriptor -- Executing dynamic test in file: date-tests.rbt, for date: 2025-11-01

date-tests.rbt > 2025-11-02 STANDARD_OUT
    16:03:19.166 [Test worker] INFO org.gradle.rbt.descriptor.DynamicDayTestDescriptor -- Executing dynamic test in file: date-tests.rbt, for date: 2025-11-02

date-tests.rbt > 2025-11-03 STANDARD_OUT
    16:03:19.167 [Test worker] INFO org.gradle.rbt.descriptor.DynamicDayTestDescriptor -- Executing dynamic test in file: date-tests.rbt, for date: 2025-11-03

date-tests.rbt > 2025-11-04 STANDARD_OUT
    16:03:19.167 [Test worker] INFO org.gradle.rbt.descriptor.DynamicDayTestDescriptor -- Executing dynamic test in file: date-tests.rbt, for date: 2025-11-04

date-tests.rbt > 2025-11-05 STANDARD_OUT
    16:03:19.167 [Test worker] INFO org.gradle.rbt.descriptor.DynamicDayTestDescriptor -- Executing dynamic test in file: date-tests.rbt, for date: 2025-11-05

more-date-tests.rbt > 2025-11-18 STANDARD_OUT
    16:03:19.168 [Test worker] INFO org.gradle.rbt.descriptor.DynamicDayTestDescriptor -- Executing dynamic test in file: more-date-tests.rbt, for date: 2025-11-18

more-date-tests.rbt > 2025-11-19 STANDARD_OUT
    16:03:19.168 [Test worker] INFO org.gradle.rbt.descriptor.DynamicDayTestDescriptor -- Executing dynamic test in file: more-date-tests.rbt, for date: 2025-11-19

more-date-tests.rbt > 2025-11-20 STANDARD_OUT
    16:03:19.168 [Test worker] INFO org.gradle.rbt.descriptor.DynamicDayTestDescriptor -- Executing dynamic test in file: more-date-tests.rbt, for date: 2025-11-20
```

The test report will show the files as parents:
<img width="1093" height="619" alt="image" src="https://github.com/user-attachments/assets/d6351409-0e21-447c-be1d-8fb0f5ac32ea" />

And the dynamic tests as children in a hierarchy:
<img width="1109" height="661" alt="image" src="https://github.com/user-attachments/assets/e1a10f8b-f8b8-45ed-9137-2d7ce86a8ba9" />
