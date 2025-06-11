
package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Bid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Bid> typedQuery;

    @InjectMocks
    private BidDaoImpl bidDao;

    private Bid bid1;
    private Bid bid2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        bid1 = new Bid();
        bid1.setBidId(1L);
        
        bid2 = new Bid();
        bid2.setBidId(2L);
    }

    @Test
    void findAll_ShouldReturnListOfBids() {
        // Arrange
        List<Bid> expectedBids = Arrays.asList(bid1, bid2);
        when(entityManager.createQuery("SELECT b FROM Bid b", Bid.class))
            .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedBids);

        // Act
        List<Bid> actualBids = bidDao.findAll();

        // Assert
        assertNotNull(actualBids);
        assertEquals(2, actualBids.size());
        assertEquals(expectedBids, actualBids);
        verify(entityManager).createQuery("SELECT b FROM Bid b", Bid.class);
        verify(typedQuery).getResultList();
    }

    @Test
    void find_ShouldReturnBidWhenExists() {
        // Arrange
        when(entityManager.find(Bid.class, 1L)).thenReturn(bid1);

        // Act
        Bid foundBid = bidDao.find(1L);

        // Assert
        assertNotNull(foundBid);
        assertEquals(1L, foundBid.getBidId());
        verify(entityManager).find(Bid.class, 1L);
    }

    @Test
    void find_ShouldReturnNullWhenBidDoesNotExist() {
        // Arrange
        when(entityManager.find(Bid.class, 999L)).thenReturn(null);

        // Act
        Bid foundBid = bidDao.find(999L);

        // Assert
        assertNull(foundBid);
        verify(entityManager).find(Bid.class, 999L);
    }

    @Test
    void findAll_ShouldReturnEmptyListWhenNoBidsExist() {
        // Arrange
        when(entityManager.createQuery("SELECT b FROM Bid b", Bid.class))
            .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        // Act
        List<Bid> actualBids = bidDao.findAll();

        // Assert
        assertNotNull(actualBids);
        assertTrue(actualBids.isEmpty());
        verify(entityManager).createQuery("SELECT b FROM Bid b", Bid.class);
        verify(typedQuery).getResultList();
    }
}