package org.example.service;

import org.example.dao.PawnShopItemDao;
import org.example.model.Auction;
import org.example.model.PawnShopItem;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PawnShopItemServiceTest {

    private PawnShopItemService pawnShopItemService;
    private PawnShopItemDao pawnShopItemDao;

    @BeforeEach
    void setUp() throws Exception {
        pawnShopItemDao = mock(PawnShopItemDao.class);
        pawnShopItemService = new PawnShopItemService();

        // Inject mock via reflection since no setter
        var field = PawnShopItemService.class.getDeclaredField("auctionDao");
        field.setAccessible(true);
        field.set(pawnShopItemService, pawnShopItemDao);
    }

    @Test
    void getItemsByOwnerId_shouldReturnListFromDao() {
        Long ownerId = 123L;
        List<PawnShopItem> expectedItems = List.of(new PawnShopItem(), new PawnShopItem());
        when(pawnShopItemDao.findByOwnerId(ownerId)).thenReturn(expectedItems);

        List<PawnShopItem> items = pawnShopItemService.getItemsByOwnerId(ownerId);

        assertEquals(expectedItems, items);
        verify(pawnShopItemDao).findByOwnerId(ownerId);
    }

    @Test
    void saveItem_shouldCallDaoSave() {
        PawnShopItem item = new PawnShopItem();

        pawnShopItemService.saveItem(item);

        verify(pawnShopItemDao).save(item);
    }

    @Test
    void deleteItem_whenItemExists_shouldCallDaoDelete() {
        Long itemId = 456L;
        PawnShopItem item = new PawnShopItem();
        when(pawnShopItemDao.find(itemId)).thenReturn(item);

        pawnShopItemService.deleteItem(itemId);

        verify(pawnShopItemDao).find(itemId);
        verify(pawnShopItemDao).delete(item);
    }

    @Test
    void deleteItem_whenItemDoesNotExist_shouldNotCallDaoDelete() {
        Long itemId = 789L;
        when(pawnShopItemDao.find(itemId)).thenReturn(null);

        pawnShopItemService.deleteItem(itemId);

        verify(pawnShopItemDao).find(itemId);
        verify(pawnShopItemDao, never()).delete(any());
    }

    @Test
    void toAuction_shouldCreateAuctionWithCorrectFields() {
        PawnShopItem item = new PawnShopItem();
        item.setName("ItemName");
        item.setDescription("ItemDescription");
        User owner = new User();
        item.setOwner(owner);
        item.setPrice(java.math.BigDecimal.valueOf(100));

        LocalDateTime endTime = LocalDateTime.now().plusDays(1);

        Auction auction = pawnShopItemService.toAuction(item, endTime);

        assertNotNull(auction);
        assertEquals(item.getName(), auction.getName());
        assertEquals(item.getDescription(), auction.getDescription());
        assertEquals(owner, auction.getOwner());
        assertEquals(item.getPrice(), auction.getStartPrice());
        assertEquals(item.getPrice(), auction.getReservePrice());
        assertNotNull(auction.getStartTime());
        assertEquals(endTime, auction.getEndTime());
        assertEquals(Auction.AuctionStatus.ACTIVE, auction.getStatus());
    }
}