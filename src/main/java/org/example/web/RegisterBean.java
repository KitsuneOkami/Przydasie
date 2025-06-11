package org.example.web;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.example.model.User;
import org.example.model.User.Role;
import org.example.service.UserService;
import org.example.util.JSFUtil;
import org.example.util.PasswordUtil;

@Named
@RequestScoped
public class RegisterBean
{
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
		JSFUtil.redirectIfLogged(true, "auctions.xhtml");
	}

	@Transactional
	public String register()
	{
		//Passwords must match
		if(!password.equals(confirmPassword))
		{
			JSFUtil.addErrorMessage("Hasła są różne");
			return null;
		}

		//There can be no null or empty values
		if(username==null||username.isEmpty()||email==null||email.isEmpty()||password.isEmpty())
		{
			JSFUtil.addErrorMessage("Wszystkie pola są wymagane");
			return null;
		}

		//Two users must not share a username or email
		if(userService.usernameOrEmailTaken(username, email))
		{
			JSFUtil.addErrorMessage("Użytkownik bądź email już istnieje");
			return null;
		}

		//Save new user
		User user = new User();
		user.setName(username);
		user.setEmail(email);
		user.setPassword(PasswordUtil.hash(password));
		user.setRole(Role.USER);
		userService.saveUser(user);

		JSFUtil.addInfoMessage("Pomyślnie zarejestrowano użytkownika");

		//Redirect, so the user can log in
		return "/login.xhtml?faces-redirect=true";
	}
}
