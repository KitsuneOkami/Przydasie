package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "auction")
public class Auction implements Serializable
{
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

	@OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Bid> bids;

	//status types for auction
	public enum AuctionStatus
	{
		ACTIVE,
		ENDED
	}
}
