package org.example.service;

import jakarta.inject.Inject;
import org.example.dao.BanDao;
import org.example.model.Ban;
import org.example.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BansService
{
	private static final Logger logger = Logger.getLogger(BansService.class.getName());

	@Inject
	private BanDao banDao;

	public List<Ban> getAllBans()
	{
		logger.log(Level.INFO, "Retrieving all Bans.");
		return banDao.findAll();
	}

	public List<Ban> getAllBansEagerUsers()
	{
		logger.log(Level.INFO, "Retrieving all Bans with eager loading of users.");
		return banDao.findAllEagerUsers();
	}

	public void deleteBan(Long banId)
	{
		logger.log(Level.INFO, "Deleting Ban with ID: {0}", banId);
		banDao.delete(banDao.find(banId));
	}

	public boolean isUserBanned(User user)
	{
		logger.log(Level.INFO, "Checking if user {0} is banned.", user.getName());
		List<Ban> bans = banDao.findAll();
		for(Ban ban : bans)
			if(ban.getBannedUser().equals(user)&&(ban.getEndDate()==null||ban.getEndDate().isAfter(LocalDateTime.now())))
			{
				logger.log(Level.INFO, "User {0} is currently banned.", user.getName());
				return true;
			}
		logger.log(Level.INFO, "User {0} is not banned.", user.getName());
		return false;
	}

	public void save(Ban ban)
	{
		logger.log(Level.INFO, "Saving Ban {0}", ban);
		banDao.save(ban);
	}
}