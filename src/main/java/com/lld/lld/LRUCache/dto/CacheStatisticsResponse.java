package com.lld.lld.LRUCache.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for cache statistics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheStatisticsResponse {
    private int currentSize;
    private int capacity;
    private long hitCount;
    private long missCount;
    private long evictionCount;
    private long putCount;
    private long getCount;
    private double hitRate;
}






