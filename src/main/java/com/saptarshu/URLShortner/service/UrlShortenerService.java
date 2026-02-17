package com.saptarshu.URLShortner.service;

import com.saptarshu.URLShortner.entity.Urls;
import com.saptarshu.URLShortner.repository.UrlsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 8;
    private final SecureRandom random = new SecureRandom();


    private final UrlsRepository repository;

    public String shortenUrl(String originalUrl) {
        String shortCode;
        

        do {
            shortCode = generateCode();
        } while (repository.existsByShortUrl(shortCode));

        Urls mapping = new Urls();
        mapping.setOriginalUrl(originalUrl);
        mapping.setShortUrl(shortCode);
        repository.save(mapping);

        return shortCode;
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}