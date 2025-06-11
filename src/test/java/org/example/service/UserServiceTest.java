package org.example.service;

import jakarta.persistence.NoResultException;
import org.example.dao.UserDao;
import org.example.model.User;
import org.example.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    private User user;
    private static final Long VALID_ID = 1L;
    private static final String VALID_USERNAME = "testUser";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "password123";
    private static final String HASHED_PASSWORD = "hashedPassword123";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(VALID_ID);
        user.setName(VALID_USERNAME);
        user.setEmail(VALID_EMAIL);
        user.setPassword(HASHED_PASSWORD);
    }

    @Test
    void findByName_WhenUserExists_ShouldReturnUser() {
        // Arrange
        String username = "testUser";
        User expectedUser = new User();
        expectedUser.setName(username);
        when(userDao.findByName(username)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.findByName(username);

        // Assert
        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(userDao).findByName(username);
    }

    @Test
    void findByName_WhenUserDoesNotExist_ShouldReturnNull() {
        // Arrange
        String username = "nonexistentUser";
        when(userDao.findByName(username)).thenReturn(Optional.empty());

        // Act
        User result = userService.findByName(username);

        // Assert
        assertNull(result);
        verify(userDao).findByName(username);
    }

    @Test
    void findByName_WhenUsernameIsNull_ShouldReturnNull() {
        // Arrange
        when(userDao.findByName(null)).thenReturn(Optional.empty());

        // Act
        User result = userService.findByName(null);

        // Assert
        assertNull(result);
        verify(userDao).findByName(null);
    }

    @Test
    void authenticate_WhenCredentialsValid_ShouldReturnUser() {
        // Arrange
        when(userDao.findByName(VALID_USERNAME)).thenReturn(Optional.of(user));
        
        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.check(VALID_PASSWORD, HASHED_PASSWORD)).thenReturn(true);

            // Act
            Optional<User> result = userService.authenticate(VALID_USERNAME, VALID_PASSWORD);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(user, result.get());
            verify(userDao, times(1)).findByName(VALID_USERNAME);
        }
    }

    @Test
    void authenticate_WhenInvalidPassword_ShouldReturnEmpty() {
        // Arrange
        when(userDao.findByName(VALID_USERNAME)).thenReturn(Optional.of(user));
        
        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.check(anyString(), eq(HASHED_PASSWORD))).thenReturn(false);

            // Act
            Optional<User> result = userService.authenticate(VALID_USERNAME, "wrongPassword");

            // Assert
            assertTrue(result.isEmpty());
            verify(userDao, times(1)).findByName(VALID_USERNAME);
        }
    }

    @Test
    void authenticate_WhenUserNotFound_ShouldReturnEmpty() {
        // Arrange
        when(userDao.findByName(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.authenticate("nonexistentUser", VALID_PASSWORD);

        // Assert
        assertTrue(result.isEmpty());
        verify(userDao, times(1)).findByName("nonexistentUser");
    }

    @Test
    void authenticate_WhenNoResultException_ShouldReturnEmpty() {
        // Arrange
        when(userDao.findByName(anyString())).thenThrow(new NoResultException());

        // Act
        Optional<User> result = userService.authenticate(VALID_USERNAME, VALID_PASSWORD);

        // Assert
        assertTrue(result.isEmpty());
        verify(userDao, times(1)).findByName(VALID_USERNAME);
    }

    @Test
    void usernameOrEmailTaken_WhenBothMatch_ShouldReturnTrue() {
        // Arrange
        when(userDao.findByName(VALID_USERNAME)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.usernameOrEmailTaken(VALID_USERNAME, VALID_EMAIL);

        // Assert
        assertTrue(result);
        verify(userDao, times(1)).findByName(VALID_USERNAME);
    }

    @Test
    void usernameOrEmailTaken_WhenEmailDoesntMatch_ShouldReturnFalse() {
        // Arrange
        when(userDao.findByName(VALID_USERNAME)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.usernameOrEmailTaken(VALID_USERNAME, "different@email.com");

        // Assert
        assertFalse(result);
        verify(userDao, times(1)).findByName(VALID_USERNAME);
    }

    @Test
    void usernameOrEmailTaken_WhenUserNotFound_ShouldReturnFalse() {
        // Arrange
        when(userDao.findByName(anyString())).thenReturn(Optional.empty());

        // Act
        boolean result = userService.usernameOrEmailTaken("newuser", VALID_EMAIL);

        // Assert
        assertFalse(result);
        verify(userDao, times(1)).findByName("newuser");
    }

    @Test
    void saveUser_ShouldCallDaoSave() {
        // Act
        userService.saveUser(user);

        // Assert
        verify(userDao, times(1)).save(user);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void getUser_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userDao.find(VALID_ID)).thenReturn(user);

        // Act
        User result = userService.getUser(VALID_ID);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
        verify(userDao, times(1)).find(VALID_ID);
    }

    @Test
    void getUser_WhenUserDoesNotExist_ShouldReturnNull() {
        // Arrange
        when(userDao.find(anyLong())).thenReturn(null);

        // Act
        User result = userService.getUser(999L);

        // Assert
        assertNull(result);
        verify(userDao, times(1)).find(999L);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteAndReturnTrue() {
        // Arrange
        when(userDao.find(VALID_ID)).thenReturn(user);

        // Act
        boolean result = userService.deleteUser(VALID_ID);

        // Assert
        assertTrue(result);
        verify(userDao, times(1)).find(VALID_ID);
        verify(userDao, times(1)).delete(user);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldReturnTrue() {
        // Arrange
        when(userDao.find(anyLong())).thenReturn(null);

        // Act
        boolean result = userService.deleteUser(999L);

        // Assert
        assertTrue(result);
        verify(userDao, times(1)).find(999L);
        verify(userDao, never()).delete(any());
    }

    @Test
    void deleteUser_WhenIdIsNull_ShouldReturnTrue() {
        // Arrange
        when(userDao.find(null)).thenReturn(null);

        // Act
        boolean result = userService.deleteUser(null);

        // Assert
        assertTrue(result);
        verify(userDao, times(1)).find(null);
        verify(userDao, never()).delete(any());
    }
}