package com.saptarshu.URLShortner.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Urls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String originalUrl;

    @Column(name = "short_code", nullable = false, unique = true)
    private String shortUrl;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private Long clickCount = 0L;
}
