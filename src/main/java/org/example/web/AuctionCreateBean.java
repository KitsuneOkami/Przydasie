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
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class AuctionCreateBean
{
	private static final Logger logger = Logger.getLogger(AuctionCreateBean.class.getName());
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
		logger.log(Level.INFO, "User {0} attempting to create auction ''{1}''", new Object[]{userSession.getUsername(), name});
		User user = userSession.getUser();
		if(user==null)
		{
			logger.log(Level.SEVERE, "Auction creation failed: User not logged in.");
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
		logger.log(Level.INFO, "Auction ''{0}'' created successfully by user ''{1}''", new Object[]{name, user.getName()});

		try
		{
			JSFUtil.redirect("auctions.xhtml");
		} catch(IOException e)
		{
			logger.log(Level.SEVERE, "Error redirecting after auction creation.", e);
		}
	}
}