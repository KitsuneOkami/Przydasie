package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import org.example.dao.UserDao;
import org.example.model.User;
import org.example.util.PasswordUtil;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class UserService
{
	private static final Logger logger = Logger.getLogger(UserService.class.getName());
	@Inject
	private UserDao userDao;
	public Optional<User> authenticate(String username, String password)
	{
		logger.log(Level.INFO, "Authenticating user: {0}", username);
		try
		{
			Optional<User> found = userDao.findByName(username);
			if(found.isPresent()&&PasswordUtil.check(password, found.get().getPassword())) {
				logger.log(Level.INFO, "User {0} authenticated successfully.", username);
				return found;
			}
		} catch(NoResultException ignored)
		{
			logger.log(Level.WARNING, "No result exception for user: {0}", username);
		}
		logger.log(Level.WARNING, "Authentication failed for user: {0}", username);
		return Optional.empty();
	}

	public User findByName(String username)
	{
		logger.log(Level.INFO, "Finding user by name: {0}", username);
		Optional<User> found = userDao.findByName(username);
		return found.orElse(null);
	}

	public boolean usernameOrEmailTaken(String username, String email)
	{
		logger.log(Level.INFO, "Checking if username ''{0}'' or email ''{1}'' is taken.", new Object[]{username, email});
		Optional<User> found = userDao.findByName(username);
		boolean result = found.isPresent()&&found.get().getEmail().equals(email);
		if(result) {
			logger.log(Level.WARNING, "Username ''{0}'' and email ''{1}'' are already taken.", new Object[]{username, email});
		} else {
			logger.log(Level.INFO, "Username ''{0}'' and email ''{1}'' are available.", new Object[]{username, email});
		}
		return result;
	}

	public void saveUser(User user)
	{
		logger.log(Level.INFO, "Saving user: {0}", user);
		userDao.save(user);
	}

	public User getUser(Long id)
	{
		logger.log(Level.INFO, "Retrieving user with ID: {0}", id);
		return userDao.find(id);
	}

	public boolean deleteUser(Long id)
	{
		logger.log(Level.INFO, "Deleting user with ID: {0}", id);
		User user = getUser(id);
		if(user==null)
		{
			logger.log(Level.WARNING, "User with ID {0} not found for deletion.", id);
			return true;
		}

		userDao.delete(user);
		logger.log(Level.INFO, "User with ID {0} deleted successfully.", id);
		return true;
	}
}