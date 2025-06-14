/**
 * Paquete que contiene las clases de servicio de la aplicación digitalvertix
 * backend.
 */
package com.digital.vertix.backdigitalvertix.service;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio encargado del envío de correos electrónicos dentro de la aplicación.
 * <p>
 * Utiliza la interfaz {@link JavaMailSender} de Spring para facilitar la
 * composición y el envío de mensajes de correo electrónico, incluyendo la
 * capacidad de enviar contenido HTML.
 */
@Service
public class EmailService {
	/**
	 * Inyección de dependencia del objeto {@link JavaMailSender} configurado para
	 * enviar correos electrónicos.
	 */
	@Autowired
	private JavaMailSender mailSender;

	/**
	 * Envía un correo electrónico al destino especificado con el asunto y el
	 * contenido HTML proporcionados.
	 *
	 * @param destino       La dirección de correo electrónico del destinatario.
	 * @param asunto        El asunto del correo electrónico.
	 * @param contenidoHtml El contenido del correo electrónico en formato HTML.
	 * @throws MessagingException Si ocurre un error durante la creación o el envío
	 *                            del mensaje.
	 */
	public void enviarCorreo(String destino, String asunto, String contenidoHtml) throws MessagingException {
		// Este método se mantiene para compatibilidad con tus otros endpoints
		// donde no necesitas la información explícita del remitente.
		MimeMessage mensaje = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

		helper.setTo(destino);
		helper.setSubject(asunto);
		helper.setFrom("virusdetected.contact@gmail.com"); // Correo del remitente configurado en tu aplicación

		helper.setText(contenidoHtml, true);

		mailSender.send(mensaje);
	}

	/**
	 * Envía un correo electrónico, permitiendo especificar el nombre y el correo
	 * del remitente. Esto es útil para formularios de contacto donde el correo
	 * saliente debe reflejar la identidad del usuario que lo envía.
	 *
	 * @param destino         La dirección de correo electrónico del destinatario
	 *                        final (ej. tu correo de soporte).
	 * @param asunto          El asunto del correo electrónico.
	 * @param contenidoHtml   El contenido del correo electrónico en formato HTML.
	 * @param remitenteNombre El nombre de la persona que envía el mensaje (ej.
	 *                        desde el formulario).
	 * @param remitenteCorreo La dirección de correo electrónico de la persona que
	 *                        envía el mensaje.
	 * @throws MessagingException Si ocurre un error durante la creación o el envío
	 *                            del mensaje.
	 */
	public void enviarCorreoConRemitente(String destino, String asunto, String contenidoHtml, String remitenteNombre,
			String remitenteCorreo) throws MessagingException {
		MimeMessage mensaje = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

		helper.setTo(destino);
		helper.setSubject(asunto);
		// Establece el "From" del correo. Aunque el servidor SMTP usará su propia
		// cuenta de envío,
		// esto ayuda a que los clientes de correo muestren "Enviado por:
		// [remitenteCorreo]" o similar.
		// Para que el campo "reply-to" funcione, el "from" debe ser un correo válido
		// configurado en el servidor SMTP.
		// Lo más común es usar tu propio correo configurado en Spring Boot para 'from'
		// y luego usar 'replyTo' o incluir la información del remitente en el cuerpo.
		helper.setFrom("veltrixdigital.co@gmail.com"); // Tu correo configurado en application.properties para enviar

		// Opcional: Configurar el campo Reply-To, para que al responder, se envíe al
		// correo del remitente del formulario
		// Esto es muy útil para formularios de contacto.
		if (remitenteCorreo != null && !remitenteCorreo.isEmpty()) {
			try {
				helper.setReplyTo(remitenteCorreo, remitenteNombre);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		helper.setText(contenidoHtml, true);

		mailSender.send(mensaje);
	}

}