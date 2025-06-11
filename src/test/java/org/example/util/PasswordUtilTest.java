package org.example.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void hash_ShouldGenerateValidBCryptHash() {
        // Arrange
        String password = "testPassword123";

        // Act
        String hash = PasswordUtil.hash(password);

        // Assert
        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$"));
    }

    @Test
    void hash_ShouldGenerateDifferentHashesForSamePassword() {
        // Arrange
        String password = "testPassword123";

        // Act
        String hash1 = PasswordUtil.hash(password);
        String hash2 = PasswordUtil.hash(password);

        // Assert
        assertNotEquals(hash1, hash2);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "simplePass",
            "Complex123!@#",
            "短いパスワード",  // Japanese characters
            "слово",          // Cyrillic characters
            "    spaces    ",
            "1234567890"
    })
    void hash_ShouldHandleVariousPasswords(String password) {
        // Act & Assert
        assertDoesNotThrow(() -> {
            String hash = PasswordUtil.hash(password);
            assertTrue(PasswordUtil.check(password, hash));
        });
    }

    @Test
    void check_ShouldReturnTrueForCorrectPassword() {
        // Arrange
        String password = "testPassword123";
        String hash = PasswordUtil.hash(password);

        // Act
        boolean result = PasswordUtil.check(password, hash);

        // Assert
        assertTrue(result);
    }

    @Test
    void check_ShouldReturnFalseForIncorrectPassword() {
        // Arrange
        String password = "testPassword123";
        String wrongPassword = "wrongPassword123";
        String hash = PasswordUtil.hash(password);

        // Act
        boolean result = PasswordUtil.check(wrongPassword, hash);

        // Assert
        assertFalse(result);
    }

    @Test
    void check_ShouldReturnFalseForEmptyPassword() {
        // Arrange
        String password = "testPassword123";
        String hash = PasswordUtil.hash(password);

        // Act
        boolean result = PasswordUtil.check("", hash);

        // Assert
        assertFalse(result);
    }

    @Test
    void check_ShouldHandleSpecialCharacters() {
        // Arrange
        String password = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        String hash = PasswordUtil.hash(password);

        // Act
        boolean result = PasswordUtil.check(password, hash);

        // Assert
        assertTrue(result);
    }

    @Test
    void check_ShouldReturnFalseForNonBCryptHash() {
        // Arrange
        String password = "testPassword123";
        String invalidHash = "invalid_hash_format";

        // Act
        boolean result = PasswordUtil.check(password, invalidHash);

        // Assert
        assertFalse(result);
    }

    @Test
    void hash_ShouldThrowExceptionForNullPassword() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> PasswordUtil.hash(null));
    }

    @Test
    void check_ShouldThrowExceptionForNullPassword() {
        // Arrange
        String hash = PasswordUtil.hash("testPassword123");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> PasswordUtil.check(null, hash));
    }

    @Test
    void check_ShouldThrowExceptionForNullHash() {
        // Arrange
        String password = "testPassword123";

        // Act & Assert
        assertThrows(NullPointerException.class, () -> PasswordUtil.check(password, null));
    }
}