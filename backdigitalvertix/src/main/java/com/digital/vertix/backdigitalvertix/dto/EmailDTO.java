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
 * correo electrónico, incluyendo la dirección de destino, el asunto y el
 * cuerpo del mensaje.
 */
public class EmailDTO {
	/**
	 * Dirección de correo electrónico del destinatario.
	 */
	private String destino;
	/**
	 * Asunto del correo electrónico.
	 */
	private String asunto;
	/**
	 * Cuerpo o mensaje del correo electrónico.
	 */
	private String mensaje;
	/**
	 * Constructor por defecto de {@code EmailDTO}.
	 */
	public EmailDTO() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Constructor con parámetros para inicializar todos los campos de {@code EmailDTO}.
	 *
	 * @param destino La dirección de correo electrónico del destinatario.
	 * @param asunto  El asunto del correo electrónico.
	 * @param mensaje El cuerpo o mensaje del correo electrónico.
	 */
	public EmailDTO(String destino, String asunto, String mensaje) {
		super();
		this.destino = destino;
		this.asunto = asunto;
		this.mensaje = mensaje;
	}
	/**
	 * Obtiene la dirección de correo electrónico del destinatario.
	 *
	 * @return La dirección de correo electrónico de destino.
	 */
	public String getDestino() {
		return destino;
	}
	/**
	 * Establece la dirección de correo electrónico del destinatario.
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
	 * Obtiene el cuerpo o mensaje del correo electrónico.
	 *
	 * @return El mensaje del correo electrónico.
	 */
	public String getMensaje() {
		return mensaje;
	}
	/**
	 * Establece el cuerpo o mensaje del correo electrónico.
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
		return "EmailDTO [destino=" + destino + ", asunto=" + asunto + ", mensaje=" + mensaje + "]";
	}

}
