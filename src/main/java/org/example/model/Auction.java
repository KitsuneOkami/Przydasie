package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Data
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
	private Long auctionId;

	private String name;

	private String description;

	@ManyToOne(fetch = FetchType.EAGER)
	private User owner;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private AuctionStatus status;

	private BigDecimal startPrice;

	private BigDecimal reservePrice;

	@OneToMany(mappedBy = "auction", fetch = FetchType.EAGER)
	private List<Bid> bids;

	//status types for auction
	public enum AuctionStatus
	{
		ACTIVE,
		ENDED
	}
}
