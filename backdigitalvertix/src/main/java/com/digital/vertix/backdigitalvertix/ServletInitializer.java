/**
 * Este paquete contiene las clases principales de la aplicación para Backdigitalvertix.
 * Incluye la configuración de Spring Boot y componentes esenciales
 * para el despliegue y arranque de la aplicación.
 */
package com.digital.vertix.backdigitalvertix;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Este paquete contiene las clases principales de la aplicación para Backdigitalvertix.
 * Incluye la configuración de Spring Boot y componentes esenciales.
 */
// Javadoc para la clase ServletInitializer
/**
 * Clase de inicialización de servlets para la aplicación Spring Boot. Esta
 * clase es necesaria cuando se despliega la aplicación como un archivo WAR en
 * un servidor de aplicaciones tradicional (como Tomcat). Extiende
 * {@link SpringBootServletInitializer} para configurar la aplicación y permitir
 * que se ejecute dentro del contenedor de servlets.
 */
public class ServletInitializer extends SpringBootServletInitializer {
	/**
	 * Configura la aplicación Spring Boot. Este método se sobrescribe para
	 * especificar la clase principal de la aplicación
	 * ({@link BackdigitalvertixApplication}) que Spring Boot debe ejecutar.
	 *
	 * @param application La instancia de {@link SpringApplicationBuilder} para
	 *                    configurar.
	 * @return La instancia de {@link SpringApplicationBuilder} configurada con la
	 *         fuente de la aplicación.
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BackdigitalvertixApplication.class);
	}

}
