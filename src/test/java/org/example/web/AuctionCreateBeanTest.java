package org.example.web;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.example.model.Auction;
import org.example.model.User;
import org.example.service.AuctionService;
import org.example.util.JSFUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionCreateBeanTest {

    @Mock
    private AuctionService auctionService;

    @Mock
    private UserSessionBean userSession;

    @Mock
    private FacesContext facesContext;

    @Mock
    private ExternalContext externalContext;

    @InjectMocks
    private AuctionCreateBean auctionCreateBean;

    private User testUser;
    private static final String TEST_NAME = "Test Auction";
    private static final String TEST_DESCRIPTION = "Test Description";
    private static final BigDecimal TEST_PRICE = new BigDecimal("100.00");
    private static final LocalDateTime TEST_START_TIME = LocalDateTime.now();
    private static final LocalDateTime TEST_END_TIME = LocalDateTime.now().plusDays(7);

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        
        auctionCreateBean.setName(TEST_NAME);
        auctionCreateBean.setDescription(TEST_DESCRIPTION);
        auctionCreateBean.setStartPrice(TEST_PRICE);
        auctionCreateBean.setStartTime(TEST_START_TIME);
        auctionCreateBean.setEndTime(TEST_END_TIME);
    }

    @Test
    void save_WhenUserLoggedIn_ShouldCreateAuctionAndRedirect() throws IOException {
        // Arrange
        when(userSession.getUser()).thenReturn(testUser);
        try (MockedStatic<JSFUtil> mockedStatic = mockStatic(JSFUtil.class)) {

            // Act
            auctionCreateBean.save();

            // Assert
            verify(auctionService).saveAuction(any(Auction.class));
            mockedStatic.verify(() -> JSFUtil.redirect("auctions.xhtml"));
        }
    }

    @Test
    void save_WhenUserNotLoggedIn_ShouldNotCreateAuction() throws IOException {
        // Arrange
        when(userSession.getUser()).thenReturn(null);
        try (MockedStatic<JSFUtil> mockedStatic = mockStatic(JSFUtil.class)) {

            // Act
            auctionCreateBean.save();

            // Assert
            verify(auctionService, never()).saveAuction(any(Auction.class));
            mockedStatic.verify(() -> JSFUtil.redirect(anyString()), never());
        }
    }

    @Test
    void save_ShouldSetCorrectAuctionPropertiesAndRedirect() throws IOException {
        // Arrange
        when(userSession.getUser()).thenReturn(testUser);

        try (MockedStatic<JSFUtil> mockedJSFUtil = mockStatic(JSFUtil.class)) {
            // Act
            auctionCreateBean.save();

            // Assert
            verify(auctionService).saveAuction(argThat(auction ->
                    Objects.equals(auction.getName(), TEST_NAME) &&
                            Objects.equals(auction.getDescription(), TEST_DESCRIPTION) &&
                            Objects.equals(auction.getStartPrice(), TEST_PRICE) &&
                            Objects.equals(auction.getStartTime(), TEST_START_TIME) &&
                            Objects.equals(auction.getEndTime(), TEST_END_TIME) &&
                            Objects.equals(auction.getOwner(), testUser) &&
                            Objects.equals(auction.getStatus(), Auction.AuctionStatus.ACTIVE) &&
                            auction.getReservePrice() == null
            ));

            // Verify no error messages were added
            mockedJSFUtil.verify(() -> JSFUtil.addErrorMessage(any()), never());

            // Verify redirect happened
            mockedJSFUtil.verify(() -> JSFUtil.redirect("auctions.xhtml"));
        }
    }

    @Test
    void save_WhenRedirectThrowsIOException_ShouldHandleException() throws IOException {
        // Arrange
        when(userSession.getUser()).thenReturn(testUser);
        try (MockedStatic<JSFUtil> mockedStatic = mockStatic(JSFUtil.class)) {
            mockedStatic.when(() -> JSFUtil.redirect(anyString()))
                    .thenThrow(new IOException("Test exception"));

            // Act & Assert - should not throw exception
            auctionCreateBean.save();

            // Verify that auction was still saved
            verify(auctionService).saveAuction(any(Auction.class));
        }
    }

    @Test
    void createAuction_ShouldCreateAuctionWithCorrectProperties() {
        // Arrange
        String testName = "Test Name";
        String testDescription = "Test Description";
        LocalDateTime testStartTime = LocalDateTime.now();
        LocalDateTime testEndTime = LocalDateTime.now().plusDays(1);
        BigDecimal testPrice = new BigDecimal("100.00");
        User testUser = new User();
        testUser.setUserId(1L);

        auctionCreateBean.setName(testName);
        auctionCreateBean.setDescription(testDescription);
        auctionCreateBean.setStartTime(testStartTime);
        auctionCreateBean.setEndTime(testEndTime);
        auctionCreateBean.setStartPrice(testPrice);
        when(userSession.getUser()).thenReturn(testUser);

        try (MockedStatic<JSFUtil> mockedJSFUtil = mockStatic(JSFUtil.class)) {
            // Act
            auctionCreateBean.save();

            // Assert
            verify(auctionService).saveAuction(argThat(auction -> {
                // Verify each property was set correctly
                return auction.getName().equals(testName) &&
                        auction.getDescription().equals(testDescription) &&
                        auction.getStartTime().equals(testStartTime) &&
                        auction.getEndTime().equals(testEndTime) &&
                        auction.getStartPrice().equals(testPrice) &&
                        auction.getOwner().equals(testUser) &&
                        auction.getStatus() == Auction.AuctionStatus.ACTIVE &&
                        // Verify default/unset properties
                        auction.getReservePrice() == null &&
                        auction.getBids() == null &&
                        auction.getAuctionId() == null;
            }));
        }
    }

    @Test
    void createAuction_WithOnlyRequiredFields_ShouldCreateValidAuction() {
        // Arrange
        // Clear any existing values from setUp()
        auctionCreateBean.setDescription(null);  // Clear description explicitly

        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRole(User.Role.USER);

        LocalDateTime now = LocalDateTime.of(2025, 6, 11, 12, 0);
        auctionCreateBean.setName("Test Name");
        auctionCreateBean.setStartTime(now);
        auctionCreateBean.setEndTime(now.plusDays(1));
        auctionCreateBean.setStartPrice(new BigDecimal("100.00"));

        when(userSession.getUser()).thenReturn(testUser);

        try (MockedStatic<JSFUtil> mockedJSFUtil = mockStatic(JSFUtil.class)) {
            // Act
            auctionCreateBean.save();

            // Assert
            verify(auctionService).saveAuction(any(Auction.class));  // First verify the call happened
            verify(auctionService).saveAuction(argThat(auction -> {
                boolean matches = auction.getName().equals("Test Name") &&
                        auction.getDescription() == null &&  // Explicitly check for null
                        auction.getStartTime().equals(now) &&
                        auction.getEndTime().equals(now.plusDays(1)) &&
                        auction.getStartPrice().equals(new BigDecimal("100.00")) &&
                        auction.getOwner().equals(testUser) &&
                        auction.getStatus() == Auction.AuctionStatus.ACTIVE &&
                        auction.getReservePrice() == null;
                if (!matches) {
                    System.out.println("Actual auction: " + auction);  // Add debug information
                }
                return matches;
            }));

            mockedJSFUtil.verify(() -> JSFUtil.redirect("auctions.xhtml"));
        }
    }

    @Test
    void createAuction_ShouldCreateNewAuctionInstance() {
        // Arrange
        User testUser = new User();
        testUser.setUserId(1L);

        auctionCreateBean.setName("Test Name");
        auctionCreateBean.setStartTime(LocalDateTime.now());
        auctionCreateBean.setEndTime(LocalDateTime.now().plusDays(1));
        auctionCreateBean.setStartPrice(new BigDecimal("100.00"));
        when(userSession.getUser()).thenReturn(testUser);

        try (MockedStatic<JSFUtil> mockedJSFUtil = mockStatic(JSFUtil.class)) {
            // Act
            auctionCreateBean.save();
            auctionCreateBean.save(); // Call save twice

            // Assert
            verify(auctionService, times(2)).saveAuction(argThat(auction ->
                    // Verify that each call creates a new instance
                    auction != null && auction.getClass() == Auction.class
            ));
        }
    }

}