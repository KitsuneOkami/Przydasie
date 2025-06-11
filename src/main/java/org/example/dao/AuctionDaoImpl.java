package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.Auction;
import org.example.util.AbstractDaoImpl;

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
}