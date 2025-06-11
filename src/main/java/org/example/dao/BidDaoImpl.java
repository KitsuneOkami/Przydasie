package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.Bid;
import org.example.util.AbstractDaoImpl;

import java.util.List;

@Stateless
public class BidDaoImpl extends AbstractDaoImpl<Bid, Long> implements BidDao
{
	@Override
	public List<Bid> findAll()
	{
		return getEntityManager()
				.createQuery("SELECT b FROM Bid b", Bid.class)
				.getResultList();
	}

	@Override
	public Bid find(Long primaryKey)
	{
		return getEntityManager().find(Bid.class, primaryKey);
	}
}