plugins {
	`kotlin-dsl`
}

dependencies {
	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.VERSION}")
	implementation("org.jetbrains.kotlin:kotlin-allopen:${Kotlin.VERSION}")

	// Spring Boot
	implementation("org.springframework.boot:spring-boot-gradle-plugin:${Spring.BOOT_VERSION}")
}

kotlin {
	sourceSets.getByName("main").kotlin.srcDir("buildSrc/src/main/kotlin")
}