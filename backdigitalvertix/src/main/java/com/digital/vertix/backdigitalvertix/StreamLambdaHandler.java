/**
 * Este paquete contiene las clases principales para la aplicación backend de Vertix Digital.
 * Incluye la configuración y el manejador para la integración con AWS Lambda,
 * permitiendo que la aplicación se ejecute en un entorno sin servidor.
 */
package com.digital.vertix.backdigitalvertix;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import jakarta.ws.rs.core.Application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Clase manejadora de solicitudes AWS Lambda que integra una aplicación Spring
 * Boot. Esta clase actúa como un puente entre las invocaciones de AWS Lambda y
 * el framework Spring Boot, permitiendo que las aplicaciones Spring Boot se
 * ejecuten en un entorno sin servidor (serverless) utilizando AWS Lambda y
 * Amazon API Gateway. Extiende
 * {@link com.amazonaws.services.lambda.runtime.RequestStreamHandler} para
 * procesar solicitudes como flujos de entrada y salida.
 */
public class StreamLambdaHandler implements RequestStreamHandler {
	/**
	 * Instancia estática del manejador de contenedores de Spring Boot para AWS
	 * Lambda. Se inicializa una vez cuando la clase es cargada, lo que optimiza el
	 * rendimiento al reutilizar la instancia de Spring Boot en invocaciones
	 * posteriores de la función Lambda. Maneja la conversión de solicitudes de AWS
	 * Proxy a solicitudes internas de Spring Boot y viceversa para las respuestas.
	 */
	private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

	static {
		try {
			handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
		} catch (ContainerInitializationException e) {
			throw new RuntimeException("No se pudo iniciar Spring", e);
		}
	}

	/**
	 * Maneja la solicitud de AWS Lambda procesando los flujos de entrada y salida.
	 * Este método es invocado por el entorno de AWS Lambda cuando la función es
	 * ejecutada. Delega el procesamiento real de la solicitud al manejador estático
	 * de Spring Boot, que se encarga de convertir el flujo de entrada en un objeto
	 * de solicitud de Spring, invocar el controlador apropiado en la aplicación
	 * Spring Boot y escribir la respuesta de Spring Boot en el flujo de salida.
	 *
	 * @param input   Un {@link java.io.InputStream} que contiene la solicitud de la
	 *                invocación de Lambda. Este flujo generalmente contiene datos
	 *                JSON que representan un evento de API Gateway.
	 * @param output  Un {@link java.io.OutputStream} donde se debe escribir la
	 *                respuesta de la función Lambda. La respuesta se enviará de
	 *                vuelta a API Gateway o al servicio que invocó la Lambda.
	 * @param context Un {@link com.amazonaws.services.lambda.runtime.Context} que
	 *                proporciona información sobre el entorno de ejecución de
	 *                Lambda, la solicitud y la función.
	 * @throws IOException Si ocurre un error de entrada/salida al leer del flujo de
	 *                     entrada o escribir en el flujo de salida.
	 */
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		handler.proxyStream(input, output, context);
	}
}
