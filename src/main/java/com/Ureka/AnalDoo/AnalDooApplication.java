package com.Ureka.AnalDoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AnalDooApplication {

	public static void main(String[] args) {

		SpringApplication.run(AnalDooApplication.class, args);
	}

}
