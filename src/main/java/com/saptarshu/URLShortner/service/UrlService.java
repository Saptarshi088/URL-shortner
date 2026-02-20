package com.saptarshu.URLShortner.service;

import com.saptarshu.URLShortner.dto.UrlCreateRequest;
import com.saptarshu.URLShortner.dto.UrlResponse;
import com.saptarshu.URLShortner.entity.Urls;
import com.saptarshu.URLShortner.repository.UrlsRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlsRepository urlsRepository;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 8;
    private final SecureRandom random = new SecureRandom();

    @Transactional
    public UrlResponse buildShortUrl(UrlCreateRequest request, HttpServletRequest httpServletRequest) {
        String shortCode;

        do {
            shortCode = generateCode();
        } while (urlsRepository.existsByShortUrl(shortCode));

        Urls url = Urls.builder()
                .originalUrl(request.getUrl())
                .shortUrl(shortCode)
                .clickCount(0L)
                .createdDate(LocalDateTime.now())
                .build();

        Urls savedUrl = urlsRepository.save(url);

        String baseUrl = ServletUriComponentsBuilder
                .fromRequestUri(httpServletRequest)
                .replacePath(null)
                .build()
                .toUriString();


        return UrlResponse.builder()
                .originalUrl(savedUrl.getOriginalUrl())
                .shortUrl(baseUrl + "/v1/api/" + shortCode)
                .createdDate(savedUrl.getCreatedDate().toLocalDate().atStartOfDay())
                .clickCount(savedUrl.getClickCount())
                .build();
    }

    @Transactional
    public String getOriginalUrl(String shortCode) {
        Urls url = urlsRepository.findByShortUrl(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

        url.setClickCount(url.getClickCount() + 1);

        return url.getOriginalUrl();
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}