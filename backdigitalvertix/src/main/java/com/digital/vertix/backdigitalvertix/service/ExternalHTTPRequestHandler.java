/**
 * Paquete que contiene las clases de servicio de la aplicación digitalvertix
 * backend.
 */
package com.digital.vertix.backdigitalvertix.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Clase de utilidad para realizar peticiones HTTP externas y parsear las
 * respuestas.
 * <p>
 * Esta clase proporciona métodos estáticos para realizar peticiones GET y POST
 * a servicios externos, como la API de VirusTotal y el modelo de lenguaje
 * Gemini. También incluye funcionalidades para parsear las respuestas JSON y
 * convertirlas a objetos DTO (Data Transfer Object) específicos de la
 * aplicación.
 */
public class ExternalHTTPRequestHandler {
	/**
	 * Cliente HTTP singleton configurado con HTTP/2 y un timeout de conexión.
	 */
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();

	/**
	 * Realiza una petición GET a la URL especificada y parsea la respuesta JSON
	 * para imprimirla de forma legible utilizando Gson.
	 *
	 * @param url La URL a la que se realizará la petición GET.
	 * @return Una cadena JSON formateada de forma legible.
	 */
	public static String doGetAndParse(String url) {
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.header("Content-type", "application/json").build();

		HttpResponse<String> response = null;

		try {
			response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("status code -> " + response.statusCode());
		String uglyJson = response.body();
		return prettyPrintUsingGson(uglyJson);
	}

	/**
	 * Formatea una cadena JSON de forma legible utilizando la librería Gson.
	 *
	 * @param uglyJson La cadena JSON sin formato.
	 * @return Una cadena JSON formateada de forma legible.
	 */
	public static String prettyPrintUsingGson(String uglyJson) {
		Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
		JsonElement jsonElement = JsonParser.parseString(uglyJson);
		String prettyJsonString = gson.toJson(jsonElement);
		return prettyJsonString;

	}
}
