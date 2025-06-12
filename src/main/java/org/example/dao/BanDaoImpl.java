package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.Ban;
import org.example.util.AbstractDaoImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class BanDaoImpl extends AbstractDaoImpl<Ban, Long> implements BanDao
{
	private static final Logger logger = Logger.getLogger(BanDaoImpl.class.getName());

	@Override
	public Ban find(Long primaryKey)
	{
		logger.log(Level.INFO, "Finding User with primary key: {0}", primaryKey);
		return getEntityManager().find(Ban.class, primaryKey);
	}

	@Override
	public List<Ban> findAll()
	{
		logger.log(Level.INFO, "Finding all Users.");
		return getEntityManager()
				.createQuery("SELECT b FROM Ban b", Ban.class)
				.getResultList();
	}

	@Override
	public List<Ban> findAllEagerUsers()
	{
		logger.log(Level.INFO, "Finding all Bans with eager loading of users.");
		return getEntityManager()
				.createQuery("SELECT b FROM Ban b JOIN FETCH b.bannedUser", Ban.class)
				.getResultList();
	}

	@Override
	public boolean isUserBanned(Long userId)
	{
		logger.log(Level.INFO, "Checking if user with ID {0} is banned.", userId);
		List<Ban> bans = getEntityManager()
				.createQuery("SELECT b FROM Ban b WHERE b.bannedUser.userId = :userId", Ban.class)
				.setParameter("userId", userId)
				.getResultList();

		for(Ban ban : bans)
		{
			if(ban.getEndDate()==null||ban.getEndDate().isAfter(LocalDateTime.now()))
			{
				logger.log(Level.INFO, "User with ID {0} is currently banned.", userId);
				return true;
			}
		}
		logger.log(Level.INFO, "User with ID {0} is not banned.", userId);
		return false;
	}
}