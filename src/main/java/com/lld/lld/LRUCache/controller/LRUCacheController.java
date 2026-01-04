package com.lld.lld.LRUCache.controller;

import com.lld.lld.LRUCache.LRUCacheService;
import com.lld.lld.LRUCache.dto.CacheRequest;
import com.lld.lld.LRUCache.dto.CacheResponse;
import com.lld.lld.LRUCache.dto.CacheStatisticsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for LRU Cache microservice
 * Provides CRUD operations and statistics endpoints
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/cache")
@RequiredArgsConstructor
public class LRUCacheController {

    private final LRUCacheService<String, String> cacheService;

    /**
     * Get a value from the cache
     * GET /api/v1/cache/{key}
     */
    @GetMapping("/{key}")
    public ResponseEntity<CacheResponse> get(@PathVariable String key) {
        log.info("GET request for key: {}", key);
        String value = cacheService.get(key);
        
        if (value == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CacheResponse.builder()
                    .key(key)
                    .success(false)
                    .message("Key not found in cache")
                    .build());
        }
        
        return ResponseEntity.ok(CacheResponse.builder()
            .key(key)
            .value(value)
            .success(true)
            .message("Value retrieved successfully")
            .build());
    }

    /**
     * Put a key-value pair into the cache
     * POST /api/v1/cache
     */
    @PostMapping
    public ResponseEntity<CacheResponse> put(@RequestBody CacheRequest request) {
        log.info("PUT request for key: {}", request.getKey());
        
        if (request.getKey() == null || request.getKey().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(CacheResponse.builder()
                    .success(false)
                    .message("Key cannot be null or empty")
                    .build());
        }
        
        String previousValue = cacheService.put(request.getKey(), request.getValue());
        boolean wasUpdate = previousValue != null;
        
        return ResponseEntity.ok(CacheResponse.builder()
            .key(request.getKey())
            .value(request.getValue())
            .success(true)
            .message(wasUpdate ? "Value updated successfully" : "Value added successfully")
            .build());
    }

    /**
     * Update a value in the cache (only if key exists)
     * PUT /api/v1/cache/{key}
     */
    @PutMapping("/{key}")
    public ResponseEntity<CacheResponse> update(
            @PathVariable String key,
            @RequestBody Map<String, String> request) {
        log.info("UPDATE request for key: {}", key);
        
        if (!cacheService.containsKey(key)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CacheResponse.builder()
                    .key(key)
                    .success(false)
                    .message("Key not found in cache")
                    .build());
        }
        
        String value = request.get("value");
        if (value == null) {
            return ResponseEntity.badRequest()
                .body(CacheResponse.builder()
                    .key(key)
                    .success(false)
                    .message("Value cannot be null")
                    .build());
        }
        
        cacheService.put(key, value);
        
        return ResponseEntity.ok(CacheResponse.builder()
            .key(key)
            .value(value)
            .success(true)
            .message("Value updated successfully")
            .build());
    }

    /**
     * Delete a key from the cache
     * DELETE /api/v1/cache/{key}
     */
    @DeleteMapping("/{key}")
    public ResponseEntity<CacheResponse> delete(@PathVariable String key) {
        log.info("DELETE request for key: {}", key);
        String value = cacheService.remove(key);
        
        if (value == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CacheResponse.builder()
                    .key(key)
                    .success(false)
                    .message("Key not found in cache")
                    .build());
        }
        
        return ResponseEntity.ok(CacheResponse.builder()
            .key(key)
            .value(value)
            .success(true)
            .message("Key deleted successfully")
            .build());
    }

    /**
     * Check if a key exists in the cache
     * HEAD /api/v1/cache/{key}
     */
    @RequestMapping(value = "/{key}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> containsKey(@PathVariable String key) {
        log.info("HEAD request for key: {}", key);
        boolean exists = cacheService.containsKey(key);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Get all key-value pairs in the cache
     * GET /api/v1/cache
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> getAll() {
        log.info("GET all request");
        Map<String, String> allEntries = cacheService.getAll();
        return ResponseEntity.ok(allEntries);
    }

    /**
     * Clear all entries from the cache
     * DELETE /api/v1/cache
     */
    @DeleteMapping
    public ResponseEntity<CacheResponse> clear() {
        log.info("CLEAR request");
        cacheService.clear();
        return ResponseEntity.ok(CacheResponse.builder()
            .success(true)
            .message("Cache cleared successfully")
            .build());
    }

    /**
     * Get cache statistics
     * GET /api/v1/cache/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<CacheStatisticsResponse> getStatistics() {
        log.info("STATS request");
        LRUCacheService.CacheStatistics stats = cacheService.getStatistics();
        
        CacheStatisticsResponse response = CacheStatisticsResponse.builder()
            .currentSize(stats.getCurrentSize())
            .capacity(stats.getCapacity())
            .hitCount(stats.getHitCount())
            .missCount(stats.getMissCount())
            .evictionCount(stats.getEvictionCount())
            .putCount(stats.getPutCount())
            .getCount(stats.getGetCount())
            .hitRate(stats.getHitRate())
            .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get cache size and capacity
     * GET /api/v1/cache/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        log.info("INFO request");
        Map<String, Object> info = new HashMap<>();
        info.put("size", cacheService.size());
        info.put("capacity", cacheService.getCapacity());
        info.put("usage", String.format("%.2f%%", (double) cacheService.size() / cacheService.getCapacity() * 100));
        return ResponseEntity.ok(info);
    }
}

