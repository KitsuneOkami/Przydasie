package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Item;
import org.example.service.ItemService;

@Named
@RequestScoped
public class ItemBean
{
	@Inject
	private ItemService itemService;

	@Setter
	@Getter
	private Item item = new Item();

	public void save()
	{
		itemService.saveItem(item);
		item = new Item();
	}
}