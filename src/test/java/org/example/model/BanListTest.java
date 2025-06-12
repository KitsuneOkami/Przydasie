package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BanListTest {

    @Test
    void constructor_ShouldCreateInstance() {
        BanList banList = new BanList();
        assertNotNull(banList, "BanList instance should not be null");
    }
}