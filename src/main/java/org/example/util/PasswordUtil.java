package org.example.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil
{
	public static String hash(String password)
	{
		return BCrypt.withDefaults().hashToString(12, password.toCharArray());
	}

	public static boolean check(String password, String hash)
	{
		return BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
	}
}