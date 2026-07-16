package com.project.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ShortenRequest {

    @NotBlank(message = "URL cannot be blank")
    public String url;

    @Pattern(
            regexp = "^[a-zA-Z0-9_-]*$",
            message = "Alias can contain only letters, numbers, '-' and '_'"
    )
    public String alias;

}
