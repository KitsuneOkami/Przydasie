package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.dao.AuctionDao;
import org.example.model.Auction;
import org.example.model.Bid;

import java.util.List;

@Stateless
public class AuctionService
{
	@Inject
	private AuctionDao auctionDao;

	public void saveAuction(Auction auction)
	{
		auctionDao.save(auction);
	}

	public Auction getAuction(Long id)
	{
		return auctionDao.find(id);
	}

	public List<Auction> findAllAuctions()
	{
		return auctionDao.findAll();
	}

	public List<Bid> getAllAuctionBids(Long auctionId)
	{
		Auction auction = getAuction(auctionId);
		if(auction==null)
		{
			return List.of(); //Return an empty list if the auction does not exist
		}
		return auction.getBids();
	}

	public boolean deleteAuction(Long id)
	{
		Auction auction = getAuction(id);
		if(auction==null)
		{
			return true;
		}

		auctionDao.delete(auction);
		return true;
	}
}