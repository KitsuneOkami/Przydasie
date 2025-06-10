package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.dao.ItemDao;
import org.example.model.Item;

import java.util.List;

@Stateless
public class ItemService
{
	@Inject
	private ItemDao itemDao;

	public void saveItem(Item item)
	{
		itemDao.save(item);
	}

	public Item getItem(Long id)
	{
		return itemDao.find(id);
	}

	public boolean deleteItem(Long id)
	{
		Item item = getItem(id);
		if(item==null)
		{
			return true;
		}

		itemDao.delete(item);
		return true;
	}
}