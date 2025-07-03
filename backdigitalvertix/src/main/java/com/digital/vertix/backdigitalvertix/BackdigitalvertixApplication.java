/**
 * Este paquete contiene la aplicación principal de Spring Boot para el proyecto Backdigitalvertix.
 * Es el punto de entrada para la configuración y el arranque de la API.
 */
package com.digital.vertix.backdigitalvertix;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Clase principal de la aplicación Spring Boot para Backdigitalvertix. Esta
 * clase inicializa y arranca la aplicación Spring Boot.
 */
@SpringBootApplication
public class BackdigitalvertixApplication {

	// 🔽 Esta línea fuerza la inclusión de StreamLambdaHandler.class en el JAR
	// final
	@SuppressWarnings("unused")
	private final Class<?> forceIncludeHandler = StreamLambdaHandler.class;

	/**
	 * Método principal que sirve como punto de entrada de la aplicación. Utiliza
	 * SpringApplication.run para arrancar la aplicación Spring Boot.
	 *
	 * @param args Argumentos de la línea de comandos pasados a la aplicación.
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackdigitalvertixApplication.class, args);
	}

	/**
	 * Define un bean de Spring para ModelMapper. ModelMapper es una librería que
	 * facilita el mapeo de objetos entre diferentes capas (por ejemplo, de
	 * entidades a DTOs y viceversa).
	 *
	 * @return Una nueva instancia de ModelMapper.
	 */
	@Bean
	ModelMapper getModelMapper() {
		return new ModelMapper();
	}
}