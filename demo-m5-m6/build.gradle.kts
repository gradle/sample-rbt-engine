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

            targets.all {
                testTask.configure {
                    maxParallelForks = 2
                    forkEvery = 1
                    testDefinitionDirs.from("src/test/definitions")
                    testDefinitionDirs.from("src/test/more-definitions")
                }
            }
        }
    }
}
