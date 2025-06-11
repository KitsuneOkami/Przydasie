package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.dao.BidDao;
import org.example.model.Bid;

import java.util.List;

@Stateless
public class BidService
{
	@Inject
	private BidDao bidDao;

	public void saveBid(Bid bid)
	{
		bidDao.save(bid);
	}

	public Bid getBid(Long id)
	{
		return bidDao.find(id);
	}

	public List<Bid> findAllBids()
	{
		return bidDao.findAll();
	}

	public boolean deleteBid(Long id)
	{
		Bid bid = getBid(id);
		if(bid==null)
		{
			return true;
		}

		bidDao.delete(bid);
		return true;
	}
}