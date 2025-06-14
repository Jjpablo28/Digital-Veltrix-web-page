package com.digital.vertix.backdigitalvertix;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackdigitalvertixApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackdigitalvertixApplication.class, args);
	}
	
	@Bean
    ModelMapper getModelMapper() {
		return new ModelMapper();
	}

}
