package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.dao.PawnShopDao;
import org.example.model.PawnShop;

@Stateless
public class PawnShopService
{
	@Inject
	private PawnShopDao pawnShopDao;

	public void savePremiumUser(PawnShop premiumUser)
	{
		pawnShopDao.save(premiumUser);
	}

	public PawnShop getPremiumUser(Long id)
	{
		return pawnShopDao.find(id);
	}

	public boolean deletePremiumUser(Long id)
	{
		PawnShop premiumUser = getPremiumUser(id);
		if(premiumUser==null)
		{
			return true;
		}

		pawnShopDao.delete(premiumUser);
		return true;
	}
}