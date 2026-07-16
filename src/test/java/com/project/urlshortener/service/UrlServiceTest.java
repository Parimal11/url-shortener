package com.project.urlshortener.service;

import com.project.urlshortener.dto.ShortenRequest;
import com.project.urlshortener.dto.ShortenResponse;
import com.project.urlshortener.entity.UrlMapping;
import com.project.urlshortener.exception.AliasAlreadyExistsException;
import com.project.urlshortener.exception.InvalidUrlException;
import com.project.urlshortener.exception.ResourceNotFoundException;
import com.project.urlshortener.repository.UrlMappingRepository;
import com.project.urlshortener.util.ShortCodeGenerator;
import com.project.urlshortener.util.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlMappingRepository repository;

    @Mock
    private UrlValidator validator;

    @Mock
    private ShortCodeGenerator generator;

    @InjectMocks
    private UrlService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                service,
                "baseUrl",
                "http://localhost:8080"
        );
    }

    @Test
    void shouldCreateShortUrl() {

        ShortenRequest request = new ShortenRequest();
        request.setUrl("https://google.com");

        when(validator.isValid(request.getUrl()))
                .thenReturn(true);

        when(repository.findByOriginalUrl(request.getUrl()))
                .thenReturn(Optional.empty());

        when(generator.generate())
                .thenReturn("abc123");

        when(repository.existsByShortCode("abc123"))
                .thenReturn(false);

        UrlMapping savedMapping = UrlMapping.builder()
                .id(1L)
                .originalUrl(request.getUrl())
                .shortCode("abc123")
                .customAlias(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.save(any(UrlMapping.class)))
                .thenReturn(savedMapping);

        ShortenResponse response = service.shorten(request);

        assertNotNull(response);
        assertEquals("abc123", response.getShortCode());
        assertEquals(
                "http://localhost:8080/abc123",
                response.getShortUrl()
        );

        verify(repository, times(1))
                .save(any(UrlMapping.class));
    }

    @Test
    void shouldReturnExistingShortUrlForDuplicateRequest() {

        ShortenRequest request = new ShortenRequest();
        request.setUrl("https://google.com");

        when(validator.isValid(request.getUrl()))
                .thenReturn(true);

        UrlMapping existing = UrlMapping.builder()
                .id(1L)
                .originalUrl(request.getUrl())
                .shortCode("xyz789")
                .customAlias(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.findByOriginalUrl(request.getUrl()))
                .thenReturn(Optional.of(existing));

        ShortenResponse response = service.shorten(request);

        assertEquals("xyz789", response.getShortCode());
        assertEquals(
                "http://localhost:8080/xyz789",
                response.getShortUrl()
        );

        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidUrlException() {

        ShortenRequest request = new ShortenRequest();
        request.setUrl("invalid-url");

        when(validator.isValid(request.getUrl()))
                .thenReturn(false);

        assertThrows(
                InvalidUrlException.class,
                () -> service.shorten(request)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowAliasAlreadyExistsException() {

        ShortenRequest request = new ShortenRequest();
        request.setUrl("https://google.com");
        request.setAlias("google");

        when(validator.isValid(request.getUrl()))
                .thenReturn(true);

        when(repository.findByOriginalUrl(request.getUrl()))
                .thenReturn(Optional.empty());

        when(repository.existsByShortCode("google"))
                .thenReturn(true);

        assertThrows(
                AliasAlreadyExistsException.class,
                () -> service.shorten(request)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void shouldReturnOriginalUrl() {

        UrlMapping mapping = UrlMapping.builder()
                .id(1L)
                .originalUrl("https://google.com")
                .shortCode("abc123")
                .customAlias(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.findByShortCode("abc123"))
                .thenReturn(Optional.of(mapping));

        String originalUrl = service.getOriginalUrl("abc123");

        assertEquals(
                "https://google.com",
                originalUrl
        );
    }

    @Test
    void shouldThrowResourceNotFoundException() {

        when(repository.findByShortCode("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getOriginalUrl("unknown")
        );
    }

    @Test
    void shouldThrowExceptionWhenDuplicateUrlIsRequestedWithCustomAlias() {

        ShortenRequest request = new ShortenRequest();
        request.setUrl("https://google.com");
        request.setAlias("newAlias");

        when(validator.isValid(request.getUrl()))
                .thenReturn(true);

        UrlMapping existing = UrlMapping.builder()
                .originalUrl(request.getUrl())
                .shortCode("oldCode")
                .build();

        when(repository.findByOriginalUrl(request.getUrl()))
                .thenReturn(Optional.of(existing));

        assertThrows(
                AliasAlreadyExistsException.class,
                () -> service.shorten(request)
        );

        verify(repository, never()).save(any());
    }

}