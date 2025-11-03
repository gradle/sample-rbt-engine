@file:Suppress("UnstableApiUsage")

plugins {
    id("java-library")
    id("rbt-plugin")
}

testing.suites {
    named<JvmTestSuite>("test") {
        useJUnitJupiter()

        dependencies {
            implementation("org.gradle:engine:0.1.0")
        }

        targets.all {
            testTask.configure {
                scanForTestDefinitions = true
                testDefinitionDirs.setFrom(project.layout.projectDirectory.file("src/test/testDefinitions"))

                options {
                    this as JUnitPlatformOptions
                    includeEngines("rbt-engine")
                    includeEngines("junit-jupiter")
                }
            }
        }
    }
}
