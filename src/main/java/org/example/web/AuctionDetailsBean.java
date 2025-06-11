package org.example.web;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Auction;
import org.example.model.Auction.AuctionStatus;
import org.example.model.Bid;
import org.example.model.User;
import org.example.service.AuctionService;
import org.example.service.BidService;

import java.io.Serializable;
import java.math.BigDecimal;

@Named
@ViewScoped
public class AuctionDetailsBean implements Serializable
{
	@Inject
	private AuctionService auctionService;

	@Inject
	private BidService bidService;

	@Inject
	private UserSessionBean userSession;

	@Getter
	@Setter
	private Long id;

	@Getter
	private Auction auction;

	@Getter
	@Setter
	private BigDecimal bidAmount;

	@PostConstruct
	public void init()
	{
		if(id!=null)
			auction = auctionService.getAuction(id);
	}

	public void placeBid()
	{
		// Check if the auction exists
		if(auction==null)
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Auction not found.", null));
			return;
		}

		// An auction must be active
		if(auction.getStatus()!=AuctionStatus.ACTIVE)
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Auction is not active.", null));
			return;
		}

		// Bid amount must be greater than zero
		if(bidAmount==null||bidAmount.compareTo(BigDecimal.ZERO) <= 0)
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid bid amount.", null));
			return;
		}

		// Bid must have an owner
		User user = userSession.getUser();
		if(user==null)
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not found.", null));
			return;
		}

		// Create and save the bid
		Bid newBid = new Bid();
		newBid.setBidAmount(bidAmount);
		newBid.setBidder(user);
		newBid.setAuction(auction);
		bidService.saveBid(newBid);

		auction.getBids().add(newBid);
		auctionService.saveAuction(auction);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Bid placed successfully.", null));
	}

	public void deleteAuction()
	{
		System.out.println("Delete auction");
		if(auction==null)
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Auction not found.", null));
			return;
		}

		if(!userSession.isLoggedIn())
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must be logged in to delete an auction.", null));
			return;
		}

		User user = userSession.getUser();
		if(auction.getOwner().equals(user))
		{
			auctionService.deleteAuction(auction.getAuctionId());
			System.out.println("Deleting auction 2: "+auction);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Auction deleted successfully.", null));
			this.auction = null;
			// Redirect to auctions list
			try
			{
				System.out.println("Deleting auction 3");
				FacesContext.getCurrentInstance().getExternalContext().redirect("auctions.xhtml");
			} catch(Exception e)
			{
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error redirecting to auctions list.", null));
			}
		}
		else
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "You are not authorized to delete this auction.", null));
		}
	}

	public boolean isAuctionOwner()
	{
		return auction!=null&&userSession.getUser().equals(auction.getOwner());
	}

	public boolean canPlaceBid()
	{
		return auction!=null&&userSession.isLoggedIn()
				&&!isAuctionOwner()&&auction.getStatus()==AuctionStatus.ACTIVE;
	}
}
