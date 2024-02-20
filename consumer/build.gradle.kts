plugins {
	id("spring-core")
}

dependencies {
	implementation(project(":core"))
	implementation("org.springframework.kafka:spring-kafka")
//
//	testImplementation("org.springframework.boot:spring-boot-testcontainers")
//	testImplementation("org.springframework.kafka:spring-kafka-test")
//	testImplementation("org.testcontainers:junit-jupiter")
//	testImplementation("org.testcontainers:kafka")
}