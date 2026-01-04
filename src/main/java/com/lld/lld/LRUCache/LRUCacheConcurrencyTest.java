package com.lld.lld.LRUCache;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Comprehensive concurrency test for LRU Cache
 * Tests thread safety with multiple concurrent operations
 */
@Slf4j
public class LRUCacheConcurrencyTest {

    private static final int CACHE_CAPACITY = 50;
    private static final int NUM_THREADS = 20;
    private static final int OPERATIONS_PER_THREAD = 1000;
    private static final int KEY_RANGE = 100; // Keys from 0 to 99

    public static void main(String[] args) {
        log.info("=== LRU Cache Concurrency Test ===\n");
        
        // Test 1: Concurrent Reads
        testConcurrentReads();
        
        // Test 2: Concurrent Writes
        testConcurrentWrites();
        
        // Test 3: Mixed Read/Write Operations
        testMixedOperations();
        
        // Test 4: Stress Test with Evictions
        testStressWithEvictions();
        
        // Test 5: Verify Data Integrity
        testDataIntegrity();
        
        log.info("\n=== All Concurrency Tests Completed ===");
    }

    /**
     * Test 1: Multiple threads performing concurrent reads
     */
    private static void testConcurrentReads() {
        log.info("Test 1: Concurrent Reads");
        log.info("----------------------------------------");
        
        LRUCacheService<String, String> cache = new LRUCacheService<>(CACHE_CAPACITY);
        
        // Pre-populate cache
        for (int i = 0; i < CACHE_CAPACITY; i++) {
            cache.put("key" + i, "value" + i);
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger nullCount = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        int keyIndex = (threadId * OPERATIONS_PER_THREAD + j) % CACHE_CAPACITY;
                        String key = "key" + keyIndex;
                        String value = cache.get(key);
                        
                        if (value != null && value.equals("value" + keyIndex)) {
                            successCount.incrementAndGet();
                        } else {
                            nullCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await(30, TimeUnit.SECONDS);
            long duration = System.currentTimeMillis() - startTime;
            
            log.info("Threads: {}, Operations per thread: {}", NUM_THREADS, OPERATIONS_PER_THREAD);
            log.info("Total operations: {}", NUM_THREADS * OPERATIONS_PER_THREAD);
            log.info("Successful reads: {}", successCount.get());
            log.info("Null reads: {}", nullCount.get());
            log.info("Duration: {} ms", duration);
            log.info("Throughput: {:.2f} ops/sec\n", 
                (NUM_THREADS * OPERATIONS_PER_THREAD * 1000.0) / duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Test interrupted", e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Test 2: Multiple threads performing concurrent writes
     */
    private static void testConcurrentWrites() {
        log.info("Test 2: Concurrent Writes");
        log.info("----------------------------------------");
        
        LRUCacheService<String, String> cache = new LRUCacheService<>(CACHE_CAPACITY);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger writeCount = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        String key = "key" + (threadId * OPERATIONS_PER_THREAD + j);
                        String value = "value" + (threadId * OPERATIONS_PER_THREAD + j);
                        cache.put(key, value);
                        writeCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await(30, TimeUnit.SECONDS);
            long duration = System.currentTimeMillis() - startTime;
            
            log.info("Threads: {}, Operations per thread: {}", NUM_THREADS, OPERATIONS_PER_THREAD);
            log.info("Total writes: {}", writeCount.get());
            log.info("Final cache size: {}", cache.size());
            log.info("Duration: {} ms", duration);
            log.info("Throughput: {:.2f} ops/sec\n", 
                (writeCount.get() * 1000.0) / duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Test interrupted", e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Test 3: Mixed read and write operations
     */
    private static void testMixedOperations() {
        log.info("Test 3: Mixed Read/Write Operations");
        log.info("----------------------------------------");
        
        LRUCacheService<String, String> cache = new LRUCacheService<>(CACHE_CAPACITY);
        
        // Pre-populate with some initial data
        for (int i = 0; i < 20; i++) {
            cache.put("key" + i, "value" + i);
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger readCount = new AtomicInteger(0);
        AtomicInteger writeCount = new AtomicInteger(0);
        AtomicInteger readSuccessCount = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    Random random = new Random(threadId);
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        int keyIndex = random.nextInt(KEY_RANGE);
                        String key = "key" + keyIndex;
                        
                        if (random.nextBoolean()) {
                            // Read operation
                            String value = cache.get(key);
                            if (value != null) {
                                readSuccessCount.incrementAndGet();
                            }
                            readCount.incrementAndGet();
                        } else {
                            // Write operation
                            cache.put(key, "value" + keyIndex + "_updated");
                            writeCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await(30, TimeUnit.SECONDS);
            long duration = System.currentTimeMillis() - startTime;
            
            log.info("Threads: {}, Operations per thread: {}", NUM_THREADS, OPERATIONS_PER_THREAD);
            log.info("Total reads: {}", readCount.get());
            log.info("Total writes: {}", writeCount.get());
            log.info("Successful reads: {}", readSuccessCount.get());
            log.info("Final cache size: {}", cache.size());
            log.info("Duration: {} ms", duration);
            log.info("Throughput: {:.2f} ops/sec\n", 
                ((readCount.get() + writeCount.get()) * 1000.0) / duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Test interrupted", e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Test 4: Stress test with evictions
     * Tests that evictions work correctly under concurrent load
     */
    private static void testStressWithEvictions() {
        log.info("Test 4: Stress Test with Evictions");
        log.info("----------------------------------------");
        
        LRUCacheService<String, String> cache = new LRUCacheService<>(CACHE_CAPACITY);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger operationCount = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    Random random = new Random(threadId);
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        int keyIndex = random.nextInt(KEY_RANGE * 2); // More keys than capacity
                        String key = "key" + keyIndex;
                        
                        if (random.nextDouble() < 0.7) {
                            // 70% reads
                            cache.get(key);
                        } else {
                            // 30% writes
                            cache.put(key, "value" + keyIndex);
                        }
                        operationCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await(30, TimeUnit.SECONDS);
            long duration = System.currentTimeMillis() - startTime;
            
            LRUCacheService.CacheStatistics stats = cache.getStatistics();
            
            log.info("Threads: {}, Operations per thread: {}", NUM_THREADS, OPERATIONS_PER_THREAD);
            log.info("Total operations: {}", operationCount.get());
            log.info("Final cache size: {} (capacity: {})", cache.size(), CACHE_CAPACITY);
            log.info("Evictions: {}", stats.getEvictionCount());
            log.info("Hits: {}, Misses: {}", stats.getHitCount(), stats.getMissCount());
            log.info("Hit rate: {:.2f}%", stats.getHitRate() * 100);
            log.info("Duration: {} ms", duration);
            log.info("Throughput: {:.2f} ops/sec\n", 
                (operationCount.get() * 1000.0) / duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Test interrupted", e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Test 5: Verify data integrity
     * Ensures no data corruption occurs under concurrent access
     */
    private static void testDataIntegrity() {
        log.info("Test 5: Data Integrity Verification");
        log.info("----------------------------------------");
        
        LRUCacheService<String, String> cache = new LRUCacheService<>(CACHE_CAPACITY);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        Map<String, String> expectedValues = new ConcurrentHashMap<>();
        AtomicInteger corruptionCount = new AtomicInteger(0);
        AtomicInteger totalChecks = new AtomicInteger(0);
        
        // Phase 1: Concurrent writes
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        String key = "key" + (threadId * OPERATIONS_PER_THREAD + j);
                        String value = "value" + (threadId * OPERATIONS_PER_THREAD + j);
                        cache.put(key, value);
                        expectedValues.put(key, value);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Test interrupted", e);
            return;
        }
        
        // Phase 2: Verify all written values
        CountDownLatch verifyLatch = new CountDownLatch(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        String key = "key" + (threadId * OPERATIONS_PER_THREAD + j);
                        String expectedValue = expectedValues.get(key);
                        
                        if (expectedValue != null) {
                            String actualValue = cache.get(key);
                            totalChecks.incrementAndGet();
                            
                            // Note: Due to evictions, some values might not be in cache
                            // But if they are, they should match
                            if (actualValue != null && !actualValue.equals(expectedValue)) {
                                corruptionCount.incrementAndGet();
                                log.error("Data corruption detected! Key: {}, Expected: {}, Actual: {}", 
                                    key, expectedValue, actualValue);
                            }
                        }
                    }
                } finally {
                    verifyLatch.countDown();
                }
            });
        }
        
        try {
            verifyLatch.await(30, TimeUnit.SECONDS);
            
            log.info("Total values written: {}", expectedValues.size());
            log.info("Total integrity checks: {}", totalChecks.get());
            log.info("Corruption count: {}", corruptionCount.get());
            
            if (corruptionCount.get() == 0) {
                log.info("✓ Data integrity verified: No corruption detected\n");
            } else {
                log.error("✗ Data integrity check failed: {} corruptions detected\n", corruptionCount.get());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Test interrupted", e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Helper method to run a specific test
     */
    public static void runSingleTest(String testName) {
        switch (testName.toLowerCase()) {
            case "reads":
                testConcurrentReads();
                break;
            case "writes":
                testConcurrentWrites();
                break;
            case "mixed":
                testMixedOperations();
                break;
            case "stress":
                testStressWithEvictions();
                break;
            case "integrity":
                testDataIntegrity();
                break;
            default:
                log.error("Unknown test: {}. Available tests: reads, writes, mixed, stress, integrity", testName);
        }
    }
}

