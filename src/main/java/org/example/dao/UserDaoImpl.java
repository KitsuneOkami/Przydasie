package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.User;
import org.example.util.AbstractDaoImpl;

import java.util.List;

@Stateless
public class UserDaoImpl extends AbstractDaoImpl<User, Long> implements UserDao
{
	@Override
	public User find(Long primaryKey)
	{
		return getEntityManager().find(User.class, primaryKey);
	}

	@Override
	public List<User> findAll()
	{
		return getEntityManager()
				.createQuery("SELECT u FROM User u", User.class)
				.getResultList();
	}
}