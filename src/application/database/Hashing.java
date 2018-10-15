package application.database;

import java.math.BigInteger;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Hashing {

	/**
	 * @param password is the password the user chose at registration
	 * @return the generated hash based on random salt and the param password
	 * 
	 * @author Niklas Sølvberg
	 */
	public static String generateHash(String password) {
		char[] passwordArray = password.toCharArray();
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[24];
		random.nextBytes(salt);
		byte[] hash = PBKDF2(passwordArray, salt, 1000, 24);
		return 1000 + ":" + toHex(salt) + ":" + toHex(hash);
	}
	
	/**
	 * @param password is the password that a user typed
	 * @param storedFullHash is the hash thta corresponds to the username that is being used to log in
	 * @return if the typed password is correct
	 * 
	 * @author Niklas Sølvberg
	 */
	public static boolean validPassword(String password, String storedFullHash) {
		char[] passwordArray = password.toCharArray();
		String[] hashParams = storedFullHash.split(":");
		int storedIterations = Integer.parseInt(hashParams[0]);
		byte[] storedSalt = fromHex(hashParams[1]);
		byte[] storedHash = fromHex(hashParams[2]);
		byte[] generatedHash = PBKDF2(passwordArray, storedSalt, storedIterations, storedHash.length);
		return equals(storedHash, generatedHash);
	}
	
	/**
	 * Used when hashing
	 * 
	 * @author Niklas Sølvberg
	 */
	private static boolean equals(byte[] a, byte[] b) {
		int diff = a.length ^ b.length;
		for (int i = 0; i < a.length && i < b.length; i++)
			diff |= a[i] ^ b[i];
		return diff == 0;
	}
	
	/**
	 * Used when hashing
	 * 
	 * @author Niklas Sølvberg
	 */
	private static byte[] PBKDF2(char[] password, byte[] salt, int iterations, int bytes) {
		try {
			PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, bytes * 8);
			SecretKeyFactory keyFactory;
			keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			return keyFactory.generateSecret(keySpec).getEncoded();
		}
		catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Used when hashing
	 * 
	 * @author Niklas Sølvberg
	 */
	private static byte[] fromHex(String hex) {
		byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++)
			binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, (2 * i) + 2), 16);
		return binary;
	}
	
	/**
	 * Used when hashing
	 * 
	 * @author Niklas Sølvberg
	 */
	private static String toHex(byte[] array) {
		BigInteger bigInteger = new BigInteger(1, array);
		String hex = bigInteger.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0)
			return String.format("%0" + paddingLength + "d", 0) + hex;
		return hex;
	}
	
}
