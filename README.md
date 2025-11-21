# Milestone 7 Demo

This project demonstrates running non-class-based tests via the Tooling API. The demo consists of the following projects:
- `engine`: contains a JUnit Test engine demonstrating resource-based test execution.
- `demo-m7`: a test project that defines tests in the `tests.xml` using the test engine.
- `demo-m7-client`: a sample Tooling API client that invokes the `customTest` task on the `demo-m7` project and renders the progress events on the console.

Run the demo project using `./gradlew :demo-m7-client:run -q` from the root of this repository.

## Details

The test engine defined in `engine` project is slightly modified to exhibit the following behavior. For each test definition it:
- provides some output on stdout for each test,
- will fail tests with names containing the 'fail' substring,
- will skip tests with names containing the 'skip' substring,
- will publish metadata for tests with names containing the 'publishMetadata' substring,
- will publish a file entry for tests with names containing the 'publishFile' substring, and
- succeeds the remaining tests.

The `demo-m7` project defines a `customtest` task that uses the test engine and exercise all the above behaviors.

Tooling API client is defined in the `M7ToolingClient` class. It connects to the `demo-m7` project and invokes the `customTest` task.
The client listens to the following progress event operation types: TEST, TEST_OUTPUT and TEST_METADATA.

The rendered output should look similar to the following:

```
------------------------------------------------
Running resource-based tests via the Tooling API
------------------------------------------------
START    Gradle Test Run :demo-m7:customTest 
  START    Gradle Test Executor 27 
    START    Test tests.xml : failingTest (file=/Users/donat/Development/git/gradle/sample-rbt-engine/demo-m7/src/customTest/definitions/tests.xml) 
      OUTPUT    StdOut: Test engine fails tests with names containing the 'fail' substring Test [file=tests.xml, name=failingTest] 
    FAILURE   Test tests.xml : failingTest (file=/Users/donat/Development/git/gradle/sample-rbt-engine/demo-m7/src/customTest/definitions/tests.xml) (reason: Test failed because its name contains the 'fail' substring)
    START    Test tests.xml : skippedTest (file=/Users/donat/Development/git/gradle/sample-rbt-engine/demo-m7/src/customTest/definitions/tests.xml) 
      OUTPUT    StdOut: Test engine skips tests with names containing the 'skip' substring Test [file=tests.xml, name=skippedTest] 
    SKIPPED   Test tests.xml : skippedTest (file=/Users/donat/Development/git/gradle/sample-rbt-engine/demo-m7/src/customTest/definitions/tests.xml) 
    START    Test tests.xml : publishMetadataTest (file=/Users/donat/Development/git/gradle/sample-rbt-engine/demo-m7/src/customTest/definitions/tests.xml) 
      OUTPUT    StdOut: Test engine publishes some metadata for tests with names containing the 'publishMetadata' substring Test [file=tests.xml, name=publishMetadataTest] 
      METADATA  Entry:{non-class-based-testing=check} 
    SUCCESS   Test tests.xml : publishMetadataTest (file=/Users/donat/Development/git/gradle/sample-rbt-engine/demo-m7/src/customTest/definitions/tests.xml) 
    START    Test tests.xml : publishFileTest (file=/Users/donat/Development/git/gradle/sample-rbt-engine/demo-m7/src/customTest/definitions/tests.xml) 
      OUTPUT    StdOut: Test engine publishes a file entry for tests with names containing the 'publishFile' substring Test [file=tests.xml, name=publishFileTest] 
      METADATA  File: /var/folders/6v/j9zhh5fs00b_bbr2f35_7s7m0000gq/T/tests.txt type: text/plain 
    SUCCESS   Test tests.xml : publishFileTest (file=/Users/donat/Development/git/gradle/sample-rbt-engine/demo-m7/src/customTest/definitions/tests.xml) 
    START    Test tests.xml : passingTest (file=/Users/donat/Development/git/gradle/sample-rbt-engine/demo-m7/src/customTest/definitions/tests.xml) 
    SUCCESS   Test tests.xml : passingTest (file=/Users/donat/Development/git/gradle/sample-rbt-engine/demo-m7/src/customTest/definitions/tests.xml) 
  FAILURE   Gradle Test Executor 27 
FAILURE   Gradle Test Run :demo-m7:customTest 
```

The indentations represent the event hierarchy. It demonstrates that the Tooling API client can successfully execute non-class-based tests, and it emits the same events and data for
- Test started/finished
- The test results: successful, failed, skipped
- The test metadata (including ReportEntry and FileEntry), and
- Test output

NOTE: this demo uses a snapshot version of Gradle 9.4.0 that includes the non-class-based testing feature. 
We'll update the demo once a milestone or a release version is available with the feature. 