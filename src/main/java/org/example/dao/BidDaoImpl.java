package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.Bid;
import org.example.util.AbstractDaoImpl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class BidDaoImpl extends AbstractDaoImpl<Bid, Long> implements BidDao
{
	private static final Logger logger = Logger.getLogger(BidDaoImpl.class.getName());
	@Override
	public List<Bid> findAll()
	{
		logger.log(Level.INFO, "Finding all Bids.");
		return getEntityManager()
				.createQuery("SELECT b FROM Bid b", Bid.class)
				.getResultList();
	}

	@Override
	public Bid find(Long primaryKey)
	{
		logger.log(Level.INFO, "Finding Bid with primary key: {0}", primaryKey);
		return getEntityManager().find(Bid.class, primaryKey);
	}
}