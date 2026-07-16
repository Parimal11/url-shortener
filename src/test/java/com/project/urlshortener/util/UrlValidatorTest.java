package com.project.urlshortener.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorTest {

    private UrlValidator urlValidator;

    @BeforeEach
    void setUp() {
        urlValidator = new UrlValidator();
    }

    @Test
    void shouldReturnTrueForValidHttpUrl() {

        String url = "http://google.com";

        assertTrue(urlValidator.isValid(url));
    }

    @Test
    void shouldReturnTrueForValidHttpsUrl() {

        String url = "https://openai.com";

        assertTrue(urlValidator.isValid(url));
    }

    @Test
    void shouldReturnFalseForInvalidUrl() {

        String url = "invalid-url";

        assertFalse(urlValidator.isValid(url));
    }

    @Test
    void shouldReturnFalseForBlankUrl() {

        String url = "";

        assertFalse(urlValidator.isValid(url));
    }

    @Test
    void shouldReturnFalseForNullUrl() {

        assertFalse(urlValidator.isValid(null));
    }

    @Test
    void shouldReturnFalseForUnsupportedProtocol() {

        String url = "ftp://example.com";

        assertFalse(urlValidator.isValid(url));
    }
}