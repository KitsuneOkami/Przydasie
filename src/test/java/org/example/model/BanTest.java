package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BanTest
{

    @Test
    void constructor_ShouldCreateInstance() {
        Ban ban = new Ban();
        assertNotNull(ban, "BanList instance should not be null");
    }
}