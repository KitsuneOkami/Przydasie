package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.Auction;
import org.example.util.AbstractDaoImpl;

import java.util.List;

@Stateless
public class AuctionDaoImpl extends AbstractDaoImpl<Auction, Long> implements AuctionDao
{
	@Override
	public Auction find(Long primaryKey)
	{
		return getEntityManager().find(Auction.class, primaryKey);
	}

	@Override
	public List<Auction> findAll()
	{
		return getEntityManager()
				.createQuery("SELECT a FROM Auction a", Auction.class)
				.getResultList();
	}
}