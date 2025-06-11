package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.dao.AuctionDao;
import org.example.model.Auction;
import org.example.model.Bid;
import org.example.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class AuctionService
{
	private static final Logger logger = Logger.getLogger(AuctionService.class.getName());
	@Inject
	private AuctionDao auctionDao;

	public void saveAuction(Auction auction)
	{
		logger.log(Level.INFO, "Saving auction: {0}", auction);
		auctionDao.save(auction);
	}

	public Auction getAuction(Long id)
	{
		logger.log(Level.INFO, "Retrieving auction with ID: {0}", id);
		return auctionDao.find(id);
	}

	public List<Auction> findAllAuctions()
	{
		logger.log(Level.INFO, "Retrieving all auctions.");
		return auctionDao.findAll();
	}

	public Set<Bid> getAllAuctionBids(Long auctionId)
	{
		logger.log(Level.INFO, "Retrieving all bids for auction ID: {0}", auctionId);
		Auction auction = getAuction(auctionId);
		if(auction==null)
		{
			logger.log(Level.WARNING, "Auction with ID {0} not found. Cannot retrieve bids.", auctionId);
			return Set.of();
		}
		return auction.getBids();
	}

	public boolean deleteAuction(Long id)
	{
		logger.log(Level.INFO, "Deleting auction with ID: {0}", id);
		Auction auction = getAuction(id);
		if(auction==null) {
			logger.log(Level.WARNING, "Auction with ID {0} not found for deletion.", id);
			return true;
		}

		auctionDao.delete(auction);
		logger.log(Level.INFO, "Auction with ID {0} deleted successfully.", id);
		return true;
	}

	public void addBidToAuction(Long auctionId, User user, BigDecimal bidAmount)
	{
		auctionDao.addBid(auctionId, user, bidAmount);
	}

	public Auction getAuctionWithBids(Long id)
	{
		return auctionDao.findWithBids(id);
	}
}