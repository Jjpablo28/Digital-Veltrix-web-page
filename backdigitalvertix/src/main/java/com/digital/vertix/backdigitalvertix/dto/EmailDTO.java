/**
 * Paquete que contiene las clases de Transferencia de Datos (DTOs) utilizadas 
 * en la aplicación digitalvertix backend.
 */
package com.digital.vertix.backdigitalvertix.dto;

/**
 * Clase de Transferencia de Datos (DTO) para representar la información de un
 * correo electrónico.
 * <p>
 * Esta clase se utiliza para encapsular los datos necesarios para enviar un
 * correo electrónico, incluyendo el nombre del remitente, la dirección de
 * correo del remitente, la dirección de destino, el asunto y el cuerpo del mensaje.
 */
public class EmailDTO {

	/**
	 * Nombre de la persona que envía el correo (remitente del formulario).
	 */
	private String nombreRemitente;

	/**
	 * Dirección de correo electrónico de la persona que envía el correo (remitente del formulario).
	 */
	private String correoRemitente;

	/**
	 * Dirección de correo electrónico del destinatario final del correo.
	 * (Por ejemplo, el correo de soporte de la empresa).
	 */
	private String destino;

	/**
	 * Asunto del correo electrónico.
	 */
	private String asunto;

	/**
	 * Cuerpo o mensaje principal del correo electrónico.
	 */
	private String mensaje;

	/**
	 * Constructor por defecto de {@code EmailDTO}.
	 */
	public EmailDTO() {
		// Constructor vacío necesario para la serialización/deserialización de Spring.
	}

	/**
	 * Constructor con parámetros para inicializar todos los campos de {@code EmailDTO}.
	 *
	 * @param nombreRemitente El nombre de la persona que envía el correo.
	 * @param correoRemitente La dirección de correo electrónico del remitente.
	 * @param destino         La dirección de correo electrónico del destinatario final.
	 * @param asunto          El asunto del correo electrónico.
	 * @param mensaje         El cuerpo o mensaje del correo electrónico.
	 */
	public EmailDTO(String nombreRemitente, String correoRemitente, String destino, String asunto, String mensaje) {
		this.nombreRemitente = nombreRemitente;
		this.correoRemitente = correoRemitente;
		this.destino = destino;
		this.asunto = asunto;
		this.mensaje = mensaje;
	}

	/**
	 * Obtiene el nombre de la persona que envía el correo.
	 *
	 * @return El nombre del remitente.
	 */
	public String getNombreRemitente() {
		return nombreRemitente;
	}

	/**
	 * Establece el nombre de la persona que envía el correo.
	 *
	 * @param nombreRemitente El nuevo nombre del remitente.
	 */
	public void setNombreRemitente(String nombreRemitente) {
		this.nombreRemitente = nombreRemitente;
	}

	/**
	 * Obtiene la dirección de correo electrónico de la persona que envía el correo.
	 *
	 * @return La dirección de correo electrónico del remitente.
	 */
	public String getCorreoRemitente() {
		return correoRemitente;
	}

	/**
	 * Establece la dirección de correo electrónico de la persona que envía el correo.
	 *
	 * @param correoRemitente La nueva dirección de correo electrónico del remitente.
	 */
	public void setCorreoRemitente(String correoRemitente) {
		this.correoRemitente = correoRemitente;
	}

	/**
	 * Obtiene la dirección de correo electrónico del destinatario final.
	 *
	 * @return La dirección de correo electrónico de destino.
	 */
	public String getDestino() {
		return destino;
	}

	/**
	 * Establece la dirección de correo electrónico del destinatario final.
	 *
	 * @param destino La nueva dirección de correo electrónico de destino.
	 */
	public void setDestino(String destino) {
		this.destino = destino;
	}

	/**
	 * Obtiene el asunto del correo electrónico.
	 *
	 * @return El asunto del correo electrónico.
	 */
	public String getAsunto() {
		return asunto;
	}

	/**
	 * Establece el asunto del correo electrónico.
	 *
	 * @param asunto El nuevo asunto del correo electrónico.
	 */
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	/**
	 * Obtiene el cuerpo o mensaje principal del correo electrónico.
	 *
	 * @return El mensaje del correo electrónico.
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * Establece el cuerpo o mensaje principal del correo electrónico.
	 *
	 * @param mensaje El nuevo mensaje del correo electrónico.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Devuelve una representación en cadena del objeto {@code EmailDTO}.
	 *
	 * @return Una cadena que representa el objeto {@code EmailDTO} con sus
	 * atributos.
	 */
	@Override
	public String toString() {
		return "EmailDTO [nombreRemitente=" + nombreRemitente + ", correoRemitente=" + correoRemitente +
		       ", destino=" + destino + ", asunto=" + asunto + ", mensaje=" + mensaje + "]";
	}
}