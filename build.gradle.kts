import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.0.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.72"
	kotlin("plugin.allopen") version "1.3.72"
}


allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.Embeddable")
	annotation("javax.persistence.MappedSuperclass")
}


group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	// Basic spring boot
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Data
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql")

	// HATEOAS links
	implementation("org.springframework.boot:spring-boot-starter-hateoas")

	// Security
	implementation("org.springframework.ldap:spring-ldap-core")
	implementation("org.springframework.security:spring-security-ldap")
	implementation("com.unboundid:unboundid-ldapsdk")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("io.jsonwebtoken:jjwt:0.9.1")

	// (De)serialization
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.11.0")

	// Kotlin support
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// Validation
	implementation("org.hibernate:hibernate-validator:6.1.5.Final")

	// API documentation
	implementation("org.springdoc:springdoc-openapi-ui:1.4.1")
	implementation("org.springdoc:springdoc-openapi-security:1.4.1")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.4.1")

	// Dev-time features
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude(module = "mockito-core")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("com.ninja-squad:springmockk:1.1.3")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
