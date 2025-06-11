package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.example.model.User;

import java.util.Optional;

@Stateless
public class AuthService
{
	@PersistenceContext
	private EntityManager em;

	public Optional<User> authenticate(String username, String password)
	{
		try
		{
			User user = em.createQuery("""
							SELECT u FROM User u WHERE u.name = :username AND u.password = :password
							""", User.class)
					.setParameter("username", username)
					.setParameter("password", password)
					.getSingleResult();
			return Optional.of(user);
		} catch(NoResultException e)
		{
			return Optional.empty();
		}
	}
}
