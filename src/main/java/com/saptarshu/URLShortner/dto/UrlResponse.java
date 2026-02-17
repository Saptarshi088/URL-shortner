package com.saptarshu.URLShortner.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlResponse {

    private String originalUrl;
    private String shortUrl;
    private LocalDateTime createdDate;
    private Long clickCount;
}
