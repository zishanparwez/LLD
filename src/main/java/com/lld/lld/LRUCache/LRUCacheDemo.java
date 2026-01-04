package com.lld.lld.LRUCache;

import lombok.extern.slf4j.Slf4j;

/**
 * Demo class to demonstrate LRU Cache functionality
 * This can be used for testing the cache service directly
 */
@Slf4j
public class LRUCacheDemo {

    public static void main(String[] args) {
        // Create a cache with capacity of 3
        LRUCacheService<String, String> cache = new LRUCacheService<>(3);

        log.info("=== LRU Cache Demo ===\n");

        // Add some entries
        log.info("Adding entries...");
        cache.put("1", "One");
        cache.put("2", "Two");
        cache.put("3", "Three");
        log.info("Cache size: {}\n", cache.size());

        // Access "1" to make it most recently used
        log.info("Accessing key '1': {}", cache.get("1"));

        // Add a new entry - should evict "2" (least recently used)
        log.info("\nAdding new entry '4' (should evict '2')...");
        cache.put("4", "Four");

        // Check what's in cache
        log.info("\nCurrent cache contents:");
        cache.getAll().forEach((k, v) -> log.info("  {} -> {}", k, v));

        // Verify "2" was evicted
        log.info("\nChecking if '2' exists: {}", cache.containsKey("2"));
        log.info("Checking if '1' exists: {}", cache.containsKey("1"));

        // Get statistics
        LRUCacheService.CacheStatistics stats = cache.getStatistics();
        log.info("\n=== Cache Statistics ===");
        log.info("Size: {}/{}", stats.getCurrentSize(), stats.getCapacity());
        log.info("Hits: {}", stats.getHitCount());
        log.info("Misses: {}", stats.getMissCount());
        log.info("Evictions: {}", stats.getEvictionCount());
        log.info("Hit Rate: {:.2f}%", stats.getHitRate() * 100);
    }
}






