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

        create("excludeMoreDateTests", JvmTestSuite::class) {
            targets.all {
                testTask.configure {
                    testDefinitionDirs.from("src/test/definitions")
                    testDefinitionDirs.from("src/test/more-definitions")

                    filter {
                        excludeTestsMatching(".*/more-date-tests.rbt")
                    }
                }
            }
        }

        create("includeOnlyMoreDateTests", JvmTestSuite::class) {
            targets.all {
                testTask.configure {
                    testDefinitionDirs.from("src/test/definitions")
                    testDefinitionDirs.from("src/test/more-definitions")

                    filter {
                        includeTestsMatching(".*/more-date-tests.rbt")
                    }
                }
            }
        }

        create("includeOnlyDateTestsButExcludeMoreDateTestsAndSubDir", JvmTestSuite::class) {
            useJUnitJupiter()

            dependencies {
                implementation(project(":hierarchical-engine"))
            }

            targets.all {
                testTask.configure {
                    testDefinitionDirs.from("src/test/definitions")
                    testDefinitionDirs.from("src/test/more-definitions")

                    filter {
                        includeTestsMatching(".*/.*date-tests.*")
                        excludeTestsMatching(".*/more.*/.*")
                        excludeTestsMatching(".*/sub/.*")
                    }
                }
            }
        }
    }
}
