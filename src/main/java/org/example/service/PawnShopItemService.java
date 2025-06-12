package org.example.service;

import jakarta.inject.Inject;
import org.example.dao.PawnShopItemDao;
import org.example.model.Auction;
import org.example.model.PawnShopItem;

import java.time.LocalDateTime;
import java.util.List;

public class PawnShopItemService
{
	@Inject
	private PawnShopItemDao auctionDao;

	public List<PawnShopItem> getItemsByOwnerId(Long ownerId)
	{
		return auctionDao.findByOwnerId(ownerId);
	}

	public void saveItem(PawnShopItem item)
	{
		auctionDao.save(item);
	}

	public void deleteItem(Long itemId)
	{
		PawnShopItem item = auctionDao.find(itemId);
		if(item!=null)
			auctionDao.delete(item);
	}

	public Auction toAuction(PawnShopItem item, LocalDateTime endTime)
	{
		Auction auction = new Auction();
		auction.setName(item.getName());
		auction.setDescription(item.getDescription());
		auction.setOwner(item.getOwner());
		auction.setStartPrice(item.getPrice());
		auction.setReservePrice(item.getPrice());
		auction.setStartTime(LocalDateTime.now());
		auction.setEndTime(endTime);
		auction.setStatus(Auction.AuctionStatus.ACTIVE);
		return auction;
	}
}