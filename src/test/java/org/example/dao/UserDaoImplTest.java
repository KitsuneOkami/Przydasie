
package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<User> typedQuery;

    @InjectMocks
    private UserDaoImpl userDao;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        user1 = new User();
        user1.setUserId(1L);
        user1.setName("testUser1");

        user2 = new User();
        user2.setUserId(2L);
        user2.setName("testUser2");
    }

    @Test
    void findAll_ShouldReturnListOfUsers() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(entityManager.createQuery("SELECT u FROM User u", User.class))
            .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userDao.findAll();

        // Assert
        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertEquals(expectedUsers, actualUsers);
        verify(entityManager).createQuery("SELECT u FROM User u", User.class);
        verify(typedQuery).getResultList();
    }

    @Test
    void findAll_ShouldReturnEmptyListWhenNoUsersExist() {
        // Arrange
        when(entityManager.createQuery("SELECT u FROM User u", User.class))
            .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        // Act
        List<User> actualUsers = userDao.findAll();

        // Assert
        assertNotNull(actualUsers);
        assertTrue(actualUsers.isEmpty());
        verify(entityManager).createQuery("SELECT u FROM User u", User.class);
        verify(typedQuery).getResultList();
    }

    @Test
    void findByName_ShouldReturnUserWhenExists() {
        // Arrange
        String username = "testUser1";
        when(entityManager.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter("name", username)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(user1);

        // Act
        Optional<User> foundUser = userDao.findByName(username);

        // Assert
        assertTrue(foundUser.isPresent());
        assertNotNull(foundUser.get());
        assertEquals(username, foundUser.get().getName());

        // Verify essential interactions
        verify(entityManager).createQuery("SELECT u FROM User u WHERE u.name = :name", User.class);
        verify(typedQuery).getSingleResult();
    }

    @Test
    void findByName_ShouldReturnEmptyOptionalWhenNoUserExists() {
        // Arrange
        String username = "nonExistentUser";
        when(entityManager.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter("name", username)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(new jakarta.persistence.NoResultException());

        // Act
        Optional<User> foundUser = userDao.findByName(username);

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void findByName_ShouldReturnEmptyOptionalWhenUserDoesNotExist() {
        // Arrange
        String username = "nonexistentUser";
        when(entityManager.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter("name", username)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(new jakarta.persistence.NoResultException());

        // Act
        Optional<User> foundUser = userDao.findByName(username);

        // Assert
        assertFalse(foundUser.isPresent());
        verify(entityManager).createQuery("SELECT u FROM User u WHERE u.name = :name", User.class);
        verify(typedQuery).setParameter("name", username);
        verify(typedQuery).getSingleResult();
    }

    @Test
    void find_ShouldReturnUserWhenExists() {
        // Arrange
        when(entityManager.find(User.class, 1L)).thenReturn(user1);

        // Act
        User foundUser = userDao.find(1L);

        // Assert
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getUserId());
        assertEquals("testUser1", foundUser.getName());
        verify(entityManager).find(User.class, 1L);
    }

    @Test
    void find_ShouldReturnNullWhenUserDoesNotExist() {
        // Arrange
        when(entityManager.find(User.class, 999L)).thenReturn(null);

        // Act
        User foundUser = userDao.find(999L);

        // Assert
        assertNull(foundUser);
        verify(entityManager).find(User.class, 999L);
    }
}