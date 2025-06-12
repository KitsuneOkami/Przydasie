package org.example.email;

import org.example.model.Auction;
import org.example.model.Auction.AuctionStatus;
import org.example.model.Bid;
import org.example.service.AuctionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionMonitorTest {

    private AuctionMonitor auctionMonitor;
    private EmailService emailService;
    private AuctionService auctionService;

    @BeforeEach
    void setUp() throws Exception {
        emailService = mock(EmailService.class);
        auctionService = mock(AuctionService.class);

        auctionMonitor = new AuctionMonitor();

        // Inject mocks via reflection since fields are private and no setters
        var emailField = AuctionMonitor.class.getDeclaredField("emailService");
        emailField.setAccessible(true);
        emailField.set(auctionMonitor, emailService);

        var auctionServiceField = AuctionMonitor.class.getDeclaredField("auctionService");
        auctionServiceField.setAccessible(true);
        auctionServiceField.set(auctionMonitor, auctionService);
    }

    @Test
    void checkAuctions_noWinningAuctions_noEmailsSent() {
        when(auctionService.getAllWinningAuctions()).thenReturn(List.of());

        auctionMonitor.checkAuctions();

        verify(auctionService).getAllWinningAuctions();
        verifyNoMoreInteractions(auctionService);
        verifyNoInteractions(emailService);
    }

    @Test
    void checkAuctions_winningAuctionWithoutHighestBid_doesNotSendEmailButUpdatesStatus() {
        Auction auction = mock(Auction.class);
        when(auction.getAuctionId()).thenReturn(1L);

        when(auctionService.getAllWinningAuctions()).thenReturn(List.of(auction));
        when(auctionService.getHighestBid(1L)).thenReturn(Optional.empty());

        auctionMonitor.checkAuctions();

        verify(auctionService).getAllWinningAuctions();
        verify(auctionService).getHighestBid(1L);

        verify(emailService, never()).sendAuctionWinEmail(anyString(), any());

        verify(auction).setStatus(AuctionStatus.ENDED);
        verify(auctionService).updateAuction(auction);
    }
}
