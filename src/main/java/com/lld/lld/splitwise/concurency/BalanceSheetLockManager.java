package com.lld.lld.splitwise.concurency;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.lld.lld.splitwise.models.Group;
import com.lld.lld.splitwise.models.User;

public class BalanceSheetLockManager {
    private final Map<String, ReentrantLock> balanceSheetLocks = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lockTimeStamps = new ConcurrentHashMap<>();
    private final Map<String, String> lockOwners = new ConcurrentHashMap<>();

    private static final int LOCK_TIMEOUT_MINUTES = 10;

    public boolean lockBalanceSheet(Group group, User user) {
        ReentrantLock lock = balanceSheetLocks.computeIfAbsent(group.getName(), k -> new ReentrantLock());

        if(lock.tryLock()) {
            if (lockOwners.containsKey(group.getName()) && !lockOwners.get(group.getName()).equals(user.getEmail())) {
                lock.unlock();
                return false;
            }

            if(isLockExpired(group)) {
                releaseLock(group);
            }

            lockOwners.put(group.getName(), user.getEmail());
            lockTimeStamps.put(group.getName(), LocalDateTime.now());
            return true;
        }
        return false;
    }

    public boolean isLockExpired(Group group) {
        LocalDateTime lockTime = lockTimeStamps.get(group.getName());
        if(lockTime == null) return false;

        return LocalDateTime.now().isAfter(lockTime.plusMinutes(LOCK_TIMEOUT_MINUTES));
    }

    public void releaseLock(Group group) {
        ReentrantLock lock = balanceSheetLocks.get(group.getName());
        if(lock != null && lock.isHeldByCurrentThread()) {
            System.out.println("🔓 Lock released for " + group.getName());
            lockOwners.remove(group.getName());
            lockTimeStamps.remove(group.getName());
            lock.unlock();
        } 
    }

    public boolean validateLock(Group group, User user) {
        return lockOwners.get(group.getName()) != null && lockOwners.get(group.getName()).equals(user.getEmail());
    }
    
    public void cleanupExpiredLocks() {
        lockTimeStamps.entrySet().removeIf(entry -> 
            LocalDateTime.now().isAfter(entry.getValue().plusMinutes(LOCK_TIMEOUT_MINUTES))
        );
    }
}
