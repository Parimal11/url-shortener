package com.project.urlshortener.util;

import org.springframework.stereotype.Component;

import java.net.URI;


/**
 * Utility responsible for validating URLs.
 *
 * Only HTTP and HTTPS URLs are considered valid.
 */
@Component
public class UrlValidator {

    public boolean isValid(String url) {

        try{
            URI uri = new URI(url);

            return uri.getScheme() != null &&
                    (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https"))
                    && uri.getHost() != null;
        } catch (Exception e) {
            return false;
        }
    }

}
