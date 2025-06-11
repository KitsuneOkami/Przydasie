package org.example.web;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.example.model.Auction;
import org.example.model.Auction.AuctionStatus;
import org.example.model.Bid;
import org.example.model.User;
import org.example.service.AuctionService;
import org.example.service.BidService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class AuctionDetailsBeanTest {

    @Mock
    private AuctionService auctionService;

    @Mock
    private BidService bidService;

    @Mock
    private UserSessionBean userSession;

    @Mock
    private FacesContext facesContext;

    @Mock
    private ExternalContext externalContext;

    @InjectMocks
    private AuctionDetailsBean auctionDetailsBean;

    private User testUser;
    private Auction testAuction;

    private static MockedStatic<FacesContext> mockedFacesContext;

    @BeforeAll
    static void setUpClass() {
        mockedFacesContext = mockStatic(FacesContext.class);
    }

    @AfterAll
    static void tearDownClass() {
        mockedFacesContext.close();
    }

    @BeforeEach
    void setUp() {
        mockedFacesContext.reset();

        // Mark lenient so unused stubbings don't cause errors
        lenient().when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
        lenient().when(facesContext.getExternalContext()).thenReturn(externalContext);

        // Initialize test user and auction as before
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setName("Test User");

        testAuction = new Auction();
        testAuction.setAuctionId(1L);
        testAuction.setStatus(AuctionStatus.ACTIVE);
        testAuction.setOwner(testUser);
        testAuction.setBids(new ArrayList<>());
    }

    @Test
    void init_WhenIdIsNull_ShouldNotLoadAuction() {
        // Arrange
        auctionDetailsBean.setId(null);

        // Act
        auctionDetailsBean.init();

        // Assert
        assertNull(auctionDetailsBean.getAuction(), "Auction should be null when id is null");
        verify(auctionService, never()).getAuction(any());
    }

    @Test
    void init_WhenIdIsSet_ShouldLoadAuction() {
        // Arrange
        Long auctionId = 1L;
        when(auctionService.getAuction(auctionId)).thenReturn(testAuction);
        auctionDetailsBean.setId(auctionId);

        // Act
        auctionDetailsBean.init();

        // Assert
        Auction loadedAuction = auctionDetailsBean.getAuction();
        assertNotNull(loadedAuction, "Auction should not be null after init");
        assertEquals(testAuction, loadedAuction, "Loaded auction should match the test auction");
        verify(auctionService, times(1)).getAuction(auctionId);
    }

    @Test
    void placeBid_WhenAuctionNull_ShouldShowError() {
        auctionDetailsBean.placeBid();

        verify(facesContext).addMessage(eq(null), argThat(msg ->
                msg.getSeverity() == FacesMessage.SEVERITY_ERROR));
        verify(bidService, never()).saveBid(any());
    }

    @Test
    void placeBid_WhenAuctionNotActive_ShouldShowError() {
        testAuction.setStatus(AuctionStatus.ENDED);
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        auctionDetailsBean.init();

        auctionDetailsBean.placeBid();

        verify(facesContext).addMessage(eq(null), argThat(msg ->
                msg.getSeverity() == FacesMessage.SEVERITY_ERROR));
        verify(bidService, never()).saveBid(any());
    }

    @Test
    void placeBid_WhenBidAmountInvalid_ShouldShowError() {
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        auctionDetailsBean.init();
        auctionDetailsBean.setBidAmount(BigDecimal.ZERO);

        auctionDetailsBean.placeBid();

        verify(facesContext).addMessage(eq(null), argThat(msg ->
                msg.getSeverity() == FacesMessage.SEVERITY_ERROR));
        verify(bidService, never()).saveBid(any());
    }

    @Test
    void placeBid_WhenBidAmountIsNull_ShouldShowError() {
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        auctionDetailsBean.init();
        auctionDetailsBean.setBidAmount(null);

        auctionDetailsBean.placeBid();

        verify(facesContext).addMessage(eq(null), argThat(msg ->
                msg.getSeverity() == FacesMessage.SEVERITY_ERROR));
        verify(bidService, never()).saveBid(any());
    }

    @Test
    void placeBid_WhenUserNotFound_ShouldShowError() {
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        when(userSession.getUser()).thenReturn(null);
        auctionDetailsBean.init();
        auctionDetailsBean.setBidAmount(BigDecimal.TEN);

        auctionDetailsBean.placeBid();

        verify(facesContext).addMessage(eq(null), argThat(msg ->
                msg.getSeverity() == FacesMessage.SEVERITY_ERROR));
        verify(bidService, never()).saveBid(any());
    }

    @Test
    void placeBid_WhenValidBid_ShouldSucceed() {
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        when(userSession.getUser()).thenReturn(testUser);
        auctionDetailsBean.init();
        auctionDetailsBean.setBidAmount(BigDecimal.TEN);

        auctionDetailsBean.placeBid();

        verify(bidService).saveBid(any(Bid.class));
        verify(auctionService).saveAuction(testAuction);
        verify(facesContext).addMessage(eq(null), argThat(message ->
                message.getSeverity() == FacesMessage.SEVERITY_INFO &&
                        message.getSummary().equals("Bid placed successfully.")));
    }

    @Test
    void canPlaceBid_WhenValidConditions_ShouldReturnTrue() {
        User bidder = new User();
        bidder.setUserId(2L);

        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        when(userSession.isLoggedIn()).thenReturn(true);
        when(userSession.getUser()).thenReturn(bidder);
        auctionDetailsBean.init();

        assertTrue(auctionDetailsBean.canPlaceBid());
    }

    @Test
    void canPlaceBid_WhenAuctionIsNull_ShouldReturnFalse() throws Exception {
        // Arrange
        Field auctionField = AuctionDetailsBean.class.getDeclaredField("auction");
        auctionField.setAccessible(true);
        auctionField.set(auctionDetailsBean, null);

        // Act
        boolean result = auctionDetailsBean.canPlaceBid();

        // Assert
        assertFalse(result, "canPlaceBid should return false when auction is null");
    }

    @Test
    void canPlaceBid_WhenUserNotLoggedIn_ShouldReturnFalse() throws Exception {
        // Arrange
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        auctionDetailsBean.init();

        when(userSession.isLoggedIn()).thenReturn(false);

        // Act
        boolean result = auctionDetailsBean.canPlaceBid();

        // Assert
        assertFalse(result, "canPlaceBid should return false when user is not logged in");
    }

    @Test
    void canPlaceBid_WhenUserIsAuctionOwner_ShouldReturnFalse() throws Exception {
        // Arrange
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        auctionDetailsBean.init();

        when(userSession.isLoggedIn()).thenReturn(true);
        when(userSession.getUser()).thenReturn(testUser);

        // Act
        boolean result = auctionDetailsBean.canPlaceBid();

        // Assert
        assertFalse(result, "canPlaceBid should return false when user is the auction owner");
    }

    @Test
    void canPlaceBid_WhenAuctionNotActive_ShouldReturnFalse() throws Exception {
        // Arrange
        testAuction.setStatus(AuctionStatus.ENDED);
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        auctionDetailsBean.init();

        when(userSession.isLoggedIn()).thenReturn(true);
        User bidder = new User();
        bidder.setUserId(2L);
        when(userSession.getUser()).thenReturn(bidder);

        // Act
        boolean result = auctionDetailsBean.canPlaceBid();

        // Assert
        assertFalse(result, "canPlaceBid should return false when auction is not active");
    }

    @Test
    void deleteAuction_WhenAuctionNull_ShouldShowError() {
        auctionDetailsBean.deleteAuction();

        verify(facesContext).addMessage(eq(null), argThat(msg ->
                msg.getSeverity() == FacesMessage.SEVERITY_ERROR));
        verify(auctionService, never()).deleteAuction(any());
    }

    @Test
    void deleteAuction_WhenUserNotLoggedIn_ShouldShowError() {
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        when(userSession.isLoggedIn()).thenReturn(false);
        auctionDetailsBean.init();

        auctionDetailsBean.deleteAuction();

        verify(facesContext).addMessage(eq(null), argThat(msg ->
                msg.getSeverity() == FacesMessage.SEVERITY_ERROR));
        verify(auctionService, never()).deleteAuction(any());
    }

    @Test
    void deleteAuction_WhenNotOwner_ShouldShowError() {
        User differentUser = new User();
        differentUser.setUserId(2L);

        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        when(userSession.isLoggedIn()).thenReturn(true);
        when(userSession.getUser()).thenReturn(differentUser);
        auctionDetailsBean.init();

        auctionDetailsBean.deleteAuction();

        verify(facesContext).addMessage(eq(null), argThat(msg ->
                msg.getSeverity() == FacesMessage.SEVERITY_ERROR));
        verify(auctionService, never()).deleteAuction(any());
    }

    @Test
    void deleteAuction_WhenSuccessful_ShouldRedirect() throws IOException {
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        when(userSession.isLoggedIn()).thenReturn(true);
        when(userSession.getUser()).thenReturn(testUser);
        when(auctionService.deleteAuction(1L)).thenReturn(true);
        auctionDetailsBean.init();

        auctionDetailsBean.deleteAuction();

        verify(auctionService).deleteAuction(1L);
        verify(externalContext).redirect("auctions.xhtml");
    }

    @Test
    void deleteAuction_WhenRedirectThrowsException_ShouldShowErrorMessage() throws Exception {
        // Arrange
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        when(userSession.isLoggedIn()).thenReturn(true);
        when(userSession.getUser()).thenReturn(testUser);
        auctionDetailsBean.init();

        // Correctly mock deleteAuction to return true
        when(auctionService.deleteAuction(testAuction.getAuctionId())).thenReturn(true);

        // Mock externalContext.redirect to throw IOException
        doThrow(new IOException("Redirect failed")).when(externalContext).redirect("auctions.xhtml");

        // Act
        auctionDetailsBean.deleteAuction();

        // Assert
        verify(auctionService).deleteAuction(testAuction.getAuctionId());
        verify(externalContext).redirect("auctions.xhtml");
        verify(facesContext).addMessage(eq(null), argThat(message ->
                message.getSeverity() == FacesMessage.SEVERITY_ERROR &&
                        message.getSummary().equals("Error redirecting to auctions list.")));
    }

    @Test
    void isAuctionOwner_WhenOwner_ShouldReturnTrue() {
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        when(userSession.getUser()).thenReturn(testUser);
        auctionDetailsBean.init();

        assertTrue(auctionDetailsBean.isAuctionOwner());
    }

    @Test
    void isAuctionOwner_WhenUserIsOwner_ShouldReturnTrue() {
        // Arrange
        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        when(userSession.getUser()).thenReturn(testUser);
        auctionDetailsBean.init();

        // Act
        boolean result = auctionDetailsBean.isAuctionOwner();

        // Assert
        assertTrue(result, "User should be recognized as the auction owner");
    }

    @Test
    void isAuctionOwner_WhenUserIsNotOwner_ShouldReturnFalse() {
        // Arrange
        User differentUser = new User();
        differentUser.setUserId(2L);

        auctionDetailsBean.setId(1L);
        when(auctionService.getAuction(1L)).thenReturn(testAuction);
        when(userSession.getUser()).thenReturn(differentUser);
        auctionDetailsBean.init();

        // Act
        boolean result = auctionDetailsBean.isAuctionOwner();

        // Assert
        assertFalse(result, "User should not be recognized as the auction owner");
    }

    @Test
    void isAuctionOwner_WhenAuctionIsNull_ShouldReturnFalse() throws Exception {
        // Arrange
        Field auctionField = AuctionDetailsBean.class.getDeclaredField("auction");
        auctionField.setAccessible(true);
        auctionField.set(auctionDetailsBean, null);

        // Act
        boolean result = auctionDetailsBean.isAuctionOwner();

        // Assert
        assertFalse(result, "isAuctionOwner should return false when auction is null");
    }

}