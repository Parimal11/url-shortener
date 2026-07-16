package com.project.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.urlshortener.dto.ShortenRequest;
import com.project.urlshortener.dto.ShortenResponse;
import com.project.urlshortener.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean   // use @MockBean if you're on Spring Boot <=3.3
    private UrlService urlService;

    @Test
    void shouldCreateShortUrl() throws Exception {

        String requestBody = """
        {
            "url": "https://google.com"
        }
        """;

        ShortenResponse response =
                new ShortenResponse(
                        "abc123",
                        "http://localhost:8080/abc123"
                );

        when(urlService.shorten(any(ShortenRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.shortCode").value("abc123"))
                .andExpect(jsonPath("$.shortUrl").value("http://localhost:8080/abc123"));
    }

    @Test
    void shouldRedirectToOriginalUrl() throws Exception {

        when(urlService.getOriginalUrl("abc123"))
                .thenReturn("https://google.com");

        mockMvc.perform(get("/abc123"))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string(
                        HttpHeaders.LOCATION,
                        "https://google.com"));
    }
}