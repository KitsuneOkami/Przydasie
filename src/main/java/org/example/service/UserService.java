package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import org.example.dao.UserDao;
import org.example.model.User;

import java.util.List;
import java.util.Optional;

@Stateless
public class UserService
{
	@Inject
	private UserDao userDao;

	public Optional<User> authenticate(String username, String password)
	{
		try
		{
			Optional<User> found = userDao.findByName(username);
			if(found.isPresent()&&found.get().getPassword().equals(password))
				return found;

		} catch(NoResultException ignored)
		{

		}
		return Optional.empty();
	}

	public List<User> findAllUsers()
	{
		return userDao.findAll();
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