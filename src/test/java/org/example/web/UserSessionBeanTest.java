package org.example.web;

import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSessionBeanTest {

    private UserSessionBean userSessionBean;

    @BeforeEach
    void setUp() {
        userSessionBean = new UserSessionBean();
        userSessionBean.init();
    }

    @Test
    void init_ShouldSetUsernameAndUserToNull() {
        assertNull(userSessionBean.getUsername());
        assertNull(userSessionBean.getUser());
    }

    @Test
    void setUser_ShouldSetUserAndUsername() {
        User user = new User();
        user.setName("testuser");

        userSessionBean.setUser(user);

        assertEquals(user, userSessionBean.getUser());
        assertEquals("testuser", userSessionBean.getUsername());
    }

    @Test
    void isLoggedIn_ShouldReturnTrueWhenUsernameSet() {
        User user = new User();
        user.setName("testuser");
        userSessionBean.setUser(user);

        assertTrue(userSessionBean.isLoggedIn());
    }

    @Test
    void isLoggedIn_ShouldReturnFalseWhenUsernameNull() {
        userSessionBean.init(); // resets username and user to null

        assertFalse(userSessionBean.isLoggedIn());
    }

    @Test
    void logout_ShouldClearUserAndUsername() {
        User user = new User();
        user.setName("testuser");
        userSessionBean.setUser(user);

        userSessionBean.logout();

        assertNull(userSessionBean.getUser());
        assertNull(userSessionBean.getUsername());
    }
}
