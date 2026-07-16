package com.project.urlshortener.controller;

import com.project.urlshortener.dto.ShortenRequest;
import com.project.urlshortener.dto.ShortenResponse;
import com.project.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * REST Controller responsible for URL shortening and redirection operations.
 *
 * Exposes endpoints to:
 * <ul>
 *     <li>Create short URLs</li>
 *     <li>Redirect short URLs to their original destinations</li>
 * </ul>
 */
@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;


    /**
     * Creates a shortened URL for the given request.
     *
     * @param request contains the original URL and optional custom alias
     * @return shortened URL response
     */
    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shorten(
            @Valid @RequestBody ShortenRequest request) {

        ShortenResponse response = urlService.shorten(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    /**
     * Redirects the client to the original URL associated with
     * the supplied short code.
     *
     * @param code short URL identifier
     * @return HTTP 301 redirect response
     */
    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(
            @PathVariable String code) {

        String originalUrl = urlService.getOriginalUrl(code);

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create(originalUrl))
                .build();
    }
}