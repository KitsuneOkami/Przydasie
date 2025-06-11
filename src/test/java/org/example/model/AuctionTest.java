package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class AuctionTest {

    @Mock
    private Auction activeAuction;
    private Auction endedAuction;

    @BeforeEach
    void setUp() {
        // Initialize test data
        activeAuction = new Auction();
        activeAuction.setAuctionId(1L);
        activeAuction.setStatus(Auction.AuctionStatus.ACTIVE);

        endedAuction = new Auction();
        endedAuction.setAuctionId(2L);
        activeAuction.setStatus(Auction.AuctionStatus.ENDED);
    }
}
