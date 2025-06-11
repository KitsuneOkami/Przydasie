package org.example.web;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.example.model.User;
import org.example.model.User.Role;
import org.example.service.UserService;
import org.example.util.JSFUtil;
import org.example.util.PasswordUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class RegisterBean
{
	private static final Logger logger = Logger.getLogger(RegisterBean.class.getName());
	@Getter
	@Setter
	private String username;
	@Getter
	@Setter
	private String email;
	@Getter
	@Setter
	private String password;
	@Getter
	@Setter
	private String confirmPassword;

	@Inject
	private
	UserService userService;
//	@Inject
//	private
//	IdentityStoreHandler identityStoreHandler; for some reason cant deploy with this

	@PostConstruct
	public void init()
	{
		logger.log(Level.INFO, "Initializing RegisterBean.");
		JSFUtil.redirectIfLogged(true, "auctions.xhtml");
	}

	@Transactional
	public String register()
	{
		logger.log(Level.INFO, "Registration attempt for user: {0}", username);
		if(!password.equals(confirmPassword))
		{
			logger.log(Level.WARNING, "Registration failed for user {0}: Passwords do not match.", username);
			JSFUtil.addErrorMessage("Hasła są różne");
			return null;
		}

		if(username==null||username.isEmpty()||email==null||email.isEmpty()||password.isEmpty())
		{
			logger.log(Level.WARNING, "Registration failed for user {0}: One or more fields are empty.", username);
			JSFUtil.addErrorMessage("Wszystkie pola są wymagane");
			return null;
		}

		if(userService.usernameOrEmailTaken(username, email))
		{
			logger.log(Level.WARNING, "Registration failed for user {0}: Username or email already exists.", username);
			JSFUtil.addErrorMessage("Użytkownik bądź email już istnieje");
			return null;
		}

		User user = new User();
		user.setName(username);
		user.setEmail(email);
		user.setPassword(PasswordUtil.hash(password));
		user.setRole(Role.USER);
		userService.saveUser(user);

		logger.log(Level.INFO, "User {0} registered successfully.", username);
		JSFUtil.addInfoMessage("Pomyślnie zarejestrowano użytkownika");
		return "/login.xhtml?faces-redirect=true";
	}
}