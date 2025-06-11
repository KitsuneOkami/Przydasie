package org.example.util;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractDaoImplTest {

    @Mock
    private EntityManager entityManager;

    private TestDao testDao;
    private TestEntity testEntity;

    @BeforeEach
    void setUp() {
        testDao = new TestDao();
        try {
            var field = AbstractDaoImpl.class.getDeclaredField("entityManager");
            field.setAccessible(true);
            field.set(testDao, entityManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set entityManager field", e);
        }
        testEntity = new TestEntity();
    }


    @Test
    void save_ShouldPersistEntity() {

        // Act
        testDao.save(testEntity);

        // Assert
        verify(entityManager).persist(testEntity);
    }

    @Test
    void update_ShouldMergeEntity() {
        // Act
        testDao.update(testEntity);

        // Assert
        verify(entityManager).merge(testEntity);
    }

    @Test
    void delete_WhenEntityManaged_ShouldRemoveDirectly() {
        // Arrange
        when(entityManager.contains(testEntity)).thenReturn(true);

        // Act
        testDao.delete(testEntity);

        // Assert
        verify(entityManager).remove(testEntity);
        verify(entityManager, never()).merge(testEntity);
    }

    @Test
    void delete_WhenEntityNotManaged_ShouldMergeAndRemove() {
        // Arrange
        when(entityManager.contains(testEntity)).thenReturn(false);
        when(entityManager.merge(testEntity)).thenReturn(testEntity);

        // Act
        testDao.delete(testEntity);

        // Assert
        verify(entityManager).merge(testEntity);
        verify(entityManager).remove(testEntity);
    }

    @Test
    void getEntityManager_ShouldReturnEntityManager() {
        // Act & Assert
        assertEquals(entityManager, testDao.getEntityManager());
    }

    // Helper classes for testing
    private static class TestEntity {
    }

    private static class TestDao extends AbstractDaoImpl<TestEntity, Long> {
        @Override
        public TestEntity find(Long primaryKey) {
            return null; // Not needed for these tests
        }
    }
}