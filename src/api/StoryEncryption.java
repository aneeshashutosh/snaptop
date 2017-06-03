package api;

import org.apache.commons.codec.binary.Base64;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class StoryEncryption
{
	static char[] HEX_CHARS = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	public static byte[] decrypt(byte[] storyData, String MediaKey, String MediaIV)
	{
		byte[] key = Base64.decodeBase64(MediaKey.getBytes());
		byte[] iv = Base64.decodeBase64(MediaIV.getBytes());

		IvParameterSpec ivspec = new IvParameterSpec(iv);
		SecretKeySpec keyspec = new SecretKeySpec(key, "AES");

		Cipher cipher = null;
		try
		{
			cipher = Cipher.getInstance("AES/CBC/NoPadding");
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
		}

		byte[] decrypted = null;
		try
		{
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
			decrypted = cipher.doFinal(storyData);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return decrypted;
	}
}