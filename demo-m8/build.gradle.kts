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
                implementation(project(":engine"))
            }

            targets {
                create("excludeMoreDefs").testTask.configure {
                    filter {
                        excludeTestsMatching(".*/more-definitions/.*")
                    }
                }
                create("includeOnlyMoreDefs").testTask.configure {
                    filter {
                        includeTestsMatching(".*/more-definitions/.*")
                    }
                }
                create("includeOnlyNumericTestsNotInASubDir").testTask.configure {
                    filter {
                        includeTestsMatching(".*/tests-\\d.xml")
                        excludeTestsMatching(".*/sub.*/.*")
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
