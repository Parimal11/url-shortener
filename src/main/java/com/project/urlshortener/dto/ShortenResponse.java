package com.project.urlshortener.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShortenResponse {

    private String shortCode;
    private String shortUrl;
}
