package com.saptarshu.URLShortner.service;

import com.saptarshu.URLShortner.dto.UrlCreateRequest;
import com.saptarshu.URLShortner.dto.UrlResponse;
import com.saptarshu.URLShortner.entity.Urls;
import com.saptarshu.URLShortner.repository.UrlsRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlsRepository  urlsRepository;
    private final Base62Encoder base62Encoder;

    @Transactional
    public UrlResponse buildShortUrl(UrlCreateRequest request) {

        // Step 1: Create entity without short code
        Urls url = Urls.builder()
                .originalUrl(request.getUrl())
                .clickCount(0L)
                .build();

        // Save to generate ID
        url = urlsRepository.save(url);

        // Step 2: Generate short code from ID
        String shortCode = Base62Encoder.encode(url.getId());

        // Step 3: Update entity
        url.setShortUrl(shortCode);

        // No need to call save() again because of @Transactional
        // JPA dirty checking will update it automatically

        // Step 4: Build response
        return UrlResponse.builder()
                .originalUrl(url.getOriginalUrl())
                .shortUrl("http://localhost:8080/" + shortCode)
                .createdDate(url.getCreatedDate().toLocalDate().atStartOfDay())
                .clickCount(url.getClickCount())
                .build();
    }

    @Transactional
    public String getOriginalUrl(String shortCode) {

        Urls url = urlsRepository.findByShortUrl(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

        // increment click count
        url.setClickCount(url.getClickCount() + 1);

        return url.getOriginalUrl();
    }

}
