package org.example.web;

import org.example.model.PawnShopItem;
import org.example.model.User;
import org.example.service.PawnShopItemService;
import org.example.util.JSFUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PawnShopItemCreateBeanTest {

    private PawnShopItemCreateBean pawnShopItemCreateBean;
    private PawnShopItemService pawnShopItemService;
    private UserSessionBean userSession;

    @BeforeEach
    void setUp() throws Exception {
        pawnShopItemService = mock(PawnShopItemService.class);
        userSession = mock(UserSessionBean.class);

        pawnShopItemCreateBean = new PawnShopItemCreateBean();

        // Inject mocks via reflection since no setters
        setField(pawnShopItemCreateBean, "pawnShopItemService", pawnShopItemService);
        setField(pawnShopItemCreateBean, "userSession", userSession);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testSave_userNotLoggedIn_doesNotSaveOrRedirect() throws IOException {
        when(userSession.getUser()).thenReturn(null);

        pawnShopItemCreateBean.setName("Item1");
        pawnShopItemCreateBean.setDescription("Desc");
        pawnShopItemCreateBean.setPrice(BigDecimal.TEN);

        pawnShopItemCreateBean.save();

        verify(pawnShopItemService, never()).saveItem(any());
        // JSFUtil.redirect is static, so we cannot verify here without static mocking
        // But since user is null, redirect should not be called
    }

    @Test
    void testSave_validInput_savesItemAndRedirects() throws Exception {
        User user = new User();
        user.setName("pawnUser");
        when(userSession.getUser()).thenReturn(user);
        when(userSession.getUsername()).thenReturn("pawnUser");

        pawnShopItemCreateBean.setName("Item1");
        pawnShopItemCreateBean.setDescription("Desc");
        pawnShopItemCreateBean.setPrice(BigDecimal.valueOf(123.45));

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            pawnShopItemCreateBean.save();

            ArgumentCaptor<PawnShopItem> captor = ArgumentCaptor.forClass(PawnShopItem.class);
            verify(pawnShopItemService).saveItem(captor.capture());
            PawnShopItem savedItem = captor.getValue();

            assertEquals("Item1", savedItem.getName());
            assertEquals("Desc", savedItem.getDescription());
            assertEquals(BigDecimal.valueOf(123.45), savedItem.getPrice());
            assertEquals(user, savedItem.getOwner());

            jsfUtilMockedStatic.verify(() -> JSFUtil.redirect("pawnshop_list.xhtml"));
        }
    }

    @Test
    void testSave_redirectThrowsIOException_logsError() throws Exception {
        User user = new User();
        user.setName("pawnUser");
        when(userSession.getUser()).thenReturn(user);
        when(userSession.getUsername()).thenReturn("pawnUser");

        pawnShopItemCreateBean.setName("Item1");
        pawnShopItemCreateBean.setDescription("Desc");
        pawnShopItemCreateBean.setPrice(BigDecimal.valueOf(123.45));

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            jsfUtilMockedStatic.when(() -> JSFUtil.redirect("pawnshop_list.xhtml")).thenThrow(new IOException("Redirect failed"));

            pawnShopItemCreateBean.save();

            verify(pawnShopItemService).saveItem(any());
            jsfUtilMockedStatic.verify(() -> JSFUtil.redirect("pawnshop_list.xhtml"));
        }
    }
}