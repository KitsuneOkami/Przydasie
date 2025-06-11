package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.model.Auction;
import org.example.service.AuctionService;

import java.util.List;

@Named
@RequestScoped
public class AuctionListBean
{
	@Inject
	private AuctionService auctionService;

	public List<Auction> getAuctions()
	{
		return auctionService.findAllAuctions();
	}
}
