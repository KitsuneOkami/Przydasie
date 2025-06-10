package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Auction;
import org.example.service.AuctionService;

import java.util.List;

@Named
@RequestScoped
public class AuctionBean
{
	@Inject
	private AuctionService auctionService;

	@Setter
	@Getter
	private Auction auction = new Auction();

	public List<Auction> getAuctions()
	{
		return auctionService.findAllAuctions();
	}

	public void save()
	{
		auctionService.saveAuction(auction);
		auction = new Auction();
	}
}