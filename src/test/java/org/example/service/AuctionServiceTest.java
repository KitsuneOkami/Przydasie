package org.example.service;

import org.example.dao.AuctionDao;
import org.example.model.Auction;
import org.example.model.Bid;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock
    private AuctionDao auctionDao;

    @InjectMocks
    private AuctionService auctionService;

    private Auction auction1;
    private Auction auction2;
    private Set<Bid> bids;

    @BeforeEach
    void setUp() {
        // Initialize test data
        auction1 = new Auction();
        auction1.setAuctionId(1L);
        auction1.setDescription("Test Auction 1");
        
        auction2 = new Auction();
        auction2.setAuctionId(2L);
        auction2.setDescription("Test Auction 2");

        bids = new HashSet<>();
        Bid bid1 = new Bid();
        bid1.setBidId(1L);
        bid1.setBidAmount(new BigDecimal("100.00"));
        
        Bid bid2 = new Bid();
        bid2.setBidId(2L);
        bid2.setBidAmount(new BigDecimal("150.00"));
        
        bids.addAll(Arrays.asList(bid1, bid2));
        auction1.setBids(bids);
    }

    @Test
    void saveAuction_ShouldCallDaoSave() {
        // Act
        auctionService.saveAuction(auction1);

        // Assert
        verify(auctionDao).save(auction1);
    }

    @Test
    void getAuction_WhenAuctionExists_ShouldReturnAuction() {
        // Arrange
        when(auctionDao.find(1L)).thenReturn(auction1);

        // Act
        Auction result = auctionService.getAuction(1L);

        // Assert
        assertNotNull(result);
        assertEquals(auction1, result);
        verify(auctionDao).find(1L);
    }

    @Test
    void getAuction_WhenAuctionDoesNotExist_ShouldReturnNull() {
        // Arrange
        when(auctionDao.find(999L)).thenReturn(null);

        // Act
        Auction result = auctionService.getAuction(999L);

        // Assert
        assertNull(result);
        verify(auctionDao).find(999L);
    }

    @Test
    void getAuctionWithBids_ShouldReturnAuctionFromDao() {
        Long auctionId = 1L;
        Auction auctionWithBids = new Auction();
        auctionWithBids.setAuctionId(auctionId);

        when(auctionDao.findWithBids(auctionId)).thenReturn(auctionWithBids);

        Auction result = auctionService.getAuctionWithBids(auctionId);

        assertNotNull(result);
        assertEquals(auctionWithBids, result);
        verify(auctionDao).findWithBids(auctionId);
    }

    @Test
    void addBidToAuction_ShouldDelegateToDao() {
        Long auctionId = 1L;
        User user = new User();
        user.setUserId(10L);
        BigDecimal bidAmount = BigDecimal.valueOf(200);

        auctionService.addBidToAuction(auctionId, user, bidAmount);

        verify(auctionDao).addBid(auctionId, user, bidAmount);
    }

    @Test
    void findAllAuctions_ShouldReturnAllAuctions() {
        // Arrange
        List<Auction> expectedAuctions = Arrays.asList(auction1, auction2);
        when(auctionDao.findAll()).thenReturn(expectedAuctions);

        // Act
        List<Auction> result = auctionService.findAllAuctions();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedAuctions, result);
        verify(auctionDao).findAll();
    }

    @Test
    void findAllAuctions_WhenNoAuctions_ShouldReturnEmptyList() {
        // Arrange
        when(auctionDao.findAll()).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAllAuctions();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(auctionDao).findAll();
    }

    @Test
    void getAllAuctionBids_WhenAuctionExists_ShouldReturnBids() {
        // Arrange
        when(auctionDao.find(1L)).thenReturn(auction1);

        // Act
        Set<Bid> result = auctionService.getAllAuctionBids(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(bids, result);
        verify(auctionDao).find(1L);
    }

    @Test
    void getAllAuctionBids_WhenAuctionDoesNotExist_ShouldReturnEmptyList() {
        // Arrange
        when(auctionDao.find(999L)).thenReturn(null);

        // Act
        Set<Bid> result = auctionService.getAllAuctionBids(999L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(auctionDao).find(999L);
    }

    @Test
    void deleteAuction_WhenAuctionExists_ShouldDeleteAndReturnTrue() {
        // Arrange
        when(auctionDao.find(1L)).thenReturn(auction1);

        // Act
        boolean result = auctionService.deleteAuction(1L);

        // Assert
        assertTrue(result);
        verify(auctionDao).find(1L);
        verify(auctionDao).delete(auction1);
    }

    @Test
    void deleteAuction_WhenAuctionDoesNotExist_ShouldReturnTrue() {
        // Arrange
        when(auctionDao.find(999L)).thenReturn(null);

        // Act
        boolean result = auctionService.deleteAuction(999L);

        // Assert
        assertTrue(result);
        verify(auctionDao).find(999L);
        verify(auctionDao, never()).delete(any());
    }
}