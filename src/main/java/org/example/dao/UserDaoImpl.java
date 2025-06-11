package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.User;
import org.example.util.AbstractDaoImpl;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class UserDaoImpl extends AbstractDaoImpl<User, Long> implements UserDao
{
	private static final Logger logger = Logger.getLogger(UserDaoImpl.class.getName());
	@Override
	public User find(Long primaryKey)
	{
		logger.log(Level.INFO, "Finding User with primary key: {0}", primaryKey);
		return getEntityManager().find(User.class, primaryKey);
	}

	@Override
	public Optional<User> findByName(String name)
	{
		logger.log(Level.INFO, "Finding User with name: {0}", name);
		try
		{
			return Optional.of(getEntityManager()
					.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class)
					.setParameter("name", name)
					.getSingleResult());
		} catch(jakarta.persistence.NoResultException e)
		{
			logger.log(Level.WARNING, "No user found with name: {0}", name);
			return Optional.empty();
		}
	}

	@Override
	public List<User> findAll()
	{
		logger.log(Level.INFO, "Finding all Users.");
		return getEntityManager()
				.createQuery("SELECT u FROM User u", User.class)
				.getResultList();
	}
}