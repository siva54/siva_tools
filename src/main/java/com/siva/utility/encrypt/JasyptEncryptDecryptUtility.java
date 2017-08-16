package com.siva.utility.encrypt;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * This is used to encrypt and decrypt a string using a given key.
 * 
 * @author sksees1
 *
 */
public class JasyptEncryptDecryptUtility {

	public static void main(String[] args) {

		try {
			basicTextEncryptor("testkey", "simpletext");
			basicTextDecryptor("testkey", "4LQNg0unKEw2p9y6KftCWRYbTIlbXv8v");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Completed...");

	}

	/**
	 * Method to encrypt the string using the key.
	 * 
	 * @param key
	 * @param password
	 */
	private static void basicTextEncryptor(String key, String password) {
		System.out.println("key-" + key + ":password-" + password);
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(key);
		String encryptedPassword = textEncryptor.encrypt(password);
		System.out.println("encryptedPassword-" + encryptedPassword);

	}

	/**
	 * Method to decrypt the string using the key (based on which encryption was
	 * done).
	 * 
	 * @param key
	 * @param encrytedPassword
	 */
	private static void basicTextDecryptor(String key, String encrytedPassword) {
		System.out.println("key-" + key + ":encryptedPassword-"
				+ encrytedPassword);
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(key);
		String decryptedPassword = textEncryptor.decrypt(encrytedPassword);
		System.out.println("decryptedPassword-" + decryptedPassword);
	}
}
