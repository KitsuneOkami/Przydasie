
package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Auction;
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
class AuctionDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Auction> typedQuery;

    @InjectMocks
    private AuctionDaoImpl auctionDao;

    private Auction auction1;
    private Auction auction2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        auction1 = new Auction();
        auction1.setAuctionId(1L);
        
        auction2 = new Auction();
        auction2.setAuctionId(2L);
    }

    @Test
    void findAll_ShouldReturnListOfAuctions() {
        // Arrange
        List<Auction> expectedAuctions = Arrays.asList(auction1, auction2);
        when(entityManager.createQuery("SELECT a FROM Auction a", Auction.class))
            .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedAuctions);

        // Act
        List<Auction> actualAuctions = auctionDao.findAll();

        // Assert
        assertNotNull(actualAuctions);
        assertEquals(2, actualAuctions.size());
        assertEquals(expectedAuctions, actualAuctions);
        verify(entityManager).createQuery("SELECT a FROM Auction a", Auction.class);
        verify(typedQuery).getResultList();
    }

    @Test
    void find_ShouldReturnAuctionWhenExists() {
        // Arrange
        when(entityManager.find(Auction.class, 1L)).thenReturn(auction1);

        // Act
        Auction foundAuction = auctionDao.find(1L);

        // Assert
        assertNotNull(foundAuction);
        assertEquals(1L, foundAuction.getAuctionId());
        verify(entityManager).find(Auction.class, 1L);
    }

    @Test
    void find_ShouldReturnNullWhenAuctionDoesNotExist() {
        // Arrange
        when(entityManager.find(Auction.class, 999L)).thenReturn(null);

        // Act
        Auction foundAuction = auctionDao.find(999L);

        // Assert
        assertNull(foundAuction);
        verify(entityManager).find(Auction.class, 999L);
    }

    @Test
    void findAll_ShouldReturnEmptyListWhenNoAuctionsExist() {
        // Arrange
        when(entityManager.createQuery("SELECT a FROM Auction a", Auction.class))
            .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        // Act
        List<Auction> actualAuctions = auctionDao.findAll();

        // Assert
        assertNotNull(actualAuctions);
        assertTrue(actualAuctions.isEmpty());
        verify(entityManager).createQuery("SELECT a FROM Auction a", Auction.class);
        verify(typedQuery).getResultList();
    }
}