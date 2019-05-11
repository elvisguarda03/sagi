package br.com.ternarius.inventario.sagi.infrastructure.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author Elvis da Guarda
 *
 */
public class SecureRandom {
	
	private final static char[] Digits = { '0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f' };
	
	public static String hex() {
		try {
			java.security.SecureRandom prng = java.security.SecureRandom.getInstance("SHA1PRNG");
			String randomNum = Integer.valueOf(prng.nextInt()).toString();
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] result = sha.digest(randomNum.getBytes());
			return hexEncode(result);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	private static String hexEncode(byte[] aInput) {
		final StringBuilder result = new StringBuilder();
		
		for (int idx = 0; idx < aInput.length; ++idx) {
			byte b = aInput[idx];
			result.append(Digits[ (b&0xf0 >> 4) ]);
			result.append(Digits[ b&0x0f]);
		}
		return result.toString();
	}
}