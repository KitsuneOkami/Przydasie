package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.dao.BidDao;
import org.example.model.Bid;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class BidService
{
	private static final Logger logger = Logger.getLogger(BidService.class.getName());
	@Inject
	private BidDao bidDao;

	public void saveBid(Bid bid)
	{
		logger.log(Level.INFO, "Saving bid: {0}", bid);
		bidDao.save(bid);
	}

	public Bid getBid(Long id)
	{
		logger.log(Level.INFO, "Retrieving bid with ID: {0}", id);
		return bidDao.find(id);
	}

	public List<Bid> findAllBids()
	{
		logger.log(Level.INFO, "Retrieving all bids.");
		return bidDao.findAll();
	}

	public boolean deleteBid(Long id)
	{
		logger.log(Level.INFO, "Deleting bid with ID: {0}", id);
		Bid bid = getBid(id);
		if(bid==null)
		{
			logger.log(Level.WARNING, "Bid with ID {0} not found for deletion.", id);
			return true;
		}

		bidDao.delete(bid);
		logger.log(Level.INFO, "Bid with ID {0} deleted successfully.", id);
		return true;
	}
}
