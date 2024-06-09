package com.bank.bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Bank App",
				description = "Backend Rest API for Bank",
				version = "v1.0",
				contact = @Contact(
						name = "Justin Fok",
						email = "justinfok1@gmail.com",
						url = "http://github.com/JustinFok1"
				),
				license = @License(
						name = "License",
						url = "http://github.com/JustinFok1"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Bank Application Documentation",
				url = "http://github.com/JustinFok1"
		)
)
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}
