# Demos

## Milestone 2 & 3 Demo

This project demonstrates capturing `ReportEntry` and `FileEntry` data published by JUnit Platform during test execution in Gradle.

This project contains a demonstration project in `demo-m2-3`, which is a JVM library project created using Gradle `init`.

Run the demo project using `./gradlew :demo-m2-3:test` and the tests should pass.

The following files are only created after running the tests:

Open the [test report](demo-m2-3/build/reports/tests/test/index.html), you should be able to navigate the test reports to see `FileEntry` data published by the tests appear as "attachments" and `ReportEntry` data published as "data".
Previously, this was listed under "metadata".

The [JUnit XML report](demo-m2-3/build/test-results/test/TEST-org.example.LibraryTest.xml) also includes the same data:
- `ReportEntry` are captured as `<properties/>`
- `FileEntry` are captured as `[[ATTACHMENT|/path/to/file]]` based on [conventions used by Jenkins, Azure pipelines and GitLab](https://kohsuke.org/2012/03/13/attaching-files-to-junit-tests/).

### Details

The test class is located at [`LibraryTest.java`](demo-m2-3/src/test/java/org/example/LibraryTest.java).

Data published during test construction:
```java
LibraryTest(TestReporter testReporter) {
    testReporter.publishEntry("constructor", "value1");
    // Publish a file at the class level
    testReporter.publishFile("constructor.json", MediaType.APPLICATION_JSON, path -> {
        Files.writeString(path, "{ constructor: [] }");
    });
}
```
is displayed at the class level in the data and attachments tabs.

Data published during setup/cleanup: 
```java
@BeforeEach
public void beforeEach(TestReporter testReporter) {
    testReporter.publishEntry("beforeEach", "value2");
    // Publish a text file before the test
    testReporter.publishFile("beforeEach.txt", MediaType.TEXT_PLAIN, path -> {
        Files.writeString(path, "Hello world");
    });
}

@AfterEach
public void afterEach(TestReporter testReporter) {
    testReporter.publishEntry("afterEach", "value4");
    // Publish a (fake) video file after the test
    testReporter.publishFile("afterEach.mp4", MediaType.create("video", "mp4"), path -> {
        Files.writeString(path, "This is just a text file that pretends to be a video.");
    });
}
```
and during test execution:
```java
@Test
void someLibraryMethodReturnsTrue(TestReporter testReporter) {
    testReporter.publishEntry("test", "value3");
    // Publish an image during the test
    testReporter.publishFile("test.svg", MediaType.create("image", "svg+xml"), path -> {
        Files.writeString(path, "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 800 600\"><rect width=\"800\" height=\"600\" fill=\"#F5F5F0\"/><rect width=\"350\" height=\"600\" fill=\"#D4292E\"/><rect x=\"350\" width=\"450\" height=\"250\" fill=\"#1356A3\"/><rect x=\"350\" y=\"250\" width=\"200\" height=\"350\" fill=\"#F7D838\"/><rect x=\"550\" y=\"250\" width=\"250\" height=\"350\" fill=\"#F5F5F0\"/><line x1=\"350\" y1=\"0\" x2=\"350\" y2=\"600\" stroke=\"#1A1A1A\" stroke-width=\"12\"/><line x1=\"350\" y1=\"250\" x2=\"800\" y2=\"250\" stroke=\"#1A1A1A\" stroke-width=\"12\"/><line x1=\"550\" y1=\"250\" x2=\"550\" y2=\"600\" stroke=\"#1A1A1A\" stroke-width=\"12\"/><rect width=\"800\" height=\"600\" fill=\"none\" stroke=\"#1A1A1A\" stroke-width=\"14\"/></svg>");
    });

    Library classUnderTest = new Library();
    assertTrue(classUnderTest.someLibraryMethod(), "someLibraryMethod should return 'true'");
}
```
is displayed at the method level in the data and attachments tabs.

The report renders any file with a `image/*` media type as an image and any file with a `video/*` media type as a video. All other files are just linked to. 
The order of attachments is based on the order they are published. 

It should look something like:
<img src="attachments.png">

NOTE: In this demo, the video is not a real video file and will not play.  

## Milestone 2 & 3 Tooling API Demo

This project demonstrates capturing `ReportEntry` and `FileEntry` data published by JUnit Platform during test execution in Gradle when running via the Tooling API.

This project contains a demonstration project in `demo-m2-3-tapi` which is a JVM application project that runs the `demo-m2-3` build using the Tooling API, and listens for `TestMetadataEvent`s while doing so.

Run the demo project using `./gradlew :demo-m2-3-tapi:run` and the build should succeed.
You will see the data and attachments published by the test printed to the console using metadata events:

```text
> Task :demo-m2-3-tapi:run
> Task :demo-m2-3:processResources NO-SOURCE
> Task :demo-m2-3:processTestResources NO-SOURCE
> Task :demo-m2-3:compileJava UP-TO-DATE
> Task :demo-m2-3:classes UP-TO-DATE
> Task :demo-m2-3:compileTestJava UP-TO-DATE
> Task :demo-m2-3:testClasses UP-TO-DATE
Received test metadata event for Test class org.example.LibraryTest
Key-values (size: 1):
	constructor = value1

Received test metadata event for Test class org.example.LibraryTest
File attachment (application/json):
	/Users/sterling/gits/sample-rbt-engine/demo-m2-3/build/junit-jupiter/org.example.LibraryTest/someLibraryMethodReturnsTrue(org.junit.jupiter.api.TestReporter)/constructor.json

Received test metadata event for Test someLibraryMethodReturnsTrue(TestReporter)(org.example.LibraryTest)
Key-values (size: 1):
	beforeEach = value2

Received test metadata event for Test someLibraryMethodReturnsTrue(TestReporter)(org.example.LibraryTest)
File attachment (text/plain):
	/Users/sterling/gits/sample-rbt-engine/demo-m2-3/build/junit-jupiter/org.example.LibraryTest/someLibraryMethodReturnsTrue(org.junit.jupiter.api.TestReporter)/beforeEach.txt

Received test metadata event for Test someLibraryMethodReturnsTrue(TestReporter)(org.example.LibraryTest)
Key-values (size: 1):
	test = value3

Received test metadata event for Test someLibraryMethodReturnsTrue(TestReporter)(org.example.LibraryTest)
File attachment (image/svg+xml):
	/Users/sterling/gits/sample-rbt-engine/demo-m2-3/build/junit-jupiter/org.example.LibraryTest/someLibraryMethodReturnsTrue(org.junit.jupiter.api.TestReporter)/test.svg

Received test metadata event for Test someLibraryMethodReturnsTrue(TestReporter)(org.example.LibraryTest)
Key-values (size: 1):
	afterEach = value4

Received test metadata event for Test someLibraryMethodReturnsTrue(TestReporter)(org.example.LibraryTest)
File attachment (video/mp4):
	/Users/sterling/gits/sample-rbt-engine/demo-m2-3/build/junit-jupiter/org.example.LibraryTest/someLibraryMethodReturnsTrue(org.junit.jupiter.api.TestReporter)/afterEach.mp4

> Task :demo-m2-3:test

BUILD SUCCESSFUL in 2s
```
