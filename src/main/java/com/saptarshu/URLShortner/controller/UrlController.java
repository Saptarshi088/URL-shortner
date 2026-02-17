package com.saptarshu.URLShortner.controller;

import com.saptarshu.URLShortner.dto.UrlCreateRequest;
import com.saptarshu.URLShortner.dto.UrlResponse;
import com.saptarshu.URLShortner.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping()
    public UrlResponse shortUrl(@RequestBody @Valid UrlCreateRequest request) {
        return urlService.buildShortUrl(request);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        String originalUrl = urlService.getOriginalUrl(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND) // 302
                .location(URI.create(originalUrl))
                .build();
    }

}
