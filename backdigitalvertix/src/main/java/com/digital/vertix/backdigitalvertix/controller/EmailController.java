/**
 * Paquete que contiene las clases de controlador (REST controllers) de la aplicación
 * backend digitalvertix.
 */
package com.digital.vertix.backdigitalvertix.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digital.vertix.backdigitalvertix.dto.EmailDTO;
import com.digital.vertix.backdigitalvertix.service.EmailService;

import jakarta.mail.MessagingException;

/**
 * Controlador REST para manejar las operaciones relacionadas con el envío de correos electrónicos.
 * <p>
 * Este controlador expone un endpoint para que el frontend pueda enviar mensajes de contacto
 * a una dirección de correo electrónico predefinida de la empresa.
 * </p>
 * La ruta base para todos los endpoints en este controlador es {@code /email}.
 */
@RestController
@RequestMapping("/email")
public class EmailController {

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
	 * Recibe los datos del formulario de contacto (nombre del remitente, correo del remitente,
	 * asunto y cuerpo del mensaje) a través de un objeto {@link EmailDTO}.
	 * Construye el contenido del correo y lo envía a una dirección de correo electrónico
	 * fija de la empresa, configurando el campo "Reply-To" para facilitar la respuesta
	 * directa al usuario que llenó el formulario.
	 * </p>
	 *
	 * @param emailDTO Un objeto {@link EmailDTO} que contiene:
	 * <ul>
	 * <li>{@code nombreRemitente}: El nombre de la persona que envía el mensaje.</li>
	 * <li>{@code correoRemitente}: La dirección de correo electrónico de la persona que envía el mensaje.</li>
	 * <li>{@code asunto}: El asunto del mensaje.</li>
	 * <li>{@code mensaje}: El cuerpo o contenido principal del mensaje.</li>
	 * </ul>
	 * @return Un {@link ResponseEntity} que indica el estado de la operación:
	 * <ul>
	 * <li>{@code HttpStatus.OK} (200) con un mensaje de éxito si el correo se envió correctamente.</li>
	 * <li>{@code HttpStatus.BAD_REQUEST} (400) con un mensaje de error si hubo un problema al enviar el correo.</li>
	 * </ul>
	 */
	@PostMapping("/enviarCorreoContacto")
	public ResponseEntity<?> enviarCorreoContacto(@RequestBody EmailDTO emailDTO) {
		// Dirección de correo a la que se enviará el mensaje (tu correo de soporte, por ejemplo)
		// ¡IMPORTANTE! Cambia esto por tu correo real de soporte o contacto de la empresa.
		String destinoFijoEmpresa = "veltrixdigital.co@gmail.com";

		// Construir el cuerpo del correo que recibirá tu empresa, incluyendo la información del remitente.
		String cuerpoParaEmpresa = "<h3>Nuevo mensaje de contacto</h3>"
				+ "<p><strong>Nombre:</strong> " + emailDTO.getNombreRemitente() + "</p>"
				+ "<p><strong>Correo del remitente:</strong> " + emailDTO.getCorreoRemitente() + "</p>"
				+ "<p><strong>Asunto:</strong> " + emailDTO.getAsunto() + "</p>"
				+ "<hr>"
				+ "<p><strong>Mensaje:</strong></p>"
				+ "<p>" + emailDTO.getMensaje() + "</p>"; // El mensaje es el cuerpo del correo que el usuario escribió

		// El asunto del correo que recibirá tu empresa, con un prefijo para identificarlo.
		String asuntoFinal = "Mensaje de Contacto desde Web: " + emailDTO.getAsunto();

		try {
			// Llama al servicio de correo, utilizando el método que permite especificar el remitente.
			emailService.enviarCorreoConRemitente(
				destinoFijoEmpresa,         // Destino: el correo de tu empresa
				asuntoFinal,                // Asunto: el asunto con el prefijo
				cuerpoParaEmpresa,          // Contenido: el cuerpo HTML construido
				emailDTO.getNombreRemitente(),  // Nombre del remitente del formulario
				emailDTO.getCorreoRemitente()   // Correo del remitente del formulario
			);
		} catch (MessagingException e) {
			System.err.println("Error al enviar el correo de contacto: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("message", "Error al enviar tu mensaje, por favor inténtalo de nuevo más tarde.", "success", false));
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("message", "Tu mensaje ha sido enviado exitosamente. ¡Gracias por contactarnos!", "success", true));
	}
}