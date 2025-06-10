package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.Buyer;
import org.example.util.AbstractDaoImpl;

@Stateless
public class BuyerDaoImpl extends AbstractDaoImpl<Buyer, Long> implements BuyerDao
{
	@Override
	public Buyer find(Long primaryKey)
	{
		return getEntityManager().find(Buyer.class, primaryKey);
	}
}