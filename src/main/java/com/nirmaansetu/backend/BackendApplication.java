package com.nirmaansetu.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Nirmaan Setu Backend application.
 * Enables various Spring Boot features like Async, Caching, Scheduling, and JPA Auditing.
 */
@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableScheduling
@EnableJpaAuditing
public class BackendApplication {

	/**
	 * Main method to launch the Spring Boot application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
