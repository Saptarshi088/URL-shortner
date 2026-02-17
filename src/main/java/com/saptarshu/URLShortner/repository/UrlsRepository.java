package com.saptarshu.URLShortner.repository;

import com.saptarshu.URLShortner.entity.Urls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlsRepository extends JpaRepository<Urls, Long> {

    Optional<Urls> findByShortUrl(String shortUrl);

}