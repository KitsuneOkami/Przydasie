package org.example.dao;

import org.example.model.Auction;
import org.example.model.Bid;
import org.example.model.User;
import org.example.util.AbstractDao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AuctionDao extends AbstractDao<Auction, Long>
{
	List<Auction> findAll();

	void addBid(Long auctionId, User user, BigDecimal bidAmount);

	Auction findWithBids(Long id);

	List<Auction> findAllWinningAuctions();

	Optional<Bid> findHighestBid(Long auctionId);
}