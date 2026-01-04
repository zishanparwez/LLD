package com.lld.lld.LRUCache.metrics;

import com.lld.lld.LRUCache.LRUCacheService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Metrics collector for LRU Cache
 * Exposes cache metrics to Micrometer for monitoring
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheMetrics {

    private final LRUCacheService<String, String> cacheService;
    private final MeterRegistry meterRegistry;

    @PostConstruct
    public void registerMetrics() {
        // Register cache size gauge
        Gauge.builder("lru.cache.size", cacheService, LRUCacheService::size)
            .description("Current number of items in the cache")
            .register(meterRegistry);

        // Register cache capacity gauge
        Gauge.builder("lru.cache.capacity", cacheService, LRUCacheService::getCapacity)
            .description("Maximum capacity of the cache")
            .register(meterRegistry);

        // Register cache usage percentage
        Gauge.builder("lru.cache.usage.percentage", cacheService, 
                cache -> (double) cache.size() / cache.getCapacity() * 100)
            .description("Cache usage percentage")
            .register(meterRegistry);

        // Register hit count
        Gauge.builder("lru.cache.hits", cacheService, 
                cache -> cache.getStatistics().getHitCount())
            .description("Total number of cache hits")
            .register(meterRegistry);

        // Register miss count
        Gauge.builder("lru.cache.misses", cacheService, 
                cache -> cache.getStatistics().getMissCount())
            .description("Total number of cache misses")
            .register(meterRegistry);

        // Register eviction count
        Gauge.builder("lru.cache.evictions", cacheService, 
                cache -> cache.getStatistics().getEvictionCount())
            .description("Total number of cache evictions")
            .register(meterRegistry);

        // Register hit rate
        Gauge.builder("lru.cache.hit.rate", cacheService, 
                cache -> cache.getStatistics().getHitRate())
            .description("Cache hit rate (0.0 to 1.0)")
            .register(meterRegistry);

        log.info("Cache metrics registered successfully");
    }
}






