package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.example.model.User;
import org.example.model.User.Role;
import org.example.service.UserService;
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
	@Inject
	private
	IdentityStoreHandler identityStoreHandler;

	@Transactional
	public String register()
	{
		//Passwords must match
		if(!password.equals(confirmPassword))
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match", null));
			return null;
		}

		//There can be no null or empty values
		if(username==null||username.isEmpty()||email==null||email.isEmpty()||password.isEmpty())
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username, email, and password must not be empty", null));
			return null;
		}

		//Two users must not share a username or email
		if(userService.usernameOrEmailTaken(username, email))
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Username or email already exists", null));
			return null;
		}

		//Save new user
		User user = new User();
		user.setName(username);
		user.setEmail(email);
		user.setPassword(PasswordUtil.hash(password));
		user.setRole(Role.USER);
		userService.saveUser(user);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration successful!", null));

		//Redirect, so the user can log in
		return "/login.xhtml?faces-redirect=true";
	}
}
