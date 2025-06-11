package org.example.web;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import org.example.model.User;

import java.io.Serializable;

/**
 * @author Pabilo8
 * @since 11.06.2025
 */
@Named(value = "userSessionBean")
@SessionScoped
public class UserSessionBean implements Serializable
{
	@Getter
	private String username;
	@Getter
	private User user;

	@PostConstruct
	public void init()
	{
		username = null;
		user = null;
	}

	public void setUser(User user)
	{
		this.user = user;
		this.username = user.getName();
	}

	public boolean isLoggedIn()
	{
		return username!=null;
	}

	public void logout()
	{
		user = null;
		username = null;
	}
}