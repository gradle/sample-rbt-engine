@file:Suppress("UnstableApiUsage")

plugins {
    `java-library`
    `java-test-fixtures`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    api(libs.junit.jupiter.engine)

    implementation("org.slf4j:jul-to-slf4j:2.0.9")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.20")

    testFixturesApi(libs.junit.platform.testkit)
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
