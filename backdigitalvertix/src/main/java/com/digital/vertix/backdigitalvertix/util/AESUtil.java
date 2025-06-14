/**
 * Paquete que contiene las clases de utilidad de la aplicación digitalvertix
 * backend.
 */
package com.digital.vertix.backdigitalvertix.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
/**
 * Clase de utilidad para realizar operaciones de cifrado AES (Advanced
 * Encryption Standard) y hashing con diferentes algoritmos.
 * <p>
 * Utiliza el modo GCM (Galois/Counter Mode) para el cifrado AES, que proporciona
 * autenticación y confidencialidad. También incluye métodos para realizar
 * hashing MD5, SHA-1, SHA-256, SHA-384 y SHA-512 utilizando la librería Apache
 * Commons Codec.
 */
public class AESUtil {
	/**
	 * Nombre del algoritmo de cifrado AES.
	 */
	private final static String ALGORITMO = "AES";
	/**
	 * Tipo de cifrado AES con modo GCM y sin padding.
	 */
	private final static String TIPOCIFRADO = "AES/GCM/NoPadding";
	/**
	 * Encripta un texto plano utilizando la clave y el vector de inicialización
	 * (IV) proporcionados.
	 *
	 * @param llave La clave de cifrado.
	 * @param iv    El vector de inicialización.
	 * @param texto El texto plano a encriptar.
	 * @return El texto encriptado en formato Base64.
	 */
	public static String encrypt(String llave, String iv, String texto) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(TIPOCIFRADO);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(), ALGORITMO);
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes());
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}

		byte[] encrypted = null;
		try {
			encrypted = cipher.doFinal(texto.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}

		return new String(encodeBase64(encrypted));
	}
	/**
	 * Desencripta un texto cifrado (en formato Base64) utilizando la clave y el
	 * vector de inicialización (IV) proporcionados.
	 *
	 * @param llave     La clave de descifrado.
	 * @param iv        El vector de inicialización.
	 * @param encrypted El texto cifrado en formato Base64.
	 * @return El texto plano desencriptado.
	 */
	public static String decrypt(String llave, String iv, String encrypted) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(TIPOCIFRADO);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(), ALGORITMO);
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes());
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {

			e.printStackTrace();
		}

		byte[] enc = decodeBase64(encrypted);
		byte[] decrypted = null;
		try {
			decrypted = cipher.doFinal(enc);
		} catch (IllegalBlockSizeException | BadPaddingException e) {

			e.printStackTrace();
		}

		return new String(decrypted);
	}
	/**
	 * Desencripta un texto cifrado (en formato Base64) utilizando una clave y un
	 * vector de inicialización predefinidos.
	 *
	 * @param encrypted El texto cifrado en formato Base64.
	 * @return El texto plano desencriptado.
	 */
	public static String decrypt(String encrypted) {
		String iv = "proyectVertixdeae";
		String key = "keywhitsecuredlk";
		return decrypt(key, iv, encrypted);
	}
	/**
	 * Encripta un texto plano utilizando una clave y un vector de inicialización
	 * predefinidos.
	 *
	 * @param plainText El texto plano a encriptar.
	 * @return El texto encriptado en formato Base64.
	 */
	public static String encrypt(String plainText) {
		String iv = "proyectVertixdeae";
		String key = "keywhitsecuredlk";
		return encrypt(key, iv, plainText);
	}
	/**
	 * Realiza un hash MD5 de un contenido dado.
	 *
	 * @param content El contenido a hashear.
	 * @return El hash MD5 en formato hexadecimal.
	 */
	public static String hashingToMD5(String content) {
		return DigestUtils.md5Hex(content);
	}
	/**
	 * Realiza un hash SHA-1 de un contenido dado.
	 *
	 * @param content El contenido a hashear.
	 * @return El hash SHA-1 en formato hexadecimal.
	 */
	public static String hashingToSHA1(String content) {
		return DigestUtils.sha1Hex(content);
	}
	/**
	 * Realiza un hash SHA-256 de un contenido dado.
	 *
	 * @param content El contenido a hashear.
	 * @return El hash SHA-256 en formato hexadecimal.
	 */
	public static String hashingToSHA256(String content) {
		return DigestUtils.sha256Hex(content);
	}
	/**
	 * Realiza un hash SHA-384 de un contenido dado.
	 *
	 * @param content El contenido a hashear.
	 * @return El hash SHA-384 en formato hexadecimal.
	 */
	public static String hashingToSHA384(String content) {
		return DigestUtils.sha384Hex(content);
	}
	/**
	 * Realiza un hash SHA-512 de un contenido dado.
	 *
	 * @param content El contenido a hashear.
	 * @return El hash SHA-512 en formato hexadecimal.
	 */
	public static String hashingToSHA512(String content) {
		return DigestUtils.sha512Hex(content);
	}

}
