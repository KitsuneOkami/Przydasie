package org.example.dao;

import org.example.model.Auction;
import org.example.util.AbstractDao;

import java.util.List;

public interface AuctionDao extends AbstractDao<Auction, Long>
{
	List<Auction> findAll();
}