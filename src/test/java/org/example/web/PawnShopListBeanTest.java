package org.example.web;

import org.example.model.Auction;
import org.example.model.PawnShopItem;
import org.example.model.User;
import org.example.service.AuctionService;
import org.example.service.PawnShopItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PawnShopListBeanTest {

    private PawnShopListBean pawnShopListBean;
    private PawnShopItemService pawnShopItemService;
    private AuctionService auctionService;
    private UserSessionBean userSessionBean;

    @BeforeEach
    void setUp() throws Exception {
        pawnShopItemService = mock(PawnShopItemService.class);
        auctionService = mock(AuctionService.class);
        userSessionBean = mock(UserSessionBean.class);

        pawnShopListBean = new PawnShopListBean();

        // Inject mocks via reflection since no setters
        setField(pawnShopListBean, "pawnShopItemService", pawnShopItemService);
        setField(pawnShopListBean, "auctionService", auctionService);
        setField(pawnShopListBean, "userSessionBean", userSessionBean);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testGetPawnShopItems_userNull_returnsEmptyList() {
        when(userSessionBean.getUser()).thenReturn(null);

        List<PawnShopItem> items = pawnShopListBean.getPawnShopItems();

        assertNotNull(items);
        assertTrue(items.isEmpty());
        verify(pawnShopItemService, never()).getItemsByOwnerId(anyLong());
    }

    @Test
    void testGetPawnShopItems_userPresent_returnsItems() {
        User user = new User();
        user.setUserId(123L);
        when(userSessionBean.getUser()).thenReturn(user);

        List<PawnShopItem> expectedItems = List.of(new PawnShopItem(), new PawnShopItem());
        when(pawnShopItemService.getItemsByOwnerId(123L)).thenReturn(expectedItems);

        List<PawnShopItem> items = pawnShopListBean.getPawnShopItems();

        assertEquals(expectedItems, items);
        verify(pawnShopItemService).getItemsByOwnerId(123L);
    }

    @Test
    void testConvertToAuction_callsServicesWithCorrectParams() {
        PawnShopItem item = new PawnShopItem();
        item.setItemId(456L);

        // Set some duration values
        pawnShopListBean.setDays(1);
        pawnShopListBean.setHours(2);
        pawnShopListBean.setMinutes(3);
        pawnShopListBean.setSeconds(4);

        Auction auction = new Auction();
        when(pawnShopItemService.toAuction(eq(item), any(LocalDateTime.class))).thenReturn(auction);

        pawnShopListBean.convertToAuction(item);

        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(pawnShopItemService).toAuction(eq(item), captor.capture());
        LocalDateTime endTime = captor.getValue();

        LocalDateTime expectedMin = LocalDateTime.now()
                .plusDays(1)
                .plusHours(2)
                .plusMinutes(3)
                .plusSeconds(4)
                .minusSeconds(1); // allow 1 second tolerance
        LocalDateTime expectedMax = expectedMin.plusSeconds(2);

        // Check that endTime is within expected range (allowing small delay)
        assertTrue(endTime.isAfter(expectedMin) && endTime.isBefore(expectedMax));

        verify(auctionService).saveAuction(auction);
        verify(pawnShopItemService).deleteItem(456L);
    }

    @Test
    void testRemoveItem_callsDeleteItem() {
        PawnShopItem item = new PawnShopItem();
        item.setItemId(789L);

        pawnShopListBean.removeItem(item);

        verify(pawnShopItemService).deleteItem(789L);
    }
}
