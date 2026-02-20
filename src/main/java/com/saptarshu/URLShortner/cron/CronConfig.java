package com.saptarshu.URLShortner.cron;

import com.saptarshu.URLShortner.repository.UrlsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CronConfig {

    private final UrlsRepository urlsRepository;

    @Scheduled(cron = "0   0   0   *   *   ?", zone = "Asia/Kolkata")
    public void removeUrlsBefore48Hours() {
        urlsRepository.deleteAll(
                urlsRepository.findByCreatedDateBefore(
                        LocalDateTime.now().minusHours(48)
                )
        );
    }
}
