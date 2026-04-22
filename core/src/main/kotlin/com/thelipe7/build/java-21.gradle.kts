plugins {
    `java`
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks {
    compileJava {
        options.release = 21
        options.encoding = Charsets.UTF_8.name()
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}