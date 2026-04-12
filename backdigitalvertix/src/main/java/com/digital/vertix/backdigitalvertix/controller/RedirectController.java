package com.digital.vertix.backdigitalvertix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para redirigir el tráfico de la URL raíz (/) directamente a la
 * documentación interactiva de Swagger UI.
 */
@Controller
public class RedirectController {

	@GetMapping("/")
	public String redirectToSwagger() {
		// Redirige automáticamente a la pantalla interactiva de la API
		return "redirect:/swagger-ui/index.html";
	}
}