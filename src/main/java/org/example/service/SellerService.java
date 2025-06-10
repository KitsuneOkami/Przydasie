package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.dao.SellerDao;
import org.example.model.Seller;

@Stateless
public class SellerService
{
	@Inject
	private SellerDao sellerDao;

	public void saveSeller(Seller seller)
	{
		sellerDao.save(seller);
	}

	public Seller getSeller(Long id)
	{
		return sellerDao.find(id);
	}

	public boolean deleteSeller(Long id)
	{
		Seller seller = getSeller(id);
		if(seller==null)
		{
			return true;
		}

		sellerDao.delete(seller);
		return true;
	}
}