/**
 * Paquete que contiene las clases de controlador (REST controllers) de la aplicación
 * backend digitalvertix.
 */
package com.digital.vertix.backdigitalvertix.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digital.vertix.backdigitalvertix.dto.EmailDTO;
import com.digital.vertix.backdigitalvertix.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import jakarta.mail.MessagingException;

/**
 * Controlador REST para manejar las operaciones relacionadas con el envío de
 * correos electrónicos.
 * <p>
 * Este controlador expone un endpoint para que el frontend pueda enviar
 * mensajes de contacto a una dirección de correo electrónico predefinida de la
 * empresa.
 * </p>
 * La ruta base para todos los endpoints en este controlador es {@code /email}.
 */
@RestController
@RequestMapping("/email")

public class EmailController {
	/**
	 * Expresión regular (regex) que define el patrón para una dirección de correo
	 * electrónico válida. Esta regex permite caracteres alfanuméricos, guiones
	 * bajos, signos más, ampersands, asteriscos y guiones en la parte del nombre de
	 * usuario. También soporta múltiples subdominios y extensiones de dominio de 2
	 * a 7 caracteres, cubriendo la mayoría de los formatos de correo electrónico
	 * comunes.
	 */
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"
			+ "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

	/**
	 * Objeto {@code Pattern} compilado a partir de la expresión regular
	 * {@code EMAIL_REGEX}. Este objeto se utiliza para realizar validaciones
	 * eficientes de cadenas de correo electrónico, permitiendo verificar si una
	 * cadena dada coincide con el formato esperado de un correo electrónico. La
	 * compilación se realiza una sola vez para optimizar el rendimiento.
	 */
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	/**
	 * Servicio para el envío de correos electrónicos. Esta dependencia está
	 * gestionada por Spring y permite enviar emails desde la aplicación.
	 */
	@Autowired
	private EmailService emailService;

	/**
	 * Constructor por defecto de {@code EmailController}.
	 * <p>
	 * Este constructor es requerido por Spring Framework para la creación de beans.
	 */
	public EmailController() {
		// Constructor por defecto
	}

	/**
	 * Endpoint para recibir y procesar los mensajes del formulario de contacto web.
	 * <p>
	 * Este método valida automáticamente los datos entrantes utilizando las reglas
	 * definidas en {@link EmailDTO}. Si la validación es exitosa, construye un
	 * correo electrónico en formato HTML y lo envía a la bandeja de entrada de la
	 * empresa.
	 * </p>
	 *
	 * @param emailDTO      Objeto que contiene los datos enviados desde el frontend
	 *                      (nombre, correo, asunto y mensaje). Se valida mediante
	 *                      {@code @Valid}.
	 * @param bindingResult Objeto inyectado por Spring que captura y contiene el
	 *                      resultado de la validación del {@code emailDTO}.
	 * @return Un {@link ResponseEntity} que contiene un JSON con el resultado de la
	 *         operación:
	 *         <ul>
	 *         <li><b>200 OK:</b> Si el correo se envió correctamente
	 *         ({@code success: true}).</li>
	 *         <li><b>400 BAD REQUEST:</b> Si los datos del formulario no cumplen
	 *         con las reglas de validación. Incluye un mapa con los campos que
	 *         fallaron y sus motivos ({@code success: false}).</li>
	 *         <li><b>500 INTERNAL SERVER ERROR:</b> Si ocurre una excepción al
	 *         intentar comunicarse con el servidor de correos
	 *         ({@code success: false}).</li>
	 *         </ul>
	 */
	@PostMapping("/enviarCorreoContacto")
	public ResponseEntity<?> enviarCorreoContacto(@Valid @RequestBody EmailDTO emailDTO, BindingResult bindingResult) {
		String destinoFijoEmpresa = "veltrixdigital.co@gmail.com";

		// Si las reglas del DTO fallan, Spring lo detecta aquí automáticamente
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("message", "Error de validación", "errors", errors, "success", false));
		}

		String cuerpoParaEmpresa = "<h3>Nuevo mensaje de contacto</h3>" + "<p><strong>Nombre:</strong> "
				+ emailDTO.getNombreRemitente() + "</p>" + "<p><strong>Correo del remitente:</strong> "
				+ emailDTO.getCorreoRemitente() + "</p>" + "<p><strong>Asunto:</strong> " + emailDTO.getAsunto()
				+ "</p>" + "<hr>" + "<p><strong>Mensaje:</strong></p>" + "<p>" + emailDTO.getMensaje() + "</p>";

		String asuntoFinal = "Mensaje de Contacto desde Web: " + emailDTO.getAsunto();

		try {
			emailService.enviarCorreoConRemitente(destinoFijoEmpresa, asuntoFinal, cuerpoParaEmpresa,
					emailDTO.getNombreRemitente(), emailDTO.getCorreoRemitente());
		} catch (MessagingException e) {
			System.err.println("Error al enviar el correo de contacto: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message",
					"Error al enviar tu mensaje, por favor inténtalo de nuevo más tarde.", "success", false));
		}

		return ResponseEntity.status(HttpStatus.OK).body(Map.of("message",
				"Tu mensaje ha sido enviado exitosamente. ¡Gracias por contactarnos!", "success", true));
	}
}