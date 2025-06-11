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

	public void savePawnShop(PawnShop pawnShop)
	{
		pawnShopDao.save(pawnShop);
	}

	public PawnShop getPawnShop(Long id)
	{
		return pawnShopDao.find(id);
	}

	public boolean deletePawnShop(Long id)
	{
		PawnShop pawnShop = getPawnShop(id);
		if(pawnShop==null)
		{
			return true;
		}

		pawnShopDao.delete(pawnShop);
		return true;
	}
}