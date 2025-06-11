package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import org.example.dao.UserDao;
import org.example.model.User;
import org.example.util.PasswordUtil;

import java.util.Optional;

@Stateless
public class UserService
{
	@Inject
	private UserDao userDao;

	public Optional<User> authenticate(String username, String password)
	{
		System.out.println("Authenticating...");
		try
		{
			Optional<User> found = userDao.findByName(username);
			System.out.println("Found: "+found.isPresent());
			if(found.isPresent()&&PasswordUtil.check(password, found.get().getPassword()))
				return found;

		} catch(NoResultException ignored)
		{

		}
		return Optional.empty();
	}

	public boolean usernameOrEmailTaken(String username, String email)
	{
		Optional<User> found = userDao.findByName(username);
		return found.isPresent()&&found.get().getEmail().equals(email);
	}

	public void saveUser(User user)
	{
		userDao.save(user);
	}

	public User getUser(Long id)
	{
		return userDao.find(id);
	}

	public boolean deleteUser(Long id)
	{
		User user = getUser(id);
		if(user==null)
		{
			return true;
		}

		userDao.delete(user);
		return true;
	}
}