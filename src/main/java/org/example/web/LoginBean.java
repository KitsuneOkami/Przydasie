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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pabilo8
 * @since 11.06.2025
 */
@Named
@RequestScoped
public class LoginBean
{
	private static final Logger logger = Logger.getLogger(LoginBean.class.getName());
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
		logger.log(Level.INFO, "Initializing LoginBean.");
		JSFUtil.redirectIfLogged(true, "auctions.xhtml");
	}

	public String login()
	{
		logger.log(Level.INFO, "Login attempt for user: {0}", username);
		Optional<User> user = userService.authenticate(username, password);
		if(user.isPresent())
		{
			userSessionBean.setUser(user.get());
			logger.log(Level.INFO, "User ''{0}'' logged in successfully.", username);
			return "/auctions.xhtml?faces-redirect=true";
		}
		else
		{
			logger.log(Level.WARNING, "Login failed for user: {0}", username);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", "Invalid credentials"));
			return null;
		}
	}

	public String logout()
	{
		logger.log(Level.INFO, "User {0} is logging out.", userSessionBean.getUsername());
		userSessionBean.logout(); // Clear the session bean
		return "/login.xhtml?faces-redirect=true";
	}
}