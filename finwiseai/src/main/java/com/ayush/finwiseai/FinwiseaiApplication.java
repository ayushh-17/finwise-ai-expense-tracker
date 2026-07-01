package com.ayush.finwiseai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinwiseaiApplication {
	public static void main(String[] args) {
		String dbUrl = System.getenv("DB_URL");
		String dbUsername = System.getenv("DB_USERNAME");
		String dbPassword = System.getenv("DB_PASSWORD");

		System.out.println("===== ENV DEBUG START =====");
		System.out.println("DB_URL: [" + dbUrl + "] Length: " + (dbUrl != null ? dbUrl.length() : "NULL"));
		System.out.println("DB_USERNAME: [" + dbUsername + "]");
		System.out.println("DB_PASSWORD length: " + (dbPassword != null ? dbPassword.length() : "NULL"));
		System.out.println("===== ENV DEBUG END =====");

		SpringApplication.run(FinwiseaiApplication.class, args);
	}
}