package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

public class UserTest {

    @Mock
    private User user;
    private User admin;
    private User pawnShopOwner;

    @BeforeEach
    void setUp() {
        // Initialize test data
        user = new User();
        user.setUserId(1L);
        user.setRole(User.Role.USER);

        admin = new User();
        admin.setUserId(2L);
        admin.setRole(User.Role.ADMIN);

        pawnShopOwner = new User();
        pawnShopOwner.setUserId(3L);
        pawnShopOwner.setRole(User.Role.PAWN_SHOP);
    }

    @Test
    void shouldReturnTrueWhenUserIsUser() {
        assert User.Role.USER.equals(user.getRole());
    }

    @Test
    void shouldReturnTrueWhenUserIsAdmin() {
        assert User.Role.ADMIN.equals(admin.getRole());
    }

    @Test
    void shouldReturnTrueWhenUserIsPawnShopOwner() {
        assert User.Role.PAWN_SHOP.equals(pawnShopOwner.getRole());
    }
}
