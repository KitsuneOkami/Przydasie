package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.Item;
import org.example.util.AbstractDaoImpl;

@Stateless
public class ItemDaoImpl extends AbstractDaoImpl<Item, Long> implements ItemDao
{
	@Override
	public Item find(Long primaryKey)
	{
		return getEntityManager().find(Item.class, primaryKey);
	}
}