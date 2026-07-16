package com.project.urlshortener.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;


/**
 * Generates random Base62 encoded short codes.
 *
 * Generated codes are validated for uniqueness by the service
 * layer before persistence.
 */
@Component
public class ShortCodeGenerator {

    private static final String BASE62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int SHORT_CODE_LENGTH = 6;

    private final SecureRandom random = new SecureRandom();

    public String generate(){
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < SHORT_CODE_LENGTH; i++){
            builder.append(
                    BASE62.charAt(
                            random.nextInt(BASE62.length())
                    )
            );
        }
        return builder.toString();
    }
}
