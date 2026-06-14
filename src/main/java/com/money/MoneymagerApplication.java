package com.money;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class MoneymagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneymagerApplication.class, args);
	}
}
