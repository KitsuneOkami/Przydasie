package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.User;
import org.example.service.UserService;

import java.util.List;

@Named
@RequestScoped
public class UserBean
{
	@Inject
	private UserService userService;

	@Setter
	@Getter
	private User user = new User();

	public List<User> getUsers()
	{
		return userService.findAllUsers();
	}

	public void save()
	{
		userService.saveUser(user);
		user = new User();
	}
}