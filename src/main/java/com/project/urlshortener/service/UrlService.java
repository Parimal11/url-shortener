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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Service responsible for URL shortening and lookup operations.
 *
 * Handles:
 * - URL validation
 * - Duplicate URL detection
 * - Custom alias handling
 * - Short-code generation
 * - Redirect lookup
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UrlService {

    private final UrlMappingRepository repository;
    private final UrlValidator validator;
    private final ShortCodeGenerator generator;

    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * Creates a shortened URL.
     *
     * If the URL already exists, the existing mapping is returned.
     * If a custom alias is supplied, uniqueness is validated.
     *
     * @param request URL shortening request
     * @return shortened URL response
     * @throws InvalidUrlException if the URL is invalid
     * @throws AliasAlreadyExistsException if alias already exists
     */
    public ShortenResponse shorten(ShortenRequest request) {

        log.info("Received shorten request for URL: {}", request.getUrl());

        validateRequest(request);


        Optional<UrlMapping> existingMapping =
                repository.findByOriginalUrl(request.getUrl());

        if (existingMapping.isPresent()) {

            log.info("Existing mapping found for URL '{}'. Returning existing short code '{}'.",
                    request.getUrl(),
                    existingMapping.get().getShortCode());

            if(hasCustomAlias(request)){
                log.warn("Custom alias '{}' requested for an already shortened URL '{}'.",
                        request.getAlias(),
                        request.getUrl());
                throw new AliasAlreadyExistsException("URL already exists with another short code");
            }
            return buildResponse(existingMapping.get());
        }

        String shortCode = resolveShortCode(request);

        UrlMapping mapping = createEntity(request, shortCode);

        UrlMapping savedMapping = repository.save(mapping);

        log.info("Successfully created short URL. Original URL='{}', Short Code='{}'.",
                savedMapping.getOriginalUrl(),
                savedMapping.getShortCode());

        return buildResponse(savedMapping);
    }

    /**
     * Retrieves the original URL associated with a short code.
     *
     * @param shortCode generated/custom short code
     * @return original URL
     * @throws ResourceNotFoundException if mapping does not exist
     */
    public String getOriginalUrl(String shortCode) {

        log.info("Received redirect request for short code '{}'.", shortCode);

        UrlMapping mapping = repository.findByShortCode(shortCode)
                .orElseThrow(() -> {

                    log.warn("No URL mapping found for short code '{}'.", shortCode);

                    return new ResourceNotFoundException("Short code not found");
                });

        log.info("Redirecting short code '{}' to '{}'.",
                shortCode,
                mapping.getOriginalUrl());

        return mapping.getOriginalUrl();
    }

    /**
     * Validates incoming request.
     */
    private void validateRequest(ShortenRequest request) {

        if (!validator.isValid(request.getUrl())) {

            log.warn("Invalid URL received: {}", request.getUrl());

            throw new InvalidUrlException("Invalid URL");
        }
    }

    /**
     * Resolves the short code.
     * Uses custom alias if provided.
     * Otherwise, generates a unique Base62 code.
     */
    private String resolveShortCode(ShortenRequest request) {

        if (hasCustomAlias(request)) {

            log.info("Custom alias '{}' requested.", request.getAlias());

            if (repository.existsByShortCode(request.getAlias())) {

                log.warn("Custom alias '{}' already exists.",
                        request.getAlias());

                throw new AliasAlreadyExistsException(
                        "Alias already exists");
            }

            log.info("Using custom alias '{}'.",
                    request.getAlias());

            return request.getAlias();
        }

        String generatedCode;

        do {
            generatedCode = generator.generate();

            if (repository.existsByShortCode(generatedCode)) {

                log.warn("Generated short code '{}' already exists. Regenerating.",
                        generatedCode);
            }
        }
        while (repository.existsByShortCode(generatedCode));

        log.info("Generated unique short code '{}'.", generatedCode);

        return generatedCode;
    }

    /**
     * Creates entity for persistence.
     */
    private UrlMapping createEntity(
            ShortenRequest request,
            String shortCode) {

        return UrlMapping.builder()
                .originalUrl(request.getUrl())
                .shortCode(shortCode)
                .customAlias(hasCustomAlias(request))
                .build();
    }

    /**
     * Builds API response.
     */
    private ShortenResponse buildResponse(UrlMapping mapping) {

        return new ShortenResponse(
                mapping.getShortCode(),
                baseUrl + "/" + mapping.getShortCode()
        );
    }

    /**
     * Checks whether user supplied a custom alias.
     */
    private boolean hasCustomAlias(ShortenRequest request) {

        return request.getAlias() != null &&
                !request.getAlias().isBlank();
    }

}