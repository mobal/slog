import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.adarshr.test-logger") version "2.1.0"
    id("idea")
    id("jacoco")
    id("org.flywaydb.flyway") version "7.0.2"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.4.10"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("org.sonarqube") version "3.0"
    id("org.springframework.boot") version "2.3.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.spring") version "1.4.10"
}

group = "hu.netcode"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    all {
        exclude(group = "org.mockito")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.amazonaws:aws-java-sdk-s3:1.11.841")
    implementation("com.fasterxml.jackson.core:jackson-core:2.11.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.github.slugify:slugify:2.4")
    implementation("com.h2database:h2")
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("org.flywaydb:flyway-core")
    implementation("org.hibernate.validator:hibernate-validator:6.1.2.Final")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springdoc:springdoc-openapi-ui:1.4.3")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.data:spring-data-rest-core")
    implementation("org.springframework.security:spring-security-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("spring.profiles.active", "test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

// TODO: Configure flyway properly!
flyway {
    password = "password"
    user = "sa"
    url = "jdbc:h2:mem:mydb"
}

jacoco {
    toolVersion = "0.8.5"
}

sonarqube {
    properties {
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.java.binaries", "build/classes")
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.jacoco.reportPath", "build/jacoco/test.exec")
        property("sonar.junit.reportPath", "build/test-results/test")
        property("sonar.language", "kotlin")
        property("sonar.sources", "src/main/kotlin")
    }
}

testlogger {
    setTheme("mocha-parallel")
    showStackTraces = true
}
