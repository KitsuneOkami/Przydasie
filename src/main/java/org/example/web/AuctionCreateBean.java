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

	public void save() {
		User user = userSession.getUser();
		if (user == null) {
			JSFUtil.addErrorMessage("User must be logged in");
			return;
		}

		if (!validateInput()) {
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

		try {
			auctionService.saveAuction(auction);
			JSFUtil.redirect("auctions.xhtml");
		} catch (IOException e) {
			JSFUtil.addErrorMessage("Error saving auction: " + e.getMessage());
			// Consider logging the exception
		}
	}

	private boolean validateInput() {
		if (name == null || name.trim().isEmpty()) {
			JSFUtil.addErrorMessage("Name is required");
			return false;
		}

		if (startTime == null || endTime == null) {
			JSFUtil.addErrorMessage("Start and end times are required");
			return false;
		}

		if (startTime.isAfter(endTime)) {
			JSFUtil.addErrorMessage("Start time must be before end time");
			return false;
		}

		if (startPrice == null || startPrice.compareTo(BigDecimal.ZERO) <= 0) {
			JSFUtil.addErrorMessage("Start price must be positive");
			return false;
		}

		return true;
	}
}
