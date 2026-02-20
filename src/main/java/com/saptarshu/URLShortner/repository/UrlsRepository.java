package com.saptarshu.URLShortner.repository;

import com.saptarshu.URLShortner.entity.Urls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.lang.model.element.UnknownElementException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlsRepository extends JpaRepository<Urls, Long> {

    Optional<Urls> findByShortUrl(String shortUrl);

    boolean existsByShortUrl(String shortUrl);


    Iterable<? extends Urls> findByCreatedDateBefore(LocalDateTime localDateTime);
}