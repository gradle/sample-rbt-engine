@file:Suppress("UnstableApiUsage")

plugins {
    `java-library`
}

class TestListenerImpl : TestListener {
    val logger: Logger = Logging.getLogger(TestListenerImpl::class.java)

    override fun beforeSuite(suite: TestDescriptor) { logger.warn("START [$suite] [$suite.name]") }
    override fun afterSuite(suite: TestDescriptor, result: TestResult) { logger.warn("FINISH [$suite] [$suite.name] [$result.resultType] [$result.testCount]") }
    override fun beforeTest(test: TestDescriptor) { logger.warn("START [$test] [$test.name]") }
    override fun afterTest(test: TestDescriptor, result: TestResult) { logger.warn("FINISH [$test] [$test.name] [$result.resultType] [$result.testCount] [$result.exception]") }
}
val listener = TestListenerImpl()

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

testing {
    suites {
        named("test", JvmTestSuite::class) {
            useJUnitJupiter()

            dependencies {
                implementation(project(":engine"))
            }

            targets.all {
                testTask.configure {
                    addTestListener(listener)
                    testDefinitionDirs.from("src/test/definitions")
                }
            }
        }
    }
}
