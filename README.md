<div align="center">
  <h1>build-conventions</h1>

  <p><em>A set of Gradle plugins with common build conventions.</p>

  <p>It centralizes common settings for Java projects, such as toolchain version, encoding, `options.release`, and code formatting with Spotless.</p>

  <div>
    <a href="https://github.com/thelipe7/build-conventions/commits/main">
      <img alt="Last commit" src="https://img.shields.io/github/last-commit/thelipe7/build-conventions?style=flat-square&logo=github">
    </a>
    <a href="https://github.com/thelipe7/build-conventions/issues">
      <img alt="GitHub issues" src="https://img.shields.io/github/issues/thelipe7/build-conventions?style=flat-square&logo=github">
    </a>
    <a href="https://github.com/thelipe7/build-conventions/stargazers">
      <img alt="GitHub stars" src="https://img.shields.io/github/stars/thelipe7/build-conventions?style=flat-square&logo=github">
    </a>
    <a href="https://github.com/thelipe7/build-conventions/blob/main/LICENSE">
      <img alt="License" src="https://img.shields.io/github/license/thelipe7/build-conventions?style=flat-square">
    </a>
  </div>
</div>

## Overview

- `core`: main module that declares and publishes Gradle plugins.
- `java-8`, `java-17`, `java-21`, and `java-25`: convention plugins for Java projects in each version.
- `spotless`: convention plugin for formatting Java code, Kotlin Gradle, YAML, and various files.
- `BlankLinesInsideTypeStep`: Spotless custom step to adjust blank lines within Java types.

## Available plugins

| Plugin | Id |
| --- | --- |
| Java 8 | `com.thelipe7.build.java-8` |
| Java 17 | `com.thelipe7.build.java-17` |
| Java 21 | `com.thelipe7.build.java-21` |
| Java 25 | `com.thelipe7.build.java-25` |
| Spotless | `com.thelipe7.build.spotless` |

## Installation

> [!NOTE]\
> Replace `PLUGIN-ID` with a plugin ID available above;  
> Replace `VERSION` with a version available on the [gradle plugin portal](https://plugins.gradle.org/).

<details open>
<summary>Gradle (Kotlin DSL)</summary>

```kotlin
plugins {
    id("PLUGIN-ID") version "VERSION"
}
```
</details>

<details>
<summary>Gradle (Groovy DSL)</summary>

```groovy
plugins {
    id 'PLUGIN-ID' version 'VERSION'
}
```
</details>

## Structure

```text
.
+-- core/
|   +-- build.gradle.kts
|   +-- src/main/
|       +-- java/com/thelipe7/build/
|           +-- BlankLinesInsideTypeStep.java
|       +-- kotlin/com/thelipe7/build/
|           +-- java-8.gradle.kts
|           +-- java-17.gradle.kts
|           +-- java-21.gradle.kts
|           +-- java-25.gradle.kts
|           +-- spotless.gradle.kts
+-- gradle/wrapper/
|   +-- gradle-wrapper.jar
|   +-- gradle-wrapper.properties
+-- gradle.properties
+-- settings.gradle.kts
+-- README.md
```

## License
This project is licensed under the Apache License 2.0.

See [LICENSE](LICENSE) for details.
