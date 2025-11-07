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
`build/reports/tests/test/org.example.LibraryTest/index.html`.

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

is displayed at the method level in the metadata tab in the test report here: `build/reports/tests/test/org.example.LibraryTest/someLibraryMethodReturnsTrue(TestReporter)/index.html`.

<img width="704" height="468" alt="image" src="https://github.com/user-attachments/assets/234533cf-41fa-4dd7-aa09-efcd44250452" />
