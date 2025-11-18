@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// Demos for M1
include("demo-m1", "demo-m1-tapi")
// Demos for M2 & 3
include("demo-m2-3", "demo-m2-3-tapi")

rootProject.name = "sample-rbt-engine"
