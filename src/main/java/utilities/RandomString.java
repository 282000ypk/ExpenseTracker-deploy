package utilities;

import java.security.SecureRandom;

public class RandomString {
	public static String getRandomString(int length)
	{
		try
		{
			String chrs = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		    SecureRandom secureRandom = SecureRandom.getInstanceStrong();
		    // 9 is the length of the string you want
		    String randomstring = secureRandom.ints(length, 0, chrs.length())
		    		.mapToObj(i -> chrs.charAt(i))
		    		.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
		    		.toString();
		    return randomstring;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
