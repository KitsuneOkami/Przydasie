package org.example.web;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.JSFUtil;

import java.util.Optional;

/**
 * @author Pabilo8
 * @since 11.06.2025
 */
@Named
@RequestScoped
public class LoginBean
{
	@Setter
	@Getter
	private String username;
	@Setter
	@Getter
	private String password;

	@Inject
	private UserSessionBean userSessionBean;

	@Inject
	private UserService userService;

	@Inject
	private HttpServletRequest request;

	@PostConstruct
	public void init()
	{
		JSFUtil.redirectIfLogged(true, "auctions.xhtml");
	}

	public String login()
	{
		Optional<User> user = userService.authenticate(username, password);

		if(user.isPresent())
		{
			//Store the user and username in the session bean
			userSessionBean.setUser(user.get());
			return "/auctions.xhtml?faces-redirect=true";
		}
		else
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", "Invalid credentials"));
			return null;
		}
	}

	public String logout()
	{
		System.out.println("Logged out");
		userSessionBean.logout(); // Clear the session bean
		return "/login.xhtml?faces-redirect=true";
	}
}
