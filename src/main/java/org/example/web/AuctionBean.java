package org.example.web;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Auction;
import org.example.model.Auction.AuctionStatus;
import org.example.model.User;
import org.example.service.AuctionService;
import org.example.service.UserService;
import org.example.util.JSFUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Named
@RequestScoped
public class AuctionBean
{
	@Inject
	private AuctionService auctionService;

	@Inject
	private UserService userService;

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
	private Auction auction = new Auction(); // For details.xhtml loading only

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

	public void deleteAuction()
	{
		if(auction==null)
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Auction not found.", null));
			return;
		}

		User user = userService.findByName(FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("username").toString());
		if(auction.getOwner().equals(user))
		{
			auctionService.deleteAuction(auction.getAuctionId());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Auction deleted successfully.", null));
			// Redirect to auctions list
			try
			{
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
		String currentUsername = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("username").toString();
		return auction.getOwner().getName().equals(currentUsername);
	}

	public void save()
	{
		String currentUsername = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("username").toString();

		Auction newAuction = new Auction();
		newAuction.setName(name);
		newAuction.setDescription(description);
		newAuction.setStartTime(startTime);
		newAuction.setEndTime(endTime);
		newAuction.setStartPrice(startPrice);
		newAuction.setStatus(AuctionStatus.ACTIVE);

		//Set the owner of the auction
		newAuction.setOwner(userService.findByName(currentUsername));

		//Save the auction
		auctionService.saveAuction(newAuction);

		// Optionally clear form fields after saving
		name = "";
		description = "";
		startTime = null;
		endTime = null;

		try
		{
			JSFUtil.redirect("auctions.xhtml");
		} catch(IOException ignored)
		{

		}
	}
}