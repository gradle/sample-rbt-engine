@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}

pluginManagement {
    includeBuild("./build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include("engine", "plugin")

rootProject.name = "sample-rbt-engine"
