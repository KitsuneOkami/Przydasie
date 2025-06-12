package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Auction;
import org.example.model.PawnShopItem;
import org.example.service.AuctionService;
import org.example.service.PawnShopItemService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class PawnShopListBean
{
	private static final Logger logger = Logger.getLogger(PawnShopListBean.class.getName());

	@Inject
	private PawnShopItemService pawnShopItemService;

	@Inject
	private AuctionService auctionService;

	@Inject
	private UserSessionBean userSessionBean;

	@Getter
	@Setter
	private int days = 0;
	@Getter
	@Setter
	private int hours = 0;
	@Getter
	@Setter
	private int minutes = 0;
	@Getter
	@Setter
	private int seconds = 0;

	public List<PawnShopItem> getPawnShopItems()
	{
		logger.log(Level.INFO, "Fetching all pawn shop items.");
		var user = userSessionBean.getUser();
		if(user==null)
			return List.of();

		return pawnShopItemService.getItemsByOwnerId(user.getUserId());
	}

	@Transactional
	public void convertToAuction(PawnShopItem item)
	{
		logger.log(Level.INFO, "Converting PawnShopItem {0} to Auction.", item.getItemId());
		LocalDateTime endTime = LocalDateTime.now()
				.plusDays(days)
				.plusHours(hours)
				.plusMinutes(minutes)
				.plusSeconds(seconds);

		Auction auction = pawnShopItemService.toAuction(item, endTime);
		auctionService.saveAuction(auction);
		pawnShopItemService.deleteItem(item.getItemId());
		logger.log(Level.INFO, "PawnShopItem {0} converted to Auction successfully.", item.getItemId());
	}

	public void removeItem(PawnShopItem item)
	{
		logger.log(Level.INFO, "Removing PawnShopItem {0}.", item.getItemId());
		pawnShopItemService.deleteItem(item.getItemId());
		logger.log(Level.INFO, "PawnShopItem {0} removed successfully.", item.getItemId());
	}
}