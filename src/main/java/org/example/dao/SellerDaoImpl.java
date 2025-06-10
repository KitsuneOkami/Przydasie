package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.Seller;
import org.example.util.AbstractDaoImpl;

@Stateless
public class SellerDaoImpl extends AbstractDaoImpl<Seller, Long> implements SellerDao
{
	@Override
	public Seller find(Long primaryKey)
	{
		return getEntityManager().find(Seller.class, primaryKey);
	}
}