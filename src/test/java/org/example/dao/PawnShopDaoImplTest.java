
package org.example.dao;

import jakarta.persistence.EntityManager;
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
class PawnShopDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PawnShopDaoImpl pawnShopDao;

    private PawnShop pawnShop;

    @BeforeEach
    void setUp() {
        // Initialize test data
        pawnShop = new PawnShop();
        pawnShop.setUserId(1L);
    }

    @Test
    void find_ShouldReturnPawnShopWhenExists() {
        // Arrange
        when(entityManager.find(PawnShop.class, 1L)).thenReturn(pawnShop);

        // Act
        PawnShop foundPawnShop = pawnShopDao.find(1L);

        // Assert
        assertNotNull(foundPawnShop);
        assertEquals(1L, foundPawnShop.getUserId());
        verify(entityManager).find(PawnShop.class, 1L);
    }

    @Test
    void find_ShouldReturnNullWhenPawnShopDoesNotExist() {
        // Arrange
        when(entityManager.find(PawnShop.class, 999L)).thenReturn(null);

        // Act
        PawnShop foundPawnShop = pawnShopDao.find(999L);

        // Assert
        assertNull(foundPawnShop);
        verify(entityManager).find(PawnShop.class, 999L);
    }
}