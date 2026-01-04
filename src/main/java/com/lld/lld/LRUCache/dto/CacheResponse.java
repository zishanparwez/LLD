package com.lld.lld.LRUCache.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for cache operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheResponse {
    private String key;
    private String value;
    private String message;
    private boolean success;
}






