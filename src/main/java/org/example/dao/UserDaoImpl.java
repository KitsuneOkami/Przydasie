package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.User;
import org.example.util.AbstractDaoImpl;

import java.util.List;
import java.util.Optional;

@Stateless
public class UserDaoImpl extends AbstractDaoImpl<User, Long> implements UserDao
{
	@Override
	public User find(Long primaryKey)
	{
		return getEntityManager().find(User.class, primaryKey);
	}

	@Override
	public Optional<User> findByName(String name)
	{
		try
		{
			return Optional.of(getEntityManager()
					.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class)
					.setParameter("name", name)
					.getSingleResult());
		} catch(jakarta.persistence.NoResultException e)
		{
			return Optional.empty();
		}
	}

	@Override
	public List<User> findAll()
	{
		return getEntityManager()
				.createQuery("SELECT u FROM User u", User.class)
				.getResultList();
	}
}