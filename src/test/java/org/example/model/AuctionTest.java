package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class AuctionTest {

    private Auction auction;
    private Bid bid;

    @BeforeEach
    void setUp() {
        auction = new Auction();
        auction.setAuctionId(1L);
        auction.setBids(new HashSet<>());

        bid = new Bid();
        bid.setBidId(1L);
        bid.setBidAmount(BigDecimal.TEN);
    }

    @Test
    void removeBid_ShouldRemoveBidAndUnsetAuction() {
        // Arrange
        auction.addBid(bid);
        assertTrue(auction.getBids().contains(bid));
        assertEquals(auction, bid.getAuction());

        // Act
        auction.removeBid(bid);

        // Assert
        assertFalse(auction.getBids().contains(bid), "Bid should be removed from auction's bids");
        assertNull(bid.getAuction(), "Bid's auction reference should be null after removal");
    }
}
