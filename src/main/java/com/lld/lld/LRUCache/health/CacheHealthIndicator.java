package com.lld.lld.LRUCache.health;

import com.lld.lld.LRUCache.LRUCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Custom health indicator for the LRU Cache service
 */
@Component
@RequiredArgsConstructor
public class CacheHealthIndicator implements HealthIndicator {

    private final LRUCacheService<String, String> cacheService;

    @Override
    public Health health() {
        try {
            int size = cacheService.size();
            int capacity = cacheService.getCapacity();
            double usagePercentage = (double) size / capacity * 100;
            
            Health.Builder builder = Health.up()
                .withDetail("status", "UP")
                .withDetail("size", size)
                .withDetail("capacity", capacity)
                .withDetail("usagePercentage", String.format("%.2f%%", usagePercentage));
            
            // Warn if cache is more than 90% full
            if (usagePercentage > 90) {
                builder.withDetail("warning", "Cache is more than 90% full");
            }
            
            return builder.build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("status", "DOWN")
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}






