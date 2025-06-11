package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.PawnShop;
import org.example.util.AbstractDaoImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class PawnShopDaoImpl extends AbstractDaoImpl<PawnShop, Long> implements PawnShopDao
{
	private static final Logger logger = Logger.getLogger(PawnShopDaoImpl.class.getName());
	@Override
	public PawnShop find(Long primaryKey)
	{
		logger.log(Level.INFO, "Finding PawnShop with primary key: {0}", primaryKey);
		return getEntityManager().find(PawnShop.class, primaryKey);
	}
}