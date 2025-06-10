package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.dao.PremiumUserDao;
import org.example.model.PremiumUser;

@Stateless
public class PremiumUserService
{
	@Inject
	private PremiumUserDao premiumUserDao;

	public void savePremiumUser(PremiumUser premiumUser)
	{
		premiumUserDao.save(premiumUser);
	}

	public PremiumUser getPremiumUser(Long id)
	{
		return premiumUserDao.find(id);
	}

	public boolean deletePremiumUser(Long id)
	{
		PremiumUser premiumUser = getPremiumUser(id);
		if(premiumUser==null)
		{
			return true;
		}

		premiumUserDao.delete(premiumUser);
		return true;
	}
}