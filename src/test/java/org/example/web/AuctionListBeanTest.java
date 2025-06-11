package org.example.web;

import org.example.model.Auction;
import org.example.service.AuctionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionListBeanTest {

    private AuctionListBean auctionListBean;
    private AuctionService auctionService;

    @BeforeEach
    void setUp() {
        auctionListBean = new AuctionListBean();
        auctionService = mock(AuctionService.class);

        // Inject mock
        try {
            var field = AuctionListBean.class.getDeclaredField("auctionService");
            field.setAccessible(true);
            field.set(auctionListBean, auctionService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAuctions_ShouldReturnListFromService() {
        List<Auction> auctions = List.of(new Auction(), new Auction());
        when(auctionService.findAllAuctions()).thenReturn(auctions);

        List<Auction> result = auctionListBean.getAuctions();

        assertEquals(auctions, result);
        verify(auctionService).findAllAuctions();
    }
}
