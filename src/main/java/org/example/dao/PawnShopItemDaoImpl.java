package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.PawnShopItem;
import org.example.util.AbstractDaoImpl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class PawnShopItemDaoImpl extends AbstractDaoImpl<PawnShopItem, Long> implements PawnShopItemDao
{
	private static final Logger logger = Logger.getLogger(PawnShopItemDaoImpl.class.getName());

	@Override
	public PawnShopItem find(Long primaryKey)
	{
		logger.log(Level.INFO, "Finding PawnShopItem with primary key: {0}", primaryKey);
		return getEntityManager().find(PawnShopItem.class, primaryKey);
	}

	@Override
	public List<PawnShopItem> findAll()
	{
		logger.log(Level.INFO, "Finding all PawnShopItems.");
		return getEntityManager()
				.createQuery("SELECT p FROM PawnShopItem p", PawnShopItem.class)
				.getResultList();
	}

	@Override
	public List<PawnShopItem> findByOwnerId(Long ownerId)
	{
		logger.log(Level.INFO, "Finding PawnShopItems for owner ID: {0}", ownerId);
		return getEntityManager()
				.createQuery("SELECT p FROM PawnShopItem p WHERE p.owner.id = :ownerId", PawnShopItem.class)
				.setParameter("ownerId", ownerId)
				.getResultList();
	}
}