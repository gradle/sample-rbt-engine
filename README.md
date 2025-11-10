# JUnit Platform ReportEntry data capture in Gradle

## Milestone 1 Demo

This project demonstrates capturing `ReportEntry` data published by JUnit Platform during test execution in Gradle.

This project contains a demonstration project in `/demo-m1` which is a JVM library project created using Gradle `init`.

Run the demo project using `./gradlew test` and the tests should pass.
Open the `/demo-m1/build/reports/tests/test/index.html` test report, you should be able to navigate the test reports to see `ReportEntry` data published by the tests appear as metadata.

### Details

The test class is located at `src/test/java/org/example/LibraryTest.java`.

Data published during test construction:

```java
LibraryTest(TestReporter testReporter) {
    testReporter.publishEntry("constructor", "value1");
}
```

is displayed at the class level in the metadata tab in the test report here:
`build/reports/tests/test/org.example.ExampleTest/index.html`.

<img width="652" height="395" alt="image" src="https://github.com/user-attachments/assets/ca234200-8e70-425d-b55b-e9bfea734066" />

Data published during setup/cleanup: 

```java

@BeforeEach
public void beforeEach(TestReporter testReporter) {
    testReporter.publishEntry("beforeEach", "value2");
}

@AfterEach
public void afterEach(TestReporter testReporter) {
    testReporter.publishEntry("afterEach", "value4");
}
```

and during test execution:

```java
@Test
void someLibraryMethodReturnsTrue(TestReporter testReporter) {
    testReporter.publishEntry("test", "value3");

    Library classUnderTest = new Library();
    assertTrue(classUnderTest.someLibraryMethod(), "someLibraryMethod should return 'true'");
}
```

is displayed at the method level in the metadata tab in the test report here: `build/reports/tests/test/org.example.ExampleTest/someLibraryMethodReturnsTrue(TestReporter)/index.html`.

<img width="704" height="468" alt="image" src="https://github.com/user-attachments/assets/234533cf-41fa-4dd7-aa09-efcd44250452" />

## Milestone 1 Tooling API Demo

This project demonstrates capturing `ReportEntry` data published by JUnit Platform during test execution in Gradle when running via the Tooling API.

This project contains a demonstration project in `/demo-m1-tapi` which is a JVM application project that runs the `/demo-m1` build using the Tooling API, and listens for `TestMetadataEvent`s while doing so.

Run the demo project using `./gradlew :demon-m1-tapi:run` and the build should succeed.
You will see the `ReportEntry` data published by the test printed to the console using metadata events:

```text
> Task :demo-m1-tapi:run
Parallel Configuration Cache is an incubating feature.
Reusing configuration cache.
> Task :demo-m1:processTestResources NO-SOURCE
> Task :demo-m1:processResources NO-SOURCE
> Task :demo-m1:compileJava FROM-CACHE
> Task :demo-m1:classes UP-TO-DATE
> Task :demo-m1:compileTestJava FROM-CACHE
> Task :demo-m1:testClasses UP-TO-DATE
Received test metadata event: metadata
  constructor = value1
Received test metadata event: metadata
  beforeEach = value2
Received test metadata event: metadata
  test = value3
Received test metadata event: metadata
  afterEach = value4
BUILD SUCCESSFUL in 1s
```