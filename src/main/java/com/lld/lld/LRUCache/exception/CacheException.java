package com.lld.lld.LRUCache.exception;

/**
 * Base exception for cache-related errors
 */
public class CacheException extends RuntimeException {
    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}






