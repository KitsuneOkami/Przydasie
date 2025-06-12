package org.example.web;

import org.example.model.Ban;
import org.example.model.User;
import org.example.model.User.Role;
import org.example.service.BansService;
import org.example.service.UserService;
import org.example.util.JSFUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BanBeanTest {

    private BanBean banBean;
    private BansService bansService;
    private UserService userService;
    private UserSessionBean userSession;

    @BeforeEach
    void setUp() throws Exception {
        bansService = mock(BansService.class);
        userService = mock(UserService.class);
        userSession = mock(UserSessionBean.class);

        banBean = new BanBean();

        // Inject mocks via reflection
        setField(banBean, "bansService", bansService);
        setField(banBean, "userService", userService);
        setField(banBean, "userSession", userSession);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testGetBans_callsServiceAndReturnsList() {
        List<Ban> expectedBans = List.of(new Ban(), new Ban());
        when(bansService.getAllBansEagerUsers()).thenReturn(expectedBans);

        List<Ban> bans = banBean.getBans();

        assertEquals(expectedBans, bans);
        verify(bansService).getAllBansEagerUsers();
    }

    @Test
    void testAddPawnShop_validInput_callsServiceAndAddsInfoMessage() {
        banBean.setShopUsername("shopUser");
        banBean.setShopEmail("shop@example.com");
        banBean.setShopPassword("password");
        banBean.setBusinessName("Business");
        banBean.setTaxId("1234567890");
        banBean.setPayoutDetails("Details");

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.addPawnShop();

            verify(userService).addPawnShop("shopUser", "shop@example.com", "password", "Business", "1234567890", "Details");
            jsfUtilMockedStatic.verify(() -> JSFUtil.addInfoMessage("Pomyślnie dodano lombard."));
        }
    }

    @Test
    void testAddPawnShop_invalidUsername_doesNotCallService() {
        banBean.setShopUsername(""); // invalid username

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.addPawnShop();

            verify(userService, never()).addPawnShop(any(), any(), any(), any(), any(), any());
            jsfUtilMockedStatic.verify(() -> JSFUtil.addErrorMessage("Nazwa użytkownika nie może być pusta."));
        }
    }

    @Test
    void testAddPawnShop_invalidPawnShopFields_doesNotCallService() {
        banBean.setShopUsername("shopUser");
        banBean.setShopEmail(""); // invalid email

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.addPawnShop();

            verify(userService, never()).addPawnShop(any(), any(), any(), any(), any(), any());
            jsfUtilMockedStatic.verify(() -> JSFUtil.addErrorMessage("Email nie może być pusty."));
        }
    }

    @Test
    void testAddAdmin_validInput_callsServiceAndAddsInfoMessage() {
        banBean.setAdminUsername("adminUser");
        banBean.setAdminEmail("admin@example.com");
        banBean.setAdminPassword("password");
        banBean.setAdminFirstName("First");
        banBean.setAdminLastName("Last");

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.addAdmin();

            verify(userService).addAdmin("adminUser", "admin@example.com", "password", "First", "Last");
            jsfUtilMockedStatic.verify(() -> JSFUtil.addInfoMessage("Pomyślnie dodano admina."));
        }
    }

    @Test
    void testAddAdmin_invalidUsername_doesNotCallService() {
        banBean.setAdminUsername(""); // invalid username

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.addAdmin();

            verify(userService, never()).addAdmin(any(), any(), any(), any(), any());
            jsfUtilMockedStatic.verify(() -> JSFUtil.addErrorMessage("Nazwa użytkownika nie może być pusta."));
        }
    }

    @Test
    void testAddAdmin_invalidAdminFields_doesNotCallService() {
        banBean.setAdminUsername("adminUser");
        banBean.setAdminEmail(""); // invalid email

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.addAdmin();

            verify(userService, never()).addAdmin(any(), any(), any(), any(), any());
            jsfUtilMockedStatic.verify(() -> JSFUtil.addErrorMessage("Email nie może być pusty."));
        }
    }

    @Test
    void testBanUser_notLoggedIn_showsError() {
        when(userSession.isLoggedIn()).thenReturn(false);

        banBean.setBanUsername("user1");
        banBean.setReason("reason");
        banBean.setEndDate(LocalDateTime.now().plusDays(1));

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.banUser();

            jsfUtilMockedStatic.verify(() -> JSFUtil.addErrorMessage("Musisz być administratorem, aby banować użytkowników."));
            verify(bansService, never()).save(any());
        }
    }

    @Test
    void testBanUser_notAdmin_showsError() {
        when(userSession.isLoggedIn()).thenReturn(true);
        User user = new User();
        user.setRole(Role.USER);
        when(userSession.getUser()).thenReturn(user);

        banBean.setBanUsername("user1");
        banBean.setReason("reason");
        banBean.setEndDate(LocalDateTime.now().plusDays(1));

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.banUser();

            jsfUtilMockedStatic.verify(() -> JSFUtil.addErrorMessage("Musisz być administratorem, aby banować użytkowników."));
            verify(bansService, never()).save(any());
        }
    }

    @Test
    void testBanUser_emptyUsername_showsError() {
        when(userSession.isLoggedIn()).thenReturn(true);
        User admin = new User();
        admin.setRole(Role.ADMIN);
        when(userSession.getUser()).thenReturn(admin);

        banBean.setBanUsername("");
        banBean.setReason("reason");
        banBean.setEndDate(LocalDateTime.now().plusDays(1));

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.banUser();

            jsfUtilMockedStatic.verify(() -> JSFUtil.addErrorMessage("Nazwa użytkownika nie może być pusta."));
            verify(bansService, never()).save(any());
        }
    }

    @Test
    void testBanUser_userNotFound_showsError() {
        when(userSession.isLoggedIn()).thenReturn(true);
        User admin = new User();
        admin.setRole(Role.ADMIN);
        when(userSession.getUser()).thenReturn(admin);

        banBean.setBanUsername("user1");
        banBean.setReason("reason");
        banBean.setEndDate(LocalDateTime.now().plusDays(1));

        when(userService.findByName("user1")).thenReturn(null);

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.banUser();

            jsfUtilMockedStatic.verify(() -> JSFUtil.addErrorMessage("Użytkownik o podanej nazwie nie istnieje."));
            verify(bansService, never()).save(any());
        }
    }

    @Test
    void testBanUser_emptyReason_showsError() {
        when(userSession.isLoggedIn()).thenReturn(true);
        User admin = new User();
        admin.setRole(Role.ADMIN);
        when(userSession.getUser()).thenReturn(admin);

        User user = new User();
        when(userService.findByName("user1")).thenReturn(user);

        banBean.setBanUsername("user1");
        banBean.setReason("");
        banBean.setEndDate(LocalDateTime.now().plusDays(1));

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.banUser();

            jsfUtilMockedStatic.verify(() -> JSFUtil.addErrorMessage("Powód banowania nie może być pusty."));
            verify(bansService, never()).save(any());
        }
    }

    @Test
    void testBanUser_nullEndDate_showsError() {
        when(userSession.isLoggedIn()).thenReturn(true);
        User admin = new User();
        admin.setRole(Role.ADMIN);
        when(userSession.getUser()).thenReturn(admin);

        User user = new User();
        when(userService.findByName("user1")).thenReturn(user);

        banBean.setBanUsername("user1");
        banBean.setReason("reason");
        banBean.setEndDate(null);

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.banUser();

            jsfUtilMockedStatic.verify(() -> JSFUtil.addErrorMessage("Data zakończenia bana nie może być pusta."));
            // The original code does not return here, so ban is saved despite the error.
            ArgumentCaptor<Ban> captor = ArgumentCaptor.forClass(Ban.class);
            verify(bansService).save(captor.capture());
            Ban savedBan = captor.getValue();
            assertEquals("reason", savedBan.getReason());
            assertNull(savedBan.getEndDate());
            assertEquals(user, savedBan.getBannedUser());

            jsfUtilMockedStatic.verify(() -> JSFUtil.addInfoMessage("Pomyślnie zbanowano użytkownika."));
        }
    }

    @Test
    void testBanUser_validInput_savesBanAndShowsInfo() {
        when(userSession.isLoggedIn()).thenReturn(true);
        User admin = new User();
        admin.setRole(Role.ADMIN);
        when(userSession.getUser()).thenReturn(admin);

        User user = new User();
        when(userService.findByName("user1")).thenReturn(user);

        banBean.setBanUsername("user1");
        banBean.setReason("reason");
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        banBean.setEndDate(endDate);

        try (var jsfUtilMockedStatic = mockStatic(JSFUtil.class)) {
            banBean.banUser();

            ArgumentCaptor<Ban> captor = ArgumentCaptor.forClass(Ban.class);
            verify(bansService).save(captor.capture());
            Ban savedBan = captor.getValue();
            assertEquals("reason", savedBan.getReason());
            assertEquals(endDate, savedBan.getEndDate());
            assertEquals(user, savedBan.getBannedUser());

            jsfUtilMockedStatic.verify(() -> JSFUtil.addInfoMessage("Pomyślnie zbanowano użytkownika."));
        }
    }
}