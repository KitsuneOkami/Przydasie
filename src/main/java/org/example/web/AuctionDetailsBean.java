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
import org.example.model.User;
import org.example.service.AuctionService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class AuctionDetailsBean implements Serializable
{
	private static final Logger logger = Logger.getLogger(AuctionDetailsBean.class.getName());
	@Inject
	private AuctionService auctionService;

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
		logger.log(Level.INFO, "Initializing AuctionDetailsBean with auction ID: {0}", id);
		if(id!=null)
		{
			auction = auctionService.getAuctionWithBids(id);
		}
		if(auction==null)
		{
			logger.log(Level.WARNING, "Auction with ID {0} could not be found.", id);
		}
	}

	public void placeBid()
	{
		logger.log(Level.INFO, "User {0} attempting to place bid on auction {1}", new Object[]{userSession.getUsername(), id});
		if(auction==null)
		{
			logger.log(Level.SEVERE, "Bid placement failed: Auction with ID {0} is null.", id);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Auction not found.", null));
			return;
		}

		if(auction.getStatus()!=AuctionStatus.ACTIVE)
		{
			logger.log(Level.WARNING, "Bid placement failed: Auction {0} is not active.", auction.getAuctionId());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Auction is not active.", null));
			return;
		}

		if(bidAmount==null||bidAmount.compareTo(BigDecimal.ZERO) <= 0)
		{
			logger.log(Level.WARNING, "Bid placement failed: Invalid bid amount of {0}.", bidAmount);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid bid amount.", null));
			return;
		}

		User user = userSession.getUser();
		if(user==null)
		{
			logger.log(Level.SEVERE, "Bid placement failed: User not logged in.");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not found.", null));
			return;
		}

		auctionService.addBidToAuction(auction.getAuctionId(), user, bidAmount);
		logger.log(Level.INFO, "Bid of {0} placed successfully on auction {1} by user {2}.", new Object[]{bidAmount, auction.getAuctionId(), user.getName()});
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Bid placed successfully.", null));
	}

	public void deleteAuction()
	{
		logger.log(Level.INFO, "Attempting to delete auction {0}", id);
		if(auction==null)
		{
			logger.log(Level.SEVERE, "Deletion failed: Auction with ID {0} not found.", id);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Auction not found.", null));
			return;
		}

		if(!userSession.isLoggedIn())
		{
			logger.log(Level.WARNING, "Deletion failed: User not logged in.");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must be logged in to delete an auction.", null));
			return;
		}

		User user = userSession.getUser();
		if(auction.getOwner().equals(user))
		{
			auctionService.deleteAuction(auction.getAuctionId());
			logger.log(Level.INFO, "Auction {0} deleted successfully by user {1}.", new Object[]{auction.getAuctionId(), user.getName()});
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Auction deleted successfully.", null));
			this.auction = null;
			try
			{
				logger.log(Level.INFO, "Redirecting to auctions page after deletion.");
				FacesContext.getCurrentInstance().getExternalContext().redirect("auctions.xhtml");
			} catch(Exception e)
			{
				logger.log(Level.SEVERE, "Failed to redirect to auctions page after deletion.", e);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error redirecting to auctions list.", null));
			}
		}
		else
		{
			logger.log(Level.WARNING, "User {0} is not authorized to delete auction {1}.", new Object[]{user.getName(), auction.getAuctionId()});
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