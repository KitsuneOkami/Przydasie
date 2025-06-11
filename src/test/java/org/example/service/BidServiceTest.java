package org.example.service;

import org.example.dao.BidDao;
import org.example.model.Auction;
import org.example.model.Bid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @Mock
    private BidDao bidDao;

    @InjectMocks
    private BidService bidService;

    private Bid bid1;
    private Bid bid2;

    @BeforeEach
    void setUp() {
        // Initialize test data


        bid1 = new Bid();
        bid1.setBidId(1L);
        bid1.setBidAmount(new BigDecimal("100.00"));


        bid2 = new Bid();
        bid2.setBidId(2L);
        bid2.setBidAmount(new BigDecimal("150.00"));
    }

    @Test
    void saveBid_ShouldCallDaoSave() {
        // Act
        bidService.saveBid(bid1);

        // Assert
        verify(bidDao).save(bid1);
    }

    @Test
    void getBid_WhenBidExists_ShouldReturnBid() {
        // Arrange
        when(bidDao.find(1L)).thenReturn(bid1);

        // Act
        Bid result = bidService.getBid(1L);

        // Assert
        assertNotNull(result);
        assertEquals(bid1, result);
        assertEquals(bid1.getBidAmount(), result.getBidAmount());
        verify(bidDao).find(1L);
    }

    @Test
    void getBid_WhenBidDoesNotExist_ShouldReturnNull() {
        // Arrange
        when(bidDao.find(999L)).thenReturn(null);

        // Act
        Bid result = bidService.getBid(999L);

        // Assert
        assertNull(result);
        verify(bidDao).find(999L);
    }

    @Test
    void findAllBids_ShouldReturnAllBids() {
        // Arrange
        List<Bid> expectedBids = Arrays.asList(bid1, bid2);
        when(bidDao.findAll()).thenReturn(expectedBids);

        // Act
        List<Bid> result = bidService.findAllBids();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedBids, result);
        verify(bidDao).findAll();
    }

    @Test
    void findAllBids_WhenNoBids_ShouldReturnEmptyList() {
        // Arrange
        when(bidDao.findAll()).thenReturn(List.of());

        // Act
        List<Bid> result = bidService.findAllBids();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bidDao).findAll();
    }

    @Test
    void deleteBid_WhenBidExists_ShouldDeleteAndReturnTrue() {
        // Arrange
        when(bidDao.find(1L)).thenReturn(bid1);

        // Act
        boolean result = bidService.deleteBid(1L);

        // Assert
        assertTrue(result);
        verify(bidDao).find(1L);
        verify(bidDao).delete(bid1);
    }

    @Test
    void deleteBid_WhenBidDoesNotExist_ShouldReturnTrue() {
        // Arrange
        when(bidDao.find(999L)).thenReturn(null);

        // Act
        boolean result = bidService.deleteBid(999L);

        // Assert
        assertTrue(result);
        verify(bidDao).find(999L);
        verify(bidDao, never()).delete(any());
    }

    @Test
    void getBid_WhenIdIsNull_ShouldReturnNull() {
        // Act
        Bid result = bidService.getBid(null);

        // Assert
        assertNull(result);
        verify(bidDao).find(null);
    }

    @Test
    void deleteBid_WhenIdIsNull_ShouldReturnTrue() {
        // Arrange
        when(bidDao.find(null)).thenReturn(null);

        // Act
        boolean result = bidService.deleteBid(null);

        // Assert
        assertTrue(result);
        verify(bidDao).find(null);
        verify(bidDao, never()).delete(any());
    }
}