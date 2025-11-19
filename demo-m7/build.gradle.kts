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
        register<JvmTestSuite>("customTest") {
            useJUnitJupiter()

            dependencies {
                implementation(project(":engine"))
            }

            targets.all {
                testTask.configure {
                    testDefinitionDirs.from("src/customTest/definitions")
                }
            }
        }

        named("test", JvmTestSuite::class) {
            useJUnitJupiter()

            dependencies {
                implementation(project(":engine"))
            }

            targets.all {
                testTask.configure {
                    testDefinitionDirs.from("src/test/definitions")
                }
            }
        }
    }
}
