package com.backend.allreva.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    @Primary
    public CacheManager concertHallMainCacheManager() {
        ConcurrentMapCacheManager cacheManager =
                new ConcurrentMapCacheManager("concertHallMainCacheManager");
        cacheManager.setCacheNames(List.of("concertHallMain"));
        return cacheManager;
    }

    @Bean
    public CacheManager relatedConcertCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        // relatedConcert 캐시 설정
        CaffeineCache relatedConcertCache = new CaffeineCache(
                "relatedConcert",
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofSeconds(calculateSecondsUntilMidnight())) // 자정까지 TTL 설정
                        .maximumSize(100) // 최대 캐시 크기
                        .build()
        );

        cacheManager.setCaches(List.of(relatedConcertCache));
        return cacheManager;
    }
    private long calculateSecondsUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay(); // 다음 날 00:00
        return ChronoUnit.SECONDS.between(now, midnight);
    }
}
