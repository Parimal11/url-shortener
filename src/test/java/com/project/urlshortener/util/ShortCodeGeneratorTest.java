package com.project.urlshortener.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShortCodeGeneratorTest {

    private ShortCodeGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new ShortCodeGenerator();
    }

    @Test
    void shouldGenerateShortCodeOfLengthSix() {

        String shortCode = generator.generate();

        assertEquals(6, shortCode.length());
    }

    @Test
    void shouldGenerateOnlyBase62Characters() {

        String shortCode = generator.generate();

        assertTrue(shortCode.matches("^[A-Za-z0-9]{6}$"));
    }

    @Test
    void shouldGenerateDifferentCodesOnConsecutiveCalls() {

        String first = generator.generate();
        String second = generator.generate();

        assertNotEquals(first, second);
    }

    @Test
    void shouldGenerateNonNullShortCode() {

        String shortCode = generator.generate();

        assertNotNull(shortCode);
    }

    @Test
    void shouldGenerateNonBlankShortCode() {

        String shortCode = generator.generate();

        assertFalse(shortCode.isBlank());
    }
}