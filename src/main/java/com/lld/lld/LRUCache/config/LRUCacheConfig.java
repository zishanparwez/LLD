package com.lld.lld.LRUCache.config;

import com.lld.lld.LRUCache.LRUCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for LRU Cache
 */
@Slf4j
@Configuration
public class LRUCacheConfig {

    @Value("${lru.cache.capacity:100}")
    private int cacheCapacity;

    @Bean
    public LRUCacheService<String, String> lruCacheService() {
        log.info("Creating LRU Cache Service with capacity: {}", cacheCapacity);
        return new LRUCacheService<>(cacheCapacity);
    }
}






