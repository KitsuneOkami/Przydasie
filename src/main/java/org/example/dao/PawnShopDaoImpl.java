package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.PawnShop;
import org.example.util.AbstractDaoImpl;

@Stateless
public class PawnShopDaoImpl extends AbstractDaoImpl<PawnShop, Long> implements PawnShopDao
{
	@Override
	public PawnShop find(Long primaryKey)
	{
		return getEntityManager().find(PawnShop.class, primaryKey);
	}
}