package com.lld.lld.LRUCache.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for cache operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheRequest {
    private String key;
    private String value;
}






