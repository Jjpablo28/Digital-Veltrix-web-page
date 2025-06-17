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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digital.vertix.backdigitalvertix.dto.EmailDTO;
import com.digital.vertix.backdigitalvertix.service.EmailService;

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
@CrossOrigin(origins = { "*" })

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
	 * Endpoint para enviar un correo de contacto desde el frontend.
	 * <p>
	 * Recibe los datos del formulario de contacto (nombre del remitente, correo del
	 * remitente, asunto y cuerpo del mensaje) a través de un objeto
	 * {@link EmailDTO}. Construye el contenido del correo y lo envía a una
	 * dirección de correo electrónico fija de la empresa, configurando el campo
	 * "Reply-To" para facilitar la respuesta directa al usuario que llenó el
	 * formulario.
	 * </p>
	 *
	 * @param emailDTO Un objeto {@link EmailDTO} que contiene:
	 *                 <ul>
	 *                 <li>{@code nombreRemitente}: El nombre de la persona que
	 *                 envía el mensaje.</li>
	 *                 <li>{@code correoRemitente}: La dirección de correo
	 *                 electrónico de la persona que envía el mensaje.</li>
	 *                 <li>{@code asunto}: El asunto del mensaje.</li>
	 *                 <li>{@code mensaje}: El cuerpo o contenido principal del
	 *                 mensaje.</li>
	 *                 </ul>
	 * @return Un {@link ResponseEntity} que indica el estado de la operación:
	 *         <ul>
	 *         <li>{@code HttpStatus.OK} (200) con un mensaje de éxito si el correo
	 *         se envió correctamente.</li>
	 *         <li>{@code HttpStatus.BAD_REQUEST} (400) con un mensaje de error si
	 *         hubo un problema al enviar el correo.</li>
	 *         </ul>
	 */
	@PostMapping("/enviarCorreoContacto")
	public ResponseEntity<?> enviarCorreoContacto(@RequestBody EmailDTO emailDTO) {
		// Dirección de correo a la que se enviará el mensaje (tu correo de soporte, por
		// ejemplo)
		String destinoFijoEmpresa = "veltrixdigital.co@gmail.com";

		// --- Validaciones manuales ---
		Map<String, String> errors = new HashMap<>();

		if (emailDTO.getNombreRemitente() == null || emailDTO.getNombreRemitente().trim().isEmpty()) {
			errors.put("nombreRemitente", "El nombre del remitente no puede estar vacío.");
		}
		if (emailDTO.getCorreoRemitente() == null || emailDTO.getCorreoRemitente().trim().isEmpty()) {
			errors.put("correoRemitente", "El correo del remitente no puede estar vacío.");
		} else {
			// Validar formato del correo electrónico usando la expresión regular
			Matcher matcher = EMAIL_PATTERN.matcher(emailDTO.getCorreoRemitente());
			if (!matcher.matches()) {
				errors.put("correoRemitente", "El formato del correo electrónico no es válido.");
			}
		}
		if (emailDTO.getAsunto() == null || emailDTO.getAsunto().trim().isEmpty()) {
			errors.put("asunto", "El asunto no puede estar vacío.");
		}
		if (emailDTO.getMensaje() == null || emailDTO.getMensaje().trim().isEmpty()) {
			errors.put("mensaje", "El mensaje no puede estar vacío.");
		}
		// Puedes añadir una validación de longitud mínima para el mensaje si lo deseas
		if (emailDTO.getMensaje() != null && emailDTO.getMensaje().trim().length() < 2) {
			errors.put("mensaje", "El mensaje debe tener al menos 10 caracteres.");
		}

		// Si hay errores, devolver la respuesta con los detalles de la validación
		// fallida
		if (!errors.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("message", "Error de validación", "errors", errors, "success", false));
		}
		// --- Fin de validaciones manuales ---

		// Construir el cuerpo del correo que recibirá tu empresa, incluyendo la
		// información del remitente.
		String cuerpoParaEmpresa = "<h3>Nuevo mensaje de contacto</h3>" + "<p><strong>Nombre:</strong> "
				+ emailDTO.getNombreRemitente() + "</p>" + "<p><strong>Correo del remitente:</strong> "
				+ emailDTO.getCorreoRemitente() + "</p>" + "<p><strong>Asunto:</strong> " + emailDTO.getAsunto()
				+ "</p>" + "<hr>" + "<p><strong>Mensaje:</strong></p>" + "<p>" + emailDTO.getMensaje() + "</p>";

		// El asunto del correo que recibirá tu empresa, con un prefijo para
		// identificarlo.
		String asuntoFinal = "Mensaje de Contacto desde Web: " + emailDTO.getAsunto();

		try {
			emailService.enviarCorreoConRemitente(destinoFijoEmpresa, asuntoFinal, cuerpoParaEmpresa,
					emailDTO.getNombreRemitente(), emailDTO.getCorreoRemitente());
		} catch (MessagingException e) {
			System.err.println("Error al enviar el correo de contacto: " + e.getMessage());
			// Error al enviar el correo debido a un problema interno del servidor de correo
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message",
					"Error al enviar tu mensaje, por favor inténtalo de nuevo más tarde.", "success", false));
		}

		// Éxito al enviar el correo
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("message",
				"Tu mensaje ha sido enviado exitosamente. ¡Gracias por contactarnos!", "success", true));
	}
}