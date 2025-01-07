package com.nt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
	    info = @Info(
	        title = "My API",
	        version = "v1",
	        description = "API documentation"
	    )
	)
@SpringBootApplication
public class RewardSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RewardSystemApplication.class, args);
	}

}
