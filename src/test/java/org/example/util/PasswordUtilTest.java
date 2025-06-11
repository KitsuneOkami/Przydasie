package org.example.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {
    private static final String TEST_PASSWORD = "testPassword123";

    @Test
    void constructor_ShouldCreate() {
        // Act & Assert
        assertDoesNotThrow(() -> new PasswordUtil());
    }

    @Test
    void hash_ShouldCreateBCryptHash() {
        // Act
        String hashedPassword = PasswordUtil.hash(TEST_PASSWORD);

        // Assert
        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$"));
    }

    @Test
    void check_WithCorrectPassword_ShouldReturnTrue() {
        // Arrange
        String hashedPassword = PasswordUtil.hash(TEST_PASSWORD);

        // Act & Assert
        assertTrue(PasswordUtil.check(TEST_PASSWORD, hashedPassword));
    }

    @Test
    void check_WithIncorrectPassword_ShouldReturnFalse() {
        // Arrange
        String hashedPassword = PasswordUtil.hash(TEST_PASSWORD);

        // Act & Assert
        assertFalse(PasswordUtil.check("wrongPassword", hashedPassword));
    }

    @Test
    void check_WithEmptyPassword_ShouldReturnFalse() {
        // Arrange
        String hashedPassword = PasswordUtil.hash(TEST_PASSWORD);

        // Act & Assert
        assertFalse(PasswordUtil.check("", hashedPassword));
    }
}