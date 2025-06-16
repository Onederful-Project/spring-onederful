package com.example.onederful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OnederfulApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnederfulApplication.class, args);
	}

}
