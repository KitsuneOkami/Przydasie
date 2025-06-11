package org.example.web;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import org.example.model.User;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pabilo8
 * @since 11.06.2025
 */
@Named(value = "userSessionBean")
@SessionScoped
public class UserSessionBean implements Serializable
{
	private static final Logger logger = Logger.getLogger(UserSessionBean.class.getName());
	@Getter
	private String username;
	@Getter
	private User user;
	@PostConstruct
	public void init()
	{
		logger.log(Level.INFO, "UserSessionBean initialized.");
		username = null;
		user = null;
	}

	public void setUser(User user)
	{
		this.user = user;
		this.username = user.getName();
		logger.log(Level.INFO, "User ''{0}'' set in session.", username);
	}

	public boolean isLoggedIn()
	{
		return username!=null;
	}

	public void logout()
	{
		logger.log(Level.INFO, "User ''{0}'' logged out from session.", username);
		user = null;
		username = null;
	}
}