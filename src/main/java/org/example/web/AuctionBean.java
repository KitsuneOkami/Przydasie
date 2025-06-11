package org.example.web;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Auction;
import org.example.model.Auction.AuctionStatus;
import org.example.model.Bid;
import org.example.model.User;
import org.example.service.AuctionService;
import org.example.util.JSFUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Named
@RequestScoped
@Stateful
public class AuctionBean
{
	@Inject
	private AuctionService auctionService;

	@Inject
	private UserSessionBean userSession;

	@Setter
	@Getter
	private String name;

	@Setter
	@Getter
	private String description;

	@Setter
	@Getter
	private LocalDateTime startTime;

	@Setter
	@Getter
	private LocalDateTime endTime;

	@Setter
	@Getter
	private BigDecimal startPrice;

	@Setter
	@Getter
	private BigDecimal bidAmount;

	@Setter
	@Getter
	private Auction auction = new Auction();

	public List<Auction> getAuctions()
	{
		return auctionService.findAllAuctions();
	}


	@PostConstruct
	public void init()
	{
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();

		String idParam = params.get("id");
		if(idParam!=null)
		{
			try
			{
				System.out.println("ID: "+idParam);
				long id = Long.parseLong(idParam);
				this.auction = auctionService.getAuction(id);
			} catch(NumberFormatException ignored) {}
		}
	}

	public void deleteAuction(Long auctionId)
	{
		System.out.println("Delete auction");
		Auction auction = auctionService.getAuction(auctionId);

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

	public boolean isAuctionOwner(Long auctionId)
	{
		Auction auction = auctionService.getAuction(auctionId);
		return auction!=null&&auction.getOwner().equals(userSession.getUser());
	}

	public void save()
	{
		//Auction must have an owner owner
		User user = userSession.getUser();
		if(user==null)
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must be logged in to create an auction.", null));
			return;
		}

		//Save the auction
		Auction newAuction = new Auction();
		newAuction.setName(name);
		newAuction.setDescription(description);
		newAuction.setStartTime(startTime);
		newAuction.setEndTime(endTime);
		newAuction.setStartPrice(startPrice);
		newAuction.setStatus(AuctionStatus.ACTIVE);
		newAuction.setOwner(user);
		auctionService.saveAuction(newAuction);

		//Clear form fields after saving
		name = "";
		description = "";
		startTime = null;
		endTime = null;
		startPrice = null;
		bidAmount = null;

		try
		{
			JSFUtil.redirect("auctions.xhtml");
		} catch(IOException ignored)
		{

		}
	}

	public void placeBid(Long auctionId)
	{
		// Reload the auction using the auction ID
		this.auction = auctionService.getAuction(auctionId);

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

		auction.getBids().add(newBid);
		auctionService.saveAuction(auction);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Bid placed successfully.", null));
	}

	public Boolean canPlaceBid(Long auctionId)
	{
		Auction auction = auctionService.getAuction(auctionId);
		return userSession.isLoggedIn()&&auction!=null
				&&!userSession.getUser().equals(auction.getOwner())
				&&auction.getStatus()==AuctionStatus.ACTIVE;
	}
}