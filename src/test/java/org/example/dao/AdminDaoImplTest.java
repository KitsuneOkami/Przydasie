
package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Admin> typedQuery;

    @InjectMocks
    private AdminDaoImpl adminDao;

    private Admin admin1;
    private Admin admin2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        admin1 = new Admin();
        admin1.setUserId(1L);

        admin2 = new Admin();
        admin2.setUserId(2L);
    }

    @Test
    void findAll_ShouldReturnListOfAdmins() {
        // Arrange
        List<Admin> expectedAdmins = Arrays.asList(admin1, admin2);
        when(entityManager.createQuery("SELECT a FROM Admin a", Admin.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedAdmins);

        // Act
        List<Admin> actualAdmins = adminDao.findAll();

        // Assert
        assertNotNull(actualAdmins);
        assertEquals(2, actualAdmins.size());
        assertEquals(expectedAdmins, actualAdmins);
        verify(entityManager).createQuery("SELECT a FROM Admin a", Admin.class);
        verify(typedQuery).getResultList();
    }

    @Test
    void find_ShouldReturnAdminWhenExists() {
        // Arrange
        when(entityManager.find(Admin.class, 1L)).thenReturn(admin1);

        // Act
        Admin foundAdmin = adminDao.find(1L);

        // Assert
        assertNotNull(foundAdmin);
        assertEquals(1L, foundAdmin.getUserId());
        verify(entityManager).find(Admin.class, 1L);
    }

    @Test
    void find_ShouldReturnNullWhenAdminDoesNotExist() {
        // Arrange
        when(entityManager.find(Admin.class, 999L)).thenReturn(null);

        // Act
        Admin foundAdmin = adminDao.find(999L);

        // Assert
        assertNull(foundAdmin);
        verify(entityManager).find(Admin.class, 999L);
    }
}