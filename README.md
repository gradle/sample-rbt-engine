= Non-Class-Based Testing in Gradle using JUnit Platform

== Milestone 5 and 6 Demos

=== Main Demo `demo-m5-m6`

This project demonstrates a https://docs.junit.org/current/user-guide/#test-engines[JUnit Test Engine] that uses resource files to define tests that can be run with JUnit Platform.
The resource files are executed directly by Gradle, there is no need for any test classes to exist.

This project contains a demonstration consumer build in `/demo-m5-m6` and a resource-based `TestEngine` in `/engine`.

Run the demo project using `./gradlew :demo-m5-m6:test --rerun` and you'll see the resource-based tests are executed, as the engine logs them to the console during execution like this:

[source,text]
----
12:13:04.089 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Executing tests with engine: [engine:rbt-engine]

UnknownClass.tests.xml : foo STANDARD_OUT
12:13:04.092 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Executing test: Test [file=tests.xml, name=foo]

UnknownClass.tests.xml : bar STANDARD_OUT
12:13:04.093 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Executing test: Test [file=tests.xml, name=bar]

UnknownClass.more-tests.xml : baz STANDARD_OUT
12:13:04.093 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Executing test: Test [file=more-tests.xml, name=baz]

UnknownClass.even-more-tests.xml : other STANDARD_OUT
12:13:04.093 [Test worker] INFO org.gradle.rbt.engine.ResourceBasedTestEngine -- Executing test: Test [file=even-more-tests.xml, name=other]

----



The tests are defined in XML files located in `src/test/definitions` of the demo project.
They are the only files needed to define these tests, and are the only files present in that project.

After running the tests, you can find the test reports in `demo-m5-m6/build/reports/tests/test/index.html`.