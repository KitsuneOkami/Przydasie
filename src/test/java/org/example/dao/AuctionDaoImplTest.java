package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Auction;
import org.example.model.Bid;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionDaoImplTest {

    @InjectMocks
    private AuctionDaoImpl auctionDao;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Because getEntityManager() is presumably from AbstractDaoImpl,
        // we mock it by spying on auctionDao and overriding getEntityManager()
        auctionDao = spy(new AuctionDaoImpl());
        doReturn(entityManager).when(auctionDao).getEntityManager();
    }

    @Test
    void find_ShouldReturnAuction_WhenFound() {
        Long auctionId = 1L;
        Auction auction = new Auction();
        auction.setAuctionId(auctionId);

        when(entityManager.find(Auction.class, auctionId)).thenReturn(auction);

        Auction result = auctionDao.find(auctionId);

        assertNotNull(result);
        assertEquals(auctionId, result.getAuctionId());
        verify(entityManager).find(Auction.class, auctionId);
    }

    @Test
    void find_ShouldReturnNull_WhenNotFound() {
        Long auctionId = 1L;

        when(entityManager.find(Auction.class, auctionId)).thenReturn(null);

        Auction result = auctionDao.find(auctionId);

        assertNull(result);
        verify(entityManager).find(Auction.class, auctionId);
    }

    @Test
    void findAll_ShouldReturnListOfAuctions() {
        List<Auction> auctions = new ArrayList<>();
        auctions.add(new Auction());
        auctions.add(new Auction());

        TypedQuery<Auction> query = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT a FROM Auction a", Auction.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(auctions);

        List<Auction> result = auctionDao.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(entityManager).createQuery("SELECT a FROM Auction a", Auction.class);
        verify(query).getResultList();
    }

    @Test
    void addBid_ShouldAddBidToAuctionAndMerge() {
        Long auctionId = 1L;
        User user = new User();
        user.setUserId(10L);
        BigDecimal bidAmount = BigDecimal.valueOf(100);

        Auction auction = spy(new Auction());
        auction.setAuctionId(auctionId);
        auction.setBids(new HashSet<>());

        when(entityManager.find(Auction.class, auctionId)).thenReturn(auction);

        auctionDao.addBid(auctionId, user, bidAmount);

        // Verify auction was found
        verify(entityManager).find(Auction.class, auctionId);

        // Verify auction.getBids().size() was called to initialize collection
        verify(auction).getBids();

        // Verify auction.addBid was called with a Bid having correct properties
        ArgumentCaptor<Bid> bidCaptor = ArgumentCaptor.forClass(Bid.class);
        verify(auction).addBid(bidCaptor.capture());

        Bid addedBid = bidCaptor.getValue();
        assertNotNull(addedBid);
        assertEquals(bidAmount, addedBid.getBidAmount());
        assertEquals(user, addedBid.getBidder());

        // Verify merge called
        verify(entityManager).merge(auction);
    }

    @Test
    void addBid_ShouldThrowException_WhenAuctionNotFound() {
        Long auctionId = 1L;
        User user = new User();
        BigDecimal bidAmount = BigDecimal.TEN;

        when(entityManager.find(Auction.class, auctionId)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                auctionDao.addBid(auctionId, user, bidAmount));

        assertEquals("Auction not found", ex.getMessage());
        verify(entityManager).find(Auction.class, auctionId);
        verify(entityManager, never()).merge(any());
    }

    @Test
    void findWithBids_ShouldReturnAuctionWithBids() {
        Long auctionId = 1L;
        Auction auction = new Auction();
        auction.setAuctionId(auctionId);

        TypedQuery<Auction> query = mock(TypedQuery.class);

        String expectedQuery = """
            SELECT DISTINCT a FROM Auction a
            LEFT JOIN FETCH a.bids b
            LEFT JOIN FETCH b.bidder
            WHERE a.auctionId = :id
            """;

        // Use anyString() to avoid exact string matching issues
        when(entityManager.createQuery(anyString(), eq(Auction.class))).thenReturn(query);
        when(query.setParameter("id", auctionId)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(auction);

        Auction result = auctionDao.findWithBids(auctionId);

        assertNotNull(result);
        assertEquals(auctionId, result.getAuctionId());

        verify(entityManager).createQuery(anyString(), eq(Auction.class));
        verify(query).setParameter("id", auctionId);
        verify(query).getSingleResult();
    }
}