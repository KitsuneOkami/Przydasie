package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Auction;
import org.example.model.Auction.AuctionStatus;
import org.example.model.User;
import org.example.service.AuctionService;
import org.example.util.JSFUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Named
@RequestScoped
public class AuctionCreateBean
{
	@Inject
	private AuctionService auctionService;

	@Inject
	private UserSessionBean userSession;

	@Getter
	@Setter
	private String name, description;

	@Getter
	@Setter
	private LocalDateTime startTime, endTime;

	@Getter
	@Setter
	private BigDecimal startPrice;

	public void save()
	{
		User user = userSession.getUser();
		if(user==null)
		{
			// Error msg
			return;
		}

		Auction auction = new Auction();
		auction.setName(name);
		auction.setDescription(description);
		auction.setStartTime(startTime);
		auction.setEndTime(endTime);
		auction.setStartPrice(startPrice);
		auction.setOwner(user);
		auction.setStatus(AuctionStatus.ACTIVE);

		auctionService.saveAuction(auction);

		// Redirect to list
		try
		{
			JSFUtil.redirect("auctions.xhtml");
		} catch(IOException e)
		{

		}
	}
}
