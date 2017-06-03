package api;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Encryption
{

	private static final String KEY_ALG = "AES";
	private static final String AES_KEY = "M02cnQ51Ji97vwT4";
	private static final String CIPHER_MODE = "AES/ECB/PKCS5Padding";

	public static byte[] encrypt(byte[] data) throws EncryptionException
	{
		Cipher cipher = null;
		try
		{
			cipher = Cipher.getInstance(CIPHER_MODE, "BC");
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new EncryptionException(e);
		}
		catch (NoSuchPaddingException e)
		{
			throw new EncryptionException(e);
		}
		catch (NoSuchProviderException e)
		{
			try
			{
				cipher = Cipher.getInstance(CIPHER_MODE);
			}
			catch(Exception er)
			{
				throw new EncryptionException(er);
			}
		}
		byte[] keyBytes = AES_KEY.getBytes();
		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, KEY_ALG);
		try
		{
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		}
		catch (InvalidKeyException e)
		{
			throw new EncryptionException(e);
		}
		try
		{
			byte[] result = cipher.doFinal(data);
			return result;
		}
		catch (IllegalBlockSizeException e)
		{
			throw new EncryptionException(e);
		}
		catch (BadPaddingException e)
		{
			throw new EncryptionException(e);
		}
	}

	public static byte[] decrypt(byte[] data) throws EncryptionException
	{
		Cipher cipher = null;
		try
		{
			cipher = Cipher.getInstance(CIPHER_MODE, "BC"); //Try and use the BC provider for devices which throw key length errors.
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new EncryptionException(e);
		}
		catch (NoSuchPaddingException e)
		{
			throw new EncryptionException(e);
		}
		catch (NoSuchProviderException e)
		{
			try
			{
				cipher = Cipher.getInstance(CIPHER_MODE); //Use this if BC provider not found.
			}
			catch(Exception er)
			{
				throw new EncryptionException(er);
			}
		}

		byte[] keyBytes = AES_KEY.getBytes();
		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, KEY_ALG);
		try
		{
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		}
		catch (InvalidKeyException e)
		{
			throw new EncryptionException(e);
		}

		try
		{
			byte[] result = cipher.doFinal(data);
			return result;
		}
		catch (IllegalBlockSizeException e)
		{
			throw new EncryptionException(e);
		}
		catch (BadPaddingException e)
		{
			throw new EncryptionException(e);
		}
	}

	public static class EncryptionException extends Exception
	{
		private static final long serialVersionUID = -6316941392522961682L;
		private Exception cause;
		public EncryptionException(Exception e)
		{
			this.cause = e;
		}

		public void printStackTrace()
		{
			cause.printStackTrace();
			this.printStackTrace();
		}
	}
}