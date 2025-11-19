@file:Suppress("UnstableApiUsage")

import java.net.URI


dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven {
            url = URI("https://repo.gradle.org/artifactory/libs-snapshots")
        }
    }

    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(
    "engine",
    "demo-m7",
    "demo-m7-client"
)

rootProject.name = "sample-rbt-engine"
