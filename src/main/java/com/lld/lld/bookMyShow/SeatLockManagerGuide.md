# Seat Lock Manager - Concurrency Solution

## Overview
The **Seat Lock Manager** is a simple and effective solution to prevent concurrent booking of the same seat in BookMyShow.

## How It Works

### 1. **Locking Mechanism**
- Uses `ReentrantLock` for thread-safe operations
- Each seat can be locked by only one user at a time
- Locks have a timeout (10 minutes default) to prevent indefinite locking

### 2. **Key Features**
- ✅ **Thread-Safe**: Multiple users can't book the same seat
- ✅ **Timeout-Based**: Locks expire automatically after 10 minutes
- ✅ **User-Specific**: Only the locking user can confirm/cancel
- ✅ **Automatic Cleanup**: Expired locks are removed automatically

### 3. **Lock Lifecycle**
```
1. User requests seat → Lock acquired
2. User confirms payment → Lock released
3. User cancels booking → Lock released
4. Lock timeout expires → Lock released automatically
```

## Implementation Details

### SeatLockManager Class
```java
// Lock a seat for a user
boolean locked = seatLockManager.lockSeat("S1", "user@email.com");

// Release a seat lock
seatLockManager.releaseLock("S1");

// Validate if user owns the lock
boolean valid = seatLockManager.validateLock("S1", "user@email.com");
```

### BookingSystem Integration
```java
// Create booking (automatically locks seats)
Booking booking = bookingSystem.createBooking(user, show, seats);

// Confirm booking (releases locks on success)
Ticket ticket = bookingSystem.confirmBooking(booking);

// Cancel booking (releases locks)
bookingSystem.cancelBooking(booking);
```

## Usage Example

### Scenario 1: Successful Booking
```java
// User A books seat S1
Booking bookingA = bookingSystem.createBooking(userA, show, Arrays.asList(seatS1));
Ticket ticketA = bookingSystem.confirmBooking(bookingA);
// ✅ Success: Seat S1 is booked
```

### Scenario 2: Concurrent Booking Prevention
```java
// User A books seat S1
Booking bookingA = bookingSystem.createBooking(userA, show, Arrays.asList(seatS1));
// 🔒 Seat S1 is now locked by User A

// User B tries to book the same seat S1
try {
    Booking bookingB = bookingSystem.createBooking(userB, show, Arrays.asList(seatS1));
} catch (RuntimeException e) {
    // ❌ Fails: "Seat S1 is not available or already locked"
}
```

## Demo
Run `ConcurrencyDemo.java` to see the Seat Lock Manager in action:

```bash
java ConcurrencyDemo
```

This will show:
- Two users trying to book the same seat (only one succeeds)
- Users booking different seats (both succeed)
- Automatic lock cleanup

## Benefits

1. **Simple**: Easy to understand and implement
2. **Reliable**: Prevents double booking completely
3. **Efficient**: Low overhead for single-instance applications
4. **User-Friendly**: Clear error messages when seats are unavailable

## Limitations

1. **Single Instance**: Works only within one application instance
2. **Memory-Based**: Locks are lost if application restarts
3. **Not Distributed**: Won't work across multiple servers

## When to Use

- ✅ Single-instance applications
- ✅ Development and testing
- ✅ Small to medium scale systems
- ❌ Distributed systems (use database locking instead)
- ❌ High-availability production systems

## Best Practices

1. **Set Reasonable Timeouts**: 5-15 minutes is usually sufficient
2. **Cleanup Regularly**: Call `cleanupExpiredLocks()` periodically
3. **Handle Exceptions**: Always release locks in finally blocks
4. **Monitor Locks**: Log lock conflicts for debugging

This simple approach provides a solid foundation for understanding concurrency in booking systems!
