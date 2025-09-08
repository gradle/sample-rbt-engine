plugins {
    `java-library`
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    testRuntimeOnly(libs.findLibrary("sample.rbt.engine").get())
}

tasks.named<UpdateDaemonJvm>("updateDaemonJvm") {
    languageVersion = JavaLanguageVersion.of(21)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}