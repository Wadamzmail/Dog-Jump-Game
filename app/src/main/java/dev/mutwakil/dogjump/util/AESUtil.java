package dev.mutwakil.dogjump.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {
	private static final String ALGORITHM = "AES";
	private static final String KEY = "mutwakil01234567";
	
	public static byte[] encrypt(byte[] data) throws Exception {
		SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		return cipher.doFinal(data);
	}
	
	public static byte[] decrypt(byte[] encryptedData) throws Exception {
		SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		return cipher.doFinal(encryptedData);
	}
}