package com.lld.lld.bookMyShow;

import java.util.List;

import com.lld.lld.bookMyShow.concurrency.SeatLockManager;
import com.lld.lld.bookMyShow.models.Booking;
import com.lld.lld.bookMyShow.models.ShowSeat;
import com.lld.lld.bookMyShow.models.Show;
import com.lld.lld.bookMyShow.models.Ticket;
import com.lld.lld.bookMyShow.models.User;
import com.lld.lld.bookMyShow.strategy.PaymentSystem;

public class BookingSystem {
    private final PaymentSystem paymentSystem;
    private final SeatLockManager seatLockManager;

    public BookingSystem(PaymentSystem paymentSystem) {
        this.paymentSystem = paymentSystem;
        this.seatLockManager = new SeatLockManager();
    }

    public Booking createBooking(User user, Show show, List<ShowSeat> seats) {
        String userId = user.getEmail(); // Using email as user ID
        
        // Try to lock all seats
        for (ShowSeat seat : seats) {
            String seatId = seat.getSeat().getSeatNumber();
            if (!seatLockManager.lockSeat(seatId, userId)) {
                // Failed to lock seat, release any previously locked seats
                releaseSeatLocks(seats, userId);
                throw new RuntimeException("Seat " + seatId + " is not available or already locked");
            }
        }
        
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookedSeats(seats);
        return booking;
    }

    public Ticket confirmBooking(Booking booking) {
        try {
            Ticket ticket = booking.confirm(paymentSystem.getPaymentStrategy());
            
            // Release locks after successful booking
            if (ticket != null) {
                releaseSeatLocks(booking.getBookedSeats(), booking.getUser().getEmail());
            }
            
            return ticket;
        } catch (Exception e) {
            // Release locks on failure
            releaseSeatLocks(booking.getBookedSeats(), booking.getUser().getEmail());
            throw e;
        }
    }

    public void cancelBooking(Booking booking) {
        booking.cancel();
        // Release locks when cancelling
        releaseSeatLocks(booking.getBookedSeats(), booking.getUser().getEmail());
    }
    
    /**
     * Releases locks for all seats in the booking
     */
    private void releaseSeatLocks(List<ShowSeat> seats, String userId) {
        for (ShowSeat seat : seats) {
            String seatId = seat.getSeat().getSeatNumber();
            if (seatLockManager.validateLock(seatId, userId)) {
                seatLockManager.releaseLock(seatId);
            }
        }
    }
    
    /**
     * Cleanup expired locks (should be called periodically)
     */
    public void cleanupExpiredLocks() {
        seatLockManager.cleanupExpiredLocks();
    }
}
