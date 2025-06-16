/**
 * Este paquete contiene las clases de configuración específicas para la aplicación Backdigitalvertix.
 * Aquí se definen configuraciones generales, como CORS, seguridad, etc.
 */
package com.digital.vertix.backdigitalvertix.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase de configuración para manejar las políticas de Cross-Origin Resource Sharing (CORS).
 * Implementa {@link WebMvcConfigurer} para personalizar la configuración MVC por defecto
 * y así permitir solicitudes de origen cruzado a los endpoints de la API.
 * <p>
 * Esta configuración permite que cualquier origen ({@code *}) acceda a cualquier endpoint
 * ({@code /**}) de la aplicación, utilizando cualquier método HTTP permitido (GET, POST, PUT, DELETE, etc.).
 * Es útil para entornos de desarrollo, pero se recomienda configurar orígenes específicos
 * en entornos de producción por motivos de seguridad.
 * </p>
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	/**
	 * Configura las reglas de mapeo CORS para la aplicación.
	 * Este método sobrescribe el comportamiento por defecto de Spring MVC
	 * para definir qué orígenes, métodos y encabezados están permitidos
	 * para las solicitudes de origen cruzado.
	 *
	 * @param registry El {@link CorsRegistry} para configurar las reglas CORS.
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // Aplica la configuración CORS a todos los endpoints de la API
				.allowedOrigins("*") // Permite solicitudes desde cualquier origen.
									// Considera especificar dominios en producción.
				.allowedMethods("*"); // Permite todos los métodos HTTP (GET, POST, PUT, DELETE, etc.).
	}
}