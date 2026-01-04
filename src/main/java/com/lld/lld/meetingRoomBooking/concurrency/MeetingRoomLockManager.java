package com.lld.lld.meetingRoomBooking.concurrency;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MeetingRoomLockManager {
    
    private final Map<String, ReentrantLock> roomLocks = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lockTimeStamps = new ConcurrentHashMap<>();
    private final Map<String, String> lockOwners = new ConcurrentHashMap<>();

    private static final int LOCK_TIMEOUT_MINUTES = 10;
    private static final int LOCK_ACQUIRE_TIMEOUT_SECONDS = 5;

    /**
     * Acquires a lock for a room. The lock must be released by calling releaseLock().
     * Returns true if lock was acquired, false otherwise.
     */
    public boolean lockRoom(String roomId, String employeeId) {
        ReentrantLock lock = roomLocks.computeIfAbsent(roomId, k -> new ReentrantLock());

        try {
            // Try to acquire the lock with a timeout to avoid indefinite waiting
            if (lock.tryLock(LOCK_ACQUIRE_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                // Check if there's an existing lock owner
                String currentOwner = lockOwners.get(roomId);
                if (currentOwner != null && !currentOwner.equals(employeeId)) {
                    // Another employee owns this lock and it hasn't expired
                    if (!isLockExpired(roomId)) {
                        lock.unlock();
                        return false;
                    }
                    // Lock is expired, we can take over
                }

                // Successfully acquired the lock
                lockOwners.put(roomId, employeeId);
                lockTimeStamps.put(roomId, LocalDateTime.now());
                return true;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return false;
    }

    public boolean isLockExpired(String roomId) {
        LocalDateTime lockTime = lockTimeStamps.get(roomId);
        if (lockTime == null) {
            return true; // No timestamp means no valid lock
        }
        return LocalDateTime.now().isAfter(lockTime.plusMinutes(LOCK_TIMEOUT_MINUTES));
    }

    /**
     * Releases the lock for a room. Must be called after lockRoom() returns true.
     */
    public void releaseLock(String roomId) {
        ReentrantLock lock = roomLocks.get(roomId);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lockOwners.remove(roomId);
            lockTimeStamps.remove(roomId);
            lock.unlock();
        }
    }

    /**
     * Checks if the current thread holds the lock for the specified room.
     */
    public boolean isLockedByCurrentThread(String roomId) {
        ReentrantLock lock = roomLocks.get(roomId);
        return lock != null && lock.isHeldByCurrentThread();
    }
}
