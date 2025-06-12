package org.example.service;

import org.example.dao.BanDao;
import org.example.model.Ban;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BansServiceTest {

    private BansService bansService;
    private BanDao banDao;

    @BeforeEach
    void setUp() throws Exception {
        banDao = mock(BanDao.class);
        bansService = new BansService();

        var field = BansService.class.getDeclaredField("banDao");
        field.setAccessible(true);
        field.set(bansService, banDao);
    }

    @Test
    void isUserBanned_whenUserIsNull_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> bansService.isUserBanned(null));
    }
}
