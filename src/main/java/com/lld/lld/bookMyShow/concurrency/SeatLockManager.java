package com.lld.lld.bookMyShow.concurrency;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
/**
 * Manages seat locking to prevent concurrent booking of the same seat
 * Uses in-memory locks for demonstration. In production, use database-level locking
 */
public class SeatLockManager {
    private final Map<String, ReentrantLock> seatLocks = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lockTimestamps = new ConcurrentHashMap<>();
    private final Map<String, String> lockOwners = new ConcurrentHashMap<>();
    
    // Lock timeout in minutes
    private static final int LOCK_TIMEOUT_MINUTES = 10;
    
    /**
     * Attempts to lock a seat for a specific user
     * @param seatId The seat to lock
     * @param userId The user requesting the lock
     * @return true if lock acquired, false if seat is already locked
     */
    public boolean lockSeat(String seatId, String userId) {
        ReentrantLock lock = seatLocks.computeIfAbsent(seatId, k -> new ReentrantLock());
        
        if (lock.tryLock()) {
            try {
                // Check if seat is already locked by someone else
                if (lockOwners.containsKey(seatId) && !lockOwners.get(seatId).equals(userId)) {
                    return false;
                }
                
                // Check if lock has expired
                if (isLockExpired(seatId)) {
                    releaseLock(seatId);
                }
                
                // Set new lock
                lockOwners.put(seatId, userId);
                lockTimestamps.put(seatId, LocalDateTime.now());
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
    
    /**
     * Releases a seat lock
     */
    public void releaseLock(String seatId) {
        ReentrantLock lock = seatLocks.get(seatId);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lockOwners.remove(seatId);
            lockTimestamps.remove(seatId);
            lock.unlock();
        }
    }
    
    /**
     * Checks if a lock has expired
     */
    private boolean isLockExpired(String seatId) {
        LocalDateTime lockTime = lockTimestamps.get(seatId);
        if (lockTime == null) return false;
        
        return LocalDateTime.now().isAfter(lockTime.plusMinutes(LOCK_TIMEOUT_MINUTES));
    }
    
    /**
     * Validates if a user owns the lock for a seat
     */
    public boolean validateLock(String seatId, String userId) {
        return lockOwners.get(seatId) != null && lockOwners.get(seatId).equals(userId);
    }
    
    /**
     * Cleans up expired locks
     */
    public void cleanupExpiredLocks() {
        lockTimestamps.entrySet().removeIf(entry -> 
            LocalDateTime.now().isAfter(entry.getValue().plusMinutes(LOCK_TIMEOUT_MINUTES))
        );
    }
}
