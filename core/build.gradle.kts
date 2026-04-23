plugins {
    `java`
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "2.1.1"
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("com.diffplug.spotless:spotless-plugin-gradle:8.4.0")
}

gradlePlugin {
    website = "https://github.com/thelipe7/build-conventions"
    vcsUrl = "https://github.com/thelipe7/build-conventions.git"
    plugins {
        named("java-25") {
            id = "com.thelipe7.build.java-25"
            displayName = "Java 25 Build Conventions"
            description = "Build conventions plugin for Java 25 projects"
            tags = listOf("java21", "build", "conventions")
        }
        named("java-21") {
            id = "com.thelipe7.build.java-21"
            displayName = "Java 21 Build Conventions"
            description = "Build conventions plugin for Java 21 projects"
            tags = listOf("java21", "build", "conventions")
        }
        named("java-17") {
            id = "com.thelipe7.build.java-17"
            displayName = "Java 17 Build Conventions"
            description = "Build conventions plugin for Java 17 projects"
            tags = listOf("java17", "build", "conventions")
        }
        named("java-8") {
            id = "com.thelipe7.build.java-8"
            displayName = "Java 8 Build Conventions"
            description = "Build conventions plugin for Java 8 projects"
            tags = listOf("java8", "build", "conventions")
        }
        named("spotless") {
            id = "com.thelipe7.build.spotless"
            displayName = "Spotless Build Conventions"
            description = "Build conventions plugin for Spotless projects"
            tags = listOf("spotless", "build", "conventions")
        }
    }
}

