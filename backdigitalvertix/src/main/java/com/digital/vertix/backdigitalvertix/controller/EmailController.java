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

@RestController
@RequestMapping("/email")
public class EmailController {

	/**
	 * Servicio para el envío de correos electrónicos. Esta dependencia está
	 * gestionada por Spring y permite enviar emails desde la aplicación.
	 */
	@Autowired
	private EmailService emailService;

	public EmailController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Endpoint para enviar un código de confirmación al correo electrónico
	 * proporcionado.
	 *
	 * @param emailDTO Un objeto {@link EmailDTO} que contiene la dirección de
	 *                 correo electrónico de destino y el código de verificación.
	 * @return Un {@link ResponseEntity} que indica el estado de la operación de
	 *         envío del correo. Devuelve un estado 200 OK si el correo se envió
	 *         correctamente, o un estado 400 BAD REQUEST si hubo un error al enviar
	 *         el correo.
	 */
	@PostMapping("/enviarCodigoConfirmación")
	public ResponseEntity<?> enviar(@RequestBody EmailDTO emailDTO) {

		String destino = emailDTO.getDestino();
		String asunto = "Codigo de Verificacion";
		String codigo = emailDTO.getMensaje();
		String asuntoCodigo = "Bienvenido a Virus Detected."
				+ "<p>A continuación recibiras el código de autenticación para poder hacer uso de nuestra página</p>"
				+ "\n" + "\n<p>Código: <b>" + codigo
				+ "</b></p>\nEste código solo es válido por una vez y es único, no lo compartas con nadie.<p></p><p></p>"
				+ "<img src=\"https://media1.tenor.com/m/0_rWNXlW6OkAAAAd/oso-bailando.gif\" style=\"width: 200px; height: 200px;\" />\r\n";

		try {
			emailService.enviarCorreo(destino, asunto, asuntoCodigo);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("message", "Error al enviar el correo, intente nuevamente", "success", false));

		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("message", "Correo enviado satisfactoriamente", "success", true));

	}

	/**
	 * Endpoint para recibir un correo electrónico a través de una solicitud POST.
	 * Este método toma un objeto EmailDTO en el cuerpo de la solicitud, extrae la
	 * información del correo (asunto, destinatario del usuario, mensaje), y luego
	 * intenta enviar este correo a una dirección de destino predefinida
	 * ("virusdetected.contact@gmail.com").
	 *
	 * @param emailDTO Un objeto EmailDTO que contiene los detalles del correo
	 *                 electrónico enviado por el usuario (asunto, destinatario,
	 *                 mensaje).
	 * @return Una respuesta ResponseEntity que indica el resultado del intento de
	 *         envío del correo: - Si el correo se envía exitosamente, retorna un
	 *         estado 200 (OK) con un mapa JSON que contiene un mensaje de éxito y
	 *         un indicador 'success' en true. - Si ocurre una excepción durante el
	 *         envío del correo (MessagingException), retorna un estado 400
	 *         (BAD_REQUEST) con un mapa JSON que contiene un mensaje de error y un
	 *         indicador 'success' en false.
	 */
	@PostMapping("/recibirCorreo")
	public ResponseEntity<?> recibir(@RequestBody EmailDTO emailDTO) {

		String destino = "virusdetected.contact@gmail.com";
		String asunto = emailDTO.getAsunto();
		String correoUsuario = emailDTO.getDestino();
		String mensaje = "<b>De: </b>" + correoUsuario + "<p><b>\nAsunto:</b> " + emailDTO.getMensaje() + "</p>";

		try {
			emailService.enviarCorreo(destino, asunto, mensaje);
		} catch (MessagingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("message", "Error al enviar el correo, intente nuevamente", "success", false));

		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of("message", "Correo enviado satisfactoriamente", "success", true));

	}
}
