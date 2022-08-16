package com.cafehub.cafehubspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CafehubSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafehubSpringApplication.class, args);
	}

}
