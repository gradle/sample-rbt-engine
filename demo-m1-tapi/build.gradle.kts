@file:Suppress("UnstableApiUsage")

plugins {
    application
}

dependencies {
    implementation(gradleApi())
}

application {
    mainClass = "org.example.Main"
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
    }
}