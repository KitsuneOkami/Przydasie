package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.dao.BuyerDao;
import org.example.model.Buyer;

import java.util.List;

@Stateless
public class BuyerService
{
	@Inject
	private BuyerDao buyerDao;

	public void saveBuyer(Buyer buyer)
	{
		buyerDao.save(buyer);
	}

	public Buyer getBuyer(Long id)
	{
		return buyerDao.find(id);
	}

	public boolean deleteBuyer(Long id)
	{
		Buyer buyer = getBuyer(id);
		if(buyer==null)
		{
			return true;
		}

		buyerDao.delete(buyer);
		return true;
	}
}