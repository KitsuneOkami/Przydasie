package org.example.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "auction")
public class Auction implements Serializable{
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

    @OneToOne
    private Item itemId;

    private String description;

    private LocalDate startTime;

    private LocalDate endTime;

    private AuctionStatus status;

    private BigDecimal startPrice;

    private BigDecimal reservePrice;

    //status types for auction
    enum AuctionStatus {
        ACTIVE,
        ENDED
    }
}
