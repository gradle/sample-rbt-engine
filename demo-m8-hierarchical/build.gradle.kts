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
        named("test", JvmTestSuite::class) {
            useJUnitJupiter()

            dependencies {
                implementation(project(":hierarchical-engine"))
            }

            targets {
                create("excludeMoreDateTests").testTask {
                    filter {
                        excludeTestsMatching(".*/more-date-tests.rbt")
                    }
                }

                create("includeOnlyMoreDateTests").testTask {
                    filter {
                        includeTestsMatching(".*/more-date-tests.rbt")
                    }
                }

                create("includeOnlyDateTestsButExcludeMoreDateTestsAndSubDir").testTask {
                    filter {
                        includeTestsMatching(".*/.*date-tests.*")
                        excludeTestsMatching(".*/more.*")
                        excludeTestsMatching(".*/sub/.*")
                    }
                }

                all {
                    testTask.configure {
                        testDefinitionDirs.from("src/test/definitions")
                        testDefinitionDirs.from("src/test/more-definitions")
                    }
                }
            }
        }
    }
}
