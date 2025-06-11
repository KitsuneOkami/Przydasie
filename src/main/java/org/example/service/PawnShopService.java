package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.dao.PawnShopDao;
import org.example.model.PawnShop;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class PawnShopService
{
	private static final Logger logger = Logger.getLogger(PawnShopService.class.getName());
	@Inject
	private PawnShopDao pawnShopDao;
	public void savePawnShop(PawnShop pawnShop)
	{
		logger.log(Level.INFO, "Saving pawn shop: {0}", pawnShop);
		pawnShopDao.save(pawnShop);
	}

	public PawnShop getPawnShop(Long id)
	{
		logger.log(Level.INFO, "Retrieving pawn shop with ID: {0}", id);
		return pawnShopDao.find(id);
	}

	public boolean deletePawnShop(Long id)
	{
		logger.log(Level.INFO, "Deleting pawn shop with ID: {0}", id);
		PawnShop pawnShop = getPawnShop(id);
		if(pawnShop==null)
		{
			logger.log(Level.WARNING, "PawnShop with ID {0} not found for deletion.", id);
			return true;
		}

		pawnShopDao.delete(pawnShop);
		logger.log(Level.INFO, "PawnShop with ID {0} deleted successfully.", id);
		return true;
	}
}