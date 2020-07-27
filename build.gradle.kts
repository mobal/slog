import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("idea")
    id("jacoco")
    id("org.flywaydb.flyway") version "6.5.2"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.72"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
    id("org.sonarqube") version "2.7.1"
    id("org.springframework.boot") version "2.3.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
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
    implementation("com.fasterxml.jackson.core:jackson-core:2.11.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.github.slugify:slugify:2.4")
    implementation("com.h2database:h2")
    implementation("org.flywaydb:flyway-core")
    implementation("org.hibernate.validator:hibernate-validator:6.1.2.Final")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springdoc:springdoc-openapi-ui:1.4.3")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
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
