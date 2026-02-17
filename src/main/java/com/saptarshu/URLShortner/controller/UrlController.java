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
    public ResponseEntity<UrlResponse> shortUrl(@RequestBody @Valid UrlCreateRequest request) {
        UrlResponse response = urlService.buildShortUrl(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlService.getOriginalUrl(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND) // 302 Redirect
                .location(URI.create(originalUrl))
                .build();
    }
}