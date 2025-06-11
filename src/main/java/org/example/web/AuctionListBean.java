package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.model.Auction;
import org.example.service.AuctionService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class AuctionListBean
{
	private static final Logger logger = Logger.getLogger(AuctionListBean.class.getName());
	@Inject
	private AuctionService auctionService;

	public List<Auction> getAuctions()
	{
		logger.log(Level.INFO, "Fetching all auctions");
		return auctionService.findAllAuctions();
	}
}