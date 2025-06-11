package org.example.service;

import org.example.dao.AdminDao;
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
class AdminServiceTest {

    @Mock
    private AdminDao adminDao;

    @InjectMocks
    private AdminService adminService;

    private Admin admin1;
    private Admin admin2;

    @BeforeEach
    void setUp() {
        admin1 = new Admin();
        admin1.setUserId(1L);
        admin1.setName("admin1");

        admin2 = new Admin();
        admin2.setUserId(2L);
        admin2.setName("admin2");
    }

    @Test
    void findAllAdmins_ShouldReturnAllAdmins() {
        // Arrange
        List<Admin> expectedAdmins = Arrays.asList(admin1, admin2);
        when(adminDao.findAll()).thenReturn(expectedAdmins);

        // Act
        List<Admin> actualAdmins = adminService.findAllAdmins();

        // Assert
        assertNotNull(actualAdmins);
        assertEquals(expectedAdmins, actualAdmins);
        verify(adminDao).findAll();
    }

    @Test
    void findAllAdmins_WhenNoAdmins_ShouldReturnEmptyList() {
        // Arrange
        when(adminDao.findAll()).thenReturn(List.of());

        // Act
        List<Admin> actualAdmins = adminService.findAllAdmins();

        // Assert
        assertNotNull(actualAdmins);
        assertTrue(actualAdmins.isEmpty());
        verify(adminDao).findAll();
    }

    @Test
    void saveAdmin_ShouldCallDaoSave() {
        // Arrange
        Admin adminToSave = admin1;

        // Act
        adminService.saveAdmin(adminToSave);

        // Assert
        verify(adminDao).save(adminToSave);
    }

    @Test
    void getAdmin_WhenAdminExists_ShouldReturnAdmin() {
        // Arrange
        Long adminId = 1L;
        when(adminDao.find(adminId)).thenReturn(admin1);

        // Act
        Admin foundAdmin = adminService.getAdmin(adminId);

        // Assert
        assertNotNull(foundAdmin);
        assertEquals(admin1, foundAdmin);
        verify(adminDao).find(adminId);
    }

    @Test
    void getAdmin_WhenAdminDoesNotExist_ShouldReturnNull() {
        // Arrange
        Long adminId = 999L;
        when(adminDao.find(adminId)).thenReturn(null);

        // Act
        Admin foundAdmin = adminService.getAdmin(adminId);

        // Assert
        assertNull(foundAdmin);
        verify(adminDao).find(adminId);
    }

    @Test
    void deleteAdmin_WhenAdminExists_ShouldDeleteAndReturnTrue() {
        // Arrange
        Long adminId = 1L;
        when(adminDao.find(adminId)).thenReturn(admin1);

        // Act
        boolean result = adminService.deleteAdmin(adminId);

        // Assert
        assertTrue(result);
        verify(adminDao).find(adminId);
        verify(adminDao).delete(admin1);
    }

    @Test
    void deleteAdmin_WhenAdminDoesNotExist_ShouldReturnTrue() {
        // Arrange
        Long adminId = 999L;
        when(adminDao.find(adminId)).thenReturn(null);

        // Act
        boolean result = adminService.deleteAdmin(adminId);

        // Assert
        assertTrue(result);
        verify(adminDao).find(adminId);
        verify(adminDao, never()).delete(any());
    }

    @Test
    void saveAdmin_WhenAdminIsNull_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.saveAdmin(null),
                "Expected saveAdmin() to throw IllegalArgumentException"
        );

        assertAll(
                () -> assertEquals("Admin cannot be null", exception.getMessage()),
                () -> verify(adminDao, never()).save(any())
        );
    }
}