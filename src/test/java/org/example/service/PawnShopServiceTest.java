package org.example.service;

import org.example.dao.PawnShopDao;
import org.example.model.PawnShop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PawnShopServiceTest {

    @Mock
    private PawnShopDao pawnShopDao;

    @InjectMocks
    private PawnShopService pawnShopService;

    private PawnShop pawnShop;
    private static final Long VALID_ID = 1L;

    @BeforeEach
    void setUp() {
        pawnShop = new PawnShop();
        pawnShop.setUserId(VALID_ID);
        pawnShop.setName("Test Pawn Shop");
    }

    @Test
    void savePawnShop_ShouldCallDaoSave() {
        // Act
        pawnShopService.savePawnShop(pawnShop);

        // Assert
        verify(pawnShopDao, times(1)).save(pawnShop);
        verifyNoMoreInteractions(pawnShopDao);
    }

    @Test
    void savePawnShop_WhenPawnShopIsNull_ShouldCallDaoSave() {
        // Act
        pawnShopService.savePawnShop(null);

        // Assert
        verify(pawnShopDao, times(1)).save(null);
        verifyNoMoreInteractions(pawnShopDao);
    }

    @Test
    void getPawnShop_WhenPawnShopExists_ShouldReturnPawnShop() {
        // Arrange
        when(pawnShopDao.find(VALID_ID)).thenReturn(pawnShop);

        // Act
        PawnShop result = pawnShopService.getPawnShop(VALID_ID);

        // Assert
        assertNotNull(result);
        assertEquals(pawnShop, result);
        assertEquals(VALID_ID, result.getUserId());
        assertEquals("Test Pawn Shop", result.getName());
        verify(pawnShopDao, times(1)).find(VALID_ID);
        verifyNoMoreInteractions(pawnShopDao);
    }

    @Test
    void getPawnShop_WhenPawnShopDoesNotExist_ShouldReturnNull() {
        // Arrange
        when(pawnShopDao.find(VALID_ID)).thenReturn(null);

        // Act
        PawnShop result = pawnShopService.getPawnShop(VALID_ID);

        // Assert
        assertNull(result);
        verify(pawnShopDao, times(1)).find(VALID_ID);
        verifyNoMoreInteractions(pawnShopDao);
    }

    @Test
    void getPawnShop_WhenIdIsNull_ShouldReturnNull() {
        // Arrange
        when(pawnShopDao.find(null)).thenReturn(null);

        // Act
        PawnShop result = pawnShopService.getPawnShop(null);

        // Assert
        assertNull(result);
        verify(pawnShopDao, times(1)).find(null);
        verifyNoMoreInteractions(pawnShopDao);
    }

    @Test
    void deletePawnShop_WhenPawnShopExists_ShouldDeleteAndReturnTrue() {
        // Arrange
        when(pawnShopDao.find(VALID_ID)).thenReturn(pawnShop);

        // Act
        boolean result = pawnShopService.deletePawnShop(VALID_ID);

        // Assert
        assertTrue(result);
        verify(pawnShopDao, times(1)).find(VALID_ID);
        verify(pawnShopDao, times(1)).delete(pawnShop);
        verifyNoMoreInteractions(pawnShopDao);
    }

    @Test
    void deletePawnShop_WhenPawnShopDoesNotExist_ShouldReturnTrue() {
        // Arrange
        when(pawnShopDao.find(VALID_ID)).thenReturn(null);

        // Act
        boolean result = pawnShopService.deletePawnShop(VALID_ID);

        // Assert
        assertTrue(result);
        verify(pawnShopDao, times(1)).find(VALID_ID);
        verify(pawnShopDao, never()).delete(any());
        verifyNoMoreInteractions(pawnShopDao);
    }

    @Test
    void deletePawnShop_WhenIdIsNull_ShouldReturnTrue() {
        // Arrange
        when(pawnShopDao.find(null)).thenReturn(null);

        // Act
        boolean result = pawnShopService.deletePawnShop(null);

        // Assert
        assertTrue(result);
        verify(pawnShopDao, times(1)).find(null);
        verify(pawnShopDao, never()).delete(any());
        verifyNoMoreInteractions(pawnShopDao);
    }
}