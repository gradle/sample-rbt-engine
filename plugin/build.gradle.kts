plugins {
    id("java-lib-conventions")
    `java-gradle-plugin`
}

dependencies {
    implementation(project(":engine"))
    implementation(libs.asm)
}

gradlePlugin {
    plugins {
        create("rbt-plugin") {
            id = "rbt-plugin"
            implementationClass = "org.gradle.engine.rbt.plugin.ResourceBasedTestingPlugin"
        }
    }
}
