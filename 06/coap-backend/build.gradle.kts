plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "hu.bme.aut.android.coap"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("org.hsqldb:hsqldb")
	implementation("org.eclipse.californium:californium-core:3.12.1")
	implementation("com.google.code.gson:gson:2.11.0")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
