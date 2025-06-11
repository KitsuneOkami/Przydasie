package org.example.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordUtil
{
	private static final Logger logger = Logger.getLogger(PasswordUtil.class.getName());
	public static String hash(String password)
	{
		logger.log(Level.INFO, "Hashing password");
		String hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray());
		logger.log(Level.INFO, "Password hashed successfully.");
		return hashed;
	}

	public static boolean check(String password, String hash)
	{
		logger.log(Level.INFO, "Checking password.");
		BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
		logger.log(Level.INFO, "Password check result: {0}", result.verified);
		return result.verified;
	}
}