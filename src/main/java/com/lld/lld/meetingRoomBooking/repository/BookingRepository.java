package com.lld.lld.meetingRoomBooking.repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.lld.lld.meetingRoomBooking.models.Booking;

public class BookingRepository {
    
    // Thread-safe list for concurrent access
    private final List<Booking> bookings;

    public BookingRepository() {
        this.bookings = new CopyOnWriteArrayList<>();
    }

    public void createBooking(Booking booking) {
        bookings.add(booking);
    }

    public boolean cancelBooking(Booking booking) {
        return bookings.remove(booking);
    }

    public List<Booking> listBookingsforRoom(String roomId) {
        return bookings.stream()
                .filter(booking -> booking.getMeetingRoom().getRoomId().equals(roomId))
                .collect(Collectors.toList());
    }

    public List<Booking> listBookingsforEmployee(String employeeId) {
        return bookings.stream()
                .filter(booking -> booking.getBookedBy().getEmployeeId().equals(employeeId))
                .collect(Collectors.toList());
    }
}
