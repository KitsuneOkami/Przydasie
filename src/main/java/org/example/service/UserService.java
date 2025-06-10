package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.dao.UserDao;
import org.example.model.User;

import java.util.List;

@Stateless
public class UserService
{
	@Inject
	private UserDao userDao;

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