plugins {
    id("java-lib-conventions")
    id("rbt-plugin")
}

tasks.named<org.gradle.engine.rbt.plugin.task.ScanResourcesTask>("scanForTests") {
    inputDir = project.layout.projectDirectory.dir("src/test/testDefinitions")
}