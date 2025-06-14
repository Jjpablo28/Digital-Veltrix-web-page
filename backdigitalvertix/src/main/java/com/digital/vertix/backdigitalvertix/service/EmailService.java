/**
 * Paquete que contiene las clases de servicio de la aplicación digitalvertix
 * backend.
 */
package com.digital.vertix.backdigitalvertix.service;

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
		MimeMessage mensaje = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

		helper.setTo(destino);
		helper.setSubject(asunto);
		helper.setFrom("veltrixdigital.co@gmail.com");

		helper.setText(contenidoHtml, true);

		mailSender.send(mensaje);
	}

}
