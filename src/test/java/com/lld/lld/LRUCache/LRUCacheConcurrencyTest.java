package com.lld.lld.LRUCache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LRU Cache concurrency
 */
class LRUCacheConcurrencyTest {

    private LRUCacheService<String, String> cache;
    private static final int CACHE_CAPACITY = 10;
    private static final int NUM_THREADS = 10;
    private static final int OPERATIONS_PER_THREAD = 100;

    @BeforeEach
    void setUp() {
        cache = new LRUCacheService<>(CACHE_CAPACITY);
    }

    @Test
    @Timeout(10)
    void testConcurrentReads() throws InterruptedException {
        // Pre-populate cache
        IntStream.range(0, CACHE_CAPACITY).forEach(i -> 
            cache.put("key" + i, "value" + i)
        );

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        int keyIndex = j % CACHE_CAPACITY;
                        String key = "key" + keyIndex;
                        String value = cache.get(key);
                        
                        if (value != null && value.equals("value" + keyIndex)) {
                            successCount.incrementAndGet();
                        } else {
                            failureCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals(0, failureCount.get(), "All reads should succeed");
        assertEquals(NUM_THREADS * OPERATIONS_PER_THREAD, successCount.get());
    }

    @Test
    @Timeout(10)
    void testConcurrentWrites() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger writeCount = new AtomicInteger(0);

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

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals(NUM_THREADS * OPERATIONS_PER_THREAD, writeCount.get());
        assertTrue(cache.size() <= CACHE_CAPACITY, "Cache size should not exceed capacity");
    }

    @Test
    @Timeout(10)
    void testConcurrentReadWrite() throws InterruptedException {
        // Pre-populate with some data
        IntStream.range(0, 5).forEach(i -> 
            cache.put("key" + i, "value" + i)
        );

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger readCount = new AtomicInteger(0);
        AtomicInteger writeCount = new AtomicInteger(0);

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    Random random = new Random(threadId);
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        int keyIndex = random.nextInt(20);
                        String key = "key" + keyIndex;
                        
                        if (random.nextBoolean()) {
                            cache.get(key);
                            readCount.incrementAndGet();
                        } else {
                            cache.put(key, "value" + keyIndex);
                            writeCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertTrue(readCount.get() > 0, "Should have some reads");
        assertTrue(writeCount.get() > 0, "Should have some writes");
        assertTrue(cache.size() <= CACHE_CAPACITY, "Cache size should not exceed capacity");
    }

    @Test
    @Timeout(10)
    void testConcurrentEvictions() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);

        // Write more keys than capacity to trigger evictions
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        String key = "key" + (threadId * OPERATIONS_PER_THREAD + j);
                        cache.put(key, "value" + j);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertTrue(cache.size() <= CACHE_CAPACITY, "Cache size should not exceed capacity");
        
        LRUCacheService.CacheStatistics stats = cache.getStatistics();
        assertTrue(stats.getEvictionCount() > 0, "Should have some evictions");
    }

    @Test
    @Timeout(10)
    void testDataIntegrity() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        Map<String, String> writtenValues = new ConcurrentHashMap<>();
        AtomicInteger corruptionCount = new AtomicInteger(0);

        // Write phase
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        String key = "key" + (threadId * OPERATIONS_PER_THREAD + j);
                        String value = "value" + (threadId * OPERATIONS_PER_THREAD + j);
                        cache.put(key, value);
                        writtenValues.put(key, value);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);

        // Verify phase - check that values that are in cache match what we wrote
        CountDownLatch verifyLatch = new CountDownLatch(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        String key = "key" + (threadId * OPERATIONS_PER_THREAD + j);
                        String expectedValue = writtenValues.get(key);
                        String actualValue = cache.get(key);
                        
                        // If value is in cache, it should match what we wrote
                        if (actualValue != null && expectedValue != null) {
                            if (!actualValue.equals(expectedValue)) {
                                corruptionCount.incrementAndGet();
                            }
                        }
                    }
                } finally {
                    verifyLatch.countDown();
                }
            });
        }

        verifyLatch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals(0, corruptionCount.get(), "No data corruption should occur");
    }

    @Test
    @Timeout(10)
    void testConcurrentRemove() throws InterruptedException {
        // Pre-populate cache
        IntStream.range(0, CACHE_CAPACITY).forEach(i -> 
            cache.put("key" + i, "value" + i)
        );

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger removeCount = new AtomicInteger(0);

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        int keyIndex = (threadId * OPERATIONS_PER_THREAD + j) % CACHE_CAPACITY;
                        String key = "key" + keyIndex;
                        String removed = cache.remove(key);
                        if (removed != null) {
                            removeCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertTrue(removeCount.get() > 0, "Should have some successful removals");
    }

    @Test
    @Timeout(10)
    void testCacheSizeConsistency() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        List<Integer> sizes = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        String key = "key" + (threadId * OPERATIONS_PER_THREAD + j);
                        cache.put(key, "value" + j);
                        sizes.add(cache.size());
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        // All reported sizes should be within valid range
        assertTrue(sizes.stream().allMatch(size -> size >= 0 && size <= CACHE_CAPACITY),
            "All cache sizes should be valid");
        
        // Final size should be valid
        assertTrue(cache.size() >= 0 && cache.size() <= CACHE_CAPACITY,
            "Final cache size should be valid");
    }
}






