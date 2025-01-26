package com.backend.allreva.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager concertHallMainCacheManager() {
        ConcurrentMapCacheManager cacheManager =
                new ConcurrentMapCacheManager("concertHallMainCacheManager");
        cacheManager.setCacheNames(List.of("concertHallMain"));
        return cacheManager;
    }
}
