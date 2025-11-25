@file:Suppress("UnstableApiUsage")

plugins {
    `java-library`
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
        configureEach {
            this as JvmTestSuite
            useJUnitJupiter()

            dependencies {
                implementation(project(":engine"))
            }
        }

        named("test", JvmTestSuite::class) {
            targets.all {
                testTask.configure {
                    testDefinitionDirs.from("src/test/definitions")
                    testDefinitionDirs.from("src/test/more-definitions")
                }
            }
        }

        create("excludeMoreDefs", JvmTestSuite::class) {
            targets.all {
                testTask.configure {
                    testDefinitionDirs.from("src/test/definitions")
                    testDefinitionDirs.from("src/test/more-definitions")

                    filter {
                        excludeTestsMatching(".*/more-definitions/.*")
                    }
                }
            }
        }

        create("includeOnlyMoreDefs", JvmTestSuite::class) {
            targets.all {
                testTask.configure {
                    testDefinitionDirs.from("src/test/definitions")
                    testDefinitionDirs.from("src/test/more-definitions")

                    filter {
                        includeTestsMatching(".*/more-definitions/.*")
                    }
                }
            }
        }

        create("includeOnlyNumericTestsNotInASubDir", JvmTestSuite::class) {
            targets.all {
                testTask.configure {
                    testDefinitionDirs.from("src/test/definitions")
                    testDefinitionDirs.from("src/test/more-definitions")

                    filter {
                        includeTestsMatching(".*/tests-\\d.xml")
                        excludeTestsMatching(".*/sub.*/.*")
                    }
                }
            }
        }
    }
}
