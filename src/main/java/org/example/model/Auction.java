package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.logging.Logger;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "auction")
public class Auction implements Serializable
{
	private static final Logger logger = Logger.getLogger(Auction.class.getName());
    /*
    A-ID(PK): Unique identifier for each auction.
    I-ID(FK): Foreign key referencing item table.
    Description: Description of the auction.
    Start Time: Time when the auction starts.
    End Time: Time when the auction ends.
    Status: Status of the auction (e.g., active, ended).
    Starting Price: Initial price set for the auction.
    Reserve Price: Minimum price required for the auction to proceed.
    */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@ToString.Include
	private Long auctionId;

	@ToString.Include
	private String name;

	private String description;

	@ManyToOne(fetch = FetchType.EAGER)
	private User owner;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private AuctionStatus status;

	private BigDecimal startPrice;

	private BigDecimal reservePrice;

	@OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Bid> bids;

	public void addBid(Bid bid)
	{
		bids.add(bid);
		bid.setAuction(this); // maintain both sides
	}

	public void removeBid(Bid bid)
	{
		bids.remove(bid);
		bid.setAuction(null); // maintain both sides
	}

	//status types for auction
	public enum AuctionStatus
	{
		ACTIVE,
		ENDED
	}
}
