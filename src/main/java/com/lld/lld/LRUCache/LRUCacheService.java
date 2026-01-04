package com.lld.lld.LRUCache;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe LRU Cache implementation
 * Uses ReadWriteLock for better concurrency (multiple readers, single writer)
 * 
 * Time Complexity:
 * - get(): O(1) average case
 * - put(): O(1) average case
 * 
 * Space Complexity: O(capacity)
 */
@Slf4j
@Service
@Getter
public class LRUCacheService<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final ReadWriteLock lock;
    private Node<K, V> head;
    private Node<K, V> tail;
    
    // Metrics
    private long hitCount = 0;
    private long missCount = 0;
    private long evictionCount = 0;
    private long putCount = 0;
    private long getCount = 0;

    public LRUCacheService() {
        this(100); // Default capacity
    }

    public LRUCacheService(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
        this.lock = new ReentrantReadWriteLock();
        this.head = null;
        this.tail = null;
        log.info("LRU Cache initialized with capacity: {}", capacity);
    }

    /**
     * Retrieves a value from the cache
     * Moves the accessed node to the head (most recently used)
     * 
     * @param key The key to retrieve
     * @return The value associated with the key, or null if not found
     */
    public V get(K key) {
        lock.writeLock().lock();
        try {
            getCount++;
            if (!map.containsKey(key)) {
                missCount++;
                log.debug("Cache miss for key: {}", key);
                return null;
            }

            Node<K, V> node = map.get(key);
            moveToHead(node);
            hitCount++;
            log.debug("Cache hit for key: {}", key);
            return node.getValue();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Inserts or updates a key-value pair in the cache
     * If the cache is full, evicts the least recently used item
     * 
     * @param key The key to insert/update
     * @param value The value to associate with the key
     * @return The previous value if key existed, null otherwise
     */
    public V put(K key, V value) {
        lock.writeLock().lock();
        try {
            putCount++;
            V previousValue = null;

            if (map.containsKey(key)) {
                // Update existing node
                Node<K, V> node = map.get(key);
                previousValue = node.getValue();
                node.setValue(value);
                moveToHead(node);
                log.debug("Updated key: {} in cache", key);
            } else {
                // Add new node
                if (map.size() >= capacity) {
                    // Evict least recently used (tail)
                    evictLRU();
                    evictionCount++;
                }
                Node<K, V> newNode = new Node<>(key, value);
                addToHead(newNode);
                map.put(key, newNode);
                log.debug("Added key: {} to cache", key);
            }
            return previousValue;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Removes a key-value pair from the cache
     * 
     * @param key The key to remove
     * @return The value that was removed, or null if key didn't exist
     */
    public V remove(K key) {
        lock.writeLock().lock();
        try {
            if (!map.containsKey(key)) {
                log.debug("Key not found for removal: {}", key);
                return null;
            }

            Node<K, V> node = map.remove(key);
            removeNode(node);
            log.debug("Removed key: {} from cache", key);
            return node.getValue();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Checks if a key exists in the cache
     * 
     * @param key The key to check
     * @return true if key exists, false otherwise
     */
    public boolean containsKey(K key) {
        lock.readLock().lock();
        try {
            return map.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Returns the current size of the cache
     * 
     * @return The number of items in the cache
     */
    public int size() {
        lock.readLock().lock();
        try {
            return map.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Clears all entries from the cache
     */
    public void clear() {
        lock.writeLock().lock();
        try {
            map.clear();
            head = null;
            tail = null;
            log.info("Cache cleared");
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Returns all key-value pairs in the cache
     * Note: This creates a snapshot, so it may not reflect concurrent modifications
     * 
     * @return A map containing all key-value pairs
     */
    public Map<K, V> getAll() {
        lock.readLock().lock();
        try {
            Map<K, V> result = new HashMap<>();
            Node<K, V> current = head;
            while (current != null) {
                result.put(current.getKey(), current.getValue());
                current = current.getNext();
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets cache statistics
     * 
     * @return CacheStatistics object with metrics
     */
    public CacheStatistics getStatistics() {
        lock.readLock().lock();
        try {
            double hitRate = (getCount > 0) ? (double) hitCount / getCount : 0.0;
            return new CacheStatistics(
                size(),
                capacity,
                hitCount,
                missCount,
                evictionCount,
                putCount,
                getCount,
                hitRate
            );
        } finally {
            lock.readLock().unlock();
        }
    }

    // ========== Private Helper Methods ==========

    /**
     * Moves a node to the head of the doubly linked list
     */
    private void moveToHead(Node<K, V> node) {
        if (node == head) {
            return; // Already at head
        }
        removeNode(node);
        addToHead(node);
    }

    /**
     * Adds a node to the head of the doubly linked list
     */
    private void addToHead(Node<K, V> node) {
        node.setNext(head);
        node.setPrev(null);

        if (head != null) {
            head.setPrev(node);
        }

        head = node;

        if (tail == null) {
            tail = head;
        }
    }

    /**
     * Removes a node from the doubly linked list
     */
    private void removeNode(Node<K, V> node) {
        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            head = node.getNext();
        }

        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            tail = node.getPrev();
        }
    }

    /**
     * Evicts the least recently used item (tail of the list)
     */
    private void evictLRU() {
        if (tail != null) {
            map.remove(tail.getKey());
            removeNode(tail);
            log.debug("Evicted LRU item with key: {}", tail.getKey());
        }
    }

    /**
     * Inner class to hold cache statistics
     */
    @Getter
    public static class CacheStatistics {
        private final int currentSize;
        private final int capacity;
        private final long hitCount;
        private final long missCount;
        private final long evictionCount;
        private final long putCount;
        private final long getCount;
        private final double hitRate;

        public CacheStatistics(int currentSize, int capacity, long hitCount, long missCount,
                              long evictionCount, long putCount, long getCount, double hitRate) {
            this.currentSize = currentSize;
            this.capacity = capacity;
            this.hitCount = hitCount;
            this.missCount = missCount;
            this.evictionCount = evictionCount;
            this.putCount = putCount;
            this.getCount = getCount;
            this.hitRate = hitRate;
        }
    }
}
