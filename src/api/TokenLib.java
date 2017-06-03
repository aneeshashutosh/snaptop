package api;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TokenLib
{

	private static final String SECRET = "iEk21fuwZApXlz93750dmW22pw389dPwOk";
	private static final String PATTERN = "0001110111101110001111010101111011010001001110011000110001000110";
	private static final String STATIC_TOKEN = "m198sOkJEn37DjqZ32lpRu76xmw288xSQ9";
	private static final String SHA256 = "SHA-256";

	public static String requestToken(String authToken, Long timestamp)
	{
		String firstHex = hexDigest(SECRET + authToken);
		String secondHex = hexDigest(timestamp.toString() + SECRET);
		StringBuilder sb = new StringBuilder();
		char[] patternChars = PATTERN.toCharArray();
		for (int i = 0; i < patternChars.length; i++)
		{
			char c = patternChars[i];
			if (c == '0')
			{
				sb.append(firstHex.charAt(i));
			}
			else
			{
				sb.append(secondHex.charAt(i));
			}
		}
		return sb.toString();
	}

	public static String staticRequestToken(Long timestamp)
	{
		return requestToken(STATIC_TOKEN, timestamp);
	}

	private static String hexDigest(String toDigest)
	{
		try
		{
			MessageDigest sha256 = MessageDigest.getInstance(SHA256);
			byte[] digested = sha256.digest(toDigest.getBytes());
			return bytesToHex(digested);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static String bytesToHex(byte[] digested)
	{
		char[] hexArray = "0123456789abcdef".toCharArray();
		char[] hexChars = new char[digested.length * 2];
		for (int i = 0; i < digested.length; i++)
		{
			int v = digested[i] & 0xFF;
			hexChars[i * 2] = hexArray[v >>> 4];
			hexChars[(i * 2) + 1] = hexArray[v & 0x0F];
		}
		return (new String(hexChars));
	}
}