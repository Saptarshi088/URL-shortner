package com.saptarshu.URLShortner.graphql;

import com.saptarshu.URLShortner.dto.UrlCreateRequest;
import com.saptarshu.URLShortner.dto.UrlResponse;
import com.saptarshu.URLShortner.repository.UrlsRepository;
import com.saptarshu.URLShortner.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GraphQLController {

    private final UrlsRepository  urlsRepository;
    private final UrlService urlService;

    @QueryMapping
    public String getOriginalUrl(@Argument String shortCode) {
       var urlEntity =  urlsRepository.findByShortUrl(shortCode).orElseThrow(()-> new RuntimeException("Url not found"));
       return urlEntity.getOriginalUrl();
    }

    @MutationMapping
    public UrlResponse generateShortUrl(
            @Argument UrlCreateRequest input,
            HttpServletRequest request) {

        return urlService.buildShortUrl(input, request);
    }
}
