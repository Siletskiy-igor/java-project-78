import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java")
    application
    jacoco
    id("checkstyle")
    id("io.freefair.lombok") version "8.6"
    id("com.github.ben-manes.versions") version "0.50.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application { mainClass.set("hexlet.code.App") }


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation("org.apache.commons:commons-collections4:4.4")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        // showStackTraces = true
        // showCauses = true
        showStandardStreams = true
    }
}

jacoco {
    toolVersion = "0.8.10"
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)  // Запуск отчета после тестов
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "sun/**",
                    "com/sun/**",
                    "jdk/**",
                    "java/**",
                    "javax/**"
                )
            }
        })
    )
}