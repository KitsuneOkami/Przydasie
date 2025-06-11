package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Auction;
import org.example.service.AuctionService;
import org.example.service.BidService;

import java.util.List;

@Named
@RequestScoped
public class AuctionBean
{
	@Inject
	private AuctionService auctionService;

	@Inject
	private BidService bidService;

	@Setter
	@Getter
	private Auction auction = new Auction();

	public List<Auction> getAuctions()
	{
		return auctionService.findAllAuctions();
	}

	public String viewDetails(Long id)
	{
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("id", id);
		return "auction_details.xhtml";
	}

	public void save()
	{
		auctionService.saveAuction(auction);
		auction = new Auction();
	}
}