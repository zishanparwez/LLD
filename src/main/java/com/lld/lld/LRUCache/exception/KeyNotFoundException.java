package com.lld.lld.LRUCache.exception;

/**
 * Exception thrown when a requested key is not found in the cache
 */
public class KeyNotFoundException extends CacheException {
    public KeyNotFoundException(String key) {
        super("Key not found in cache: " + key);
    }
}






