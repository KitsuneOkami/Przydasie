package org.example.email;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.model.Auction;
import org.example.model.Auction.AuctionStatus;
import org.example.model.Bid;
import org.example.service.AuctionService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Singleton
@Startup
public class AuctionMonitor
{
	private static final Logger logger = Logger.getLogger(AuctionMonitor.class.getName());

	@Inject
	private EmailService emailService;

	@Inject
	private AuctionService auctionService;

	@Schedule(hour = "*", minute = "*", second = "*/15", persistent = false) // runs every 1 minute
	@Transactional
	public void checkAuctions()
	{
		logger.info("Checking for auctions that have ended...");
		List<Auction> winningAuctions = auctionService.getAllWinningAuctions();
		for(Auction auction : winningAuctions)
		{
			logger.info("Found winning action "+auction);

			Optional<Bid> highestBid = auctionService.getHighestBid(auction.getAuctionId());
			highestBid.ifPresent(bid -> emailService.sendAuctionWinEmail(bid.getBidder().getEmail(), auction));

			auction.setStatus(AuctionStatus.ENDED);
			auctionService.updateAuction(auction);
		}
	}
}
