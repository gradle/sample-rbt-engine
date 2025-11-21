@file:Suppress("UnstableApiUsage")

plugins {
    `java-library`
    `java-test-fixtures`
}

dependencies {
    api(libs.junit.jupiter.engine)
    api(libs.junit.platform.launcher)

    testFixturesApi(libs.junit.platform.testkit)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

testing {
    suites {
        named("test",JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit.jupiter.get())

            dependencies {
                implementation(libs.junit.platform.testkit)
            }

            targets {
                all {
                    testTask.configure {
                        options {
                            // When launching the tests via Gradle, the engine will be on the classpath as
                            // we don't want to use it, so we need to exclude it explicitly.  This
                            // value needs to match ResourceBasedTestEngine.ENGINE_ID
                            (this as JUnitPlatformOptions).excludeEngines("rbt-engine")
                        }
                    }
                }
            }
        }
    }
}

// These need to be kept in sync with the values in the Version Catalog
version = "0.1.0"
group = "org.gradle"
