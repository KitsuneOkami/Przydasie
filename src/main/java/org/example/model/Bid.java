package org.example.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.logging.Logger;

@Data
@Entity
@Table(name = "bid")
public class Bid implements Serializable {
    private static final Logger logger = Logger.getLogger(Bid.class.getName());
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;
    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;
    @ManyToOne
    private User bidder;

    private BigDecimal bidAmount;
}