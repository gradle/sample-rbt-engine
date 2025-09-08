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

testing {
    suites {
        named("test",JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit.jupiter.get())

            dependencies {
                implementation(libs.junit.platform.testkit)
                implementation(testFixtures(project()))
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

version = "0.1.0"
group = "org.gradle"
