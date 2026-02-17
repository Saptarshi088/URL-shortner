package com.saptarshu.URLShortner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlCreateRequest {

    @NotBlank(message = "URL cannot be empty")
    @Pattern(
            regexp = "^https?://.*",
            message = "URL must start with http:// or https://"
    )
    private String url;



    public UrlCreateRequest() {
    }

    public UrlCreateRequest(String url, String customCode) {
        this.url = url;
    }


}
