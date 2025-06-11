package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.Auction;
import org.example.model.Bid;
import org.example.model.User;
import org.example.util.AbstractDaoImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class AuctionDaoImpl extends AbstractDaoImpl<Auction, Long> implements AuctionDao
{
	private static final Logger logger = Logger.getLogger(AuctionDaoImpl.class.getName());

	@Override
	public Auction find(Long primaryKey)
	{
		logger.log(Level.INFO, "Finding Auction with primary key: {0}", primaryKey);
		return getEntityManager().find(Auction.class, primaryKey);
	}

	@Override
	public List<Auction> findAll()
	{
		logger.log(Level.INFO, "Finding all Auctions.");
		return getEntityManager()
				.createQuery("SELECT a FROM Auction a", Auction.class)
				.getResultList();
	}

	@Override
	public void addBid(Long auctionId, User user, BigDecimal bidAmount)
	{
		Auction auction = getEntityManager().find(Auction.class, auctionId);
		if(auction==null)
		{
			throw new IllegalArgumentException("Auction not found");
		}

		// Initialize collection to avoid lazy init conflict
		auction.getBids().size(); // force init before modifying

		Bid newBid = new Bid();
		newBid.setBidAmount(bidAmount);
		newBid.setBidder(user);

		auction.addBid(newBid); // this sets both sides
		// No need to merge if auction is managed
		// But to be safe:
		getEntityManager().merge(auction);
	}

	@Override
	public Auction findWithBids(Long auctionId) {
		return getEntityManager()
				.createQuery("""
						SELECT DISTINCT a FROM Auction a
						LEFT JOIN FETCH a.bids
						LEFT JOIN FETCH a.bids.bidder
						WHERE a.auctionId = :id
						""", Auction.class)
				.setParameter("id", auctionId)
				.getSingleResult();
	}



}