plugins {
    `application`
    kotlin("jvm") version "2.2.21"
}

dependencies {
    implementation("org.gradle:gradle-tooling-api:9.4.0-20251120104804+0000")
    implementation(libs.slf4j.simple)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass.set("sample.M7ToolingClient")
}
