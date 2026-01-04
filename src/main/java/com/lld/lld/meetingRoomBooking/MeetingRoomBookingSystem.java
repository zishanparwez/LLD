package com.lld.lld.meetingRoomBooking;

import com.lld.lld.meetingRoomBooking.concurrency.MeetingRoomLockManager;
import com.lld.lld.meetingRoomBooking.models.Booking;
import com.lld.lld.meetingRoomBooking.models.MeetingRoom;
import com.lld.lld.meetingRoomBooking.repository.BookingRepository;
import com.lld.lld.meetingRoomBooking.repository.MeetingRoomRepository;
import com.lld.lld.meetingRoomBooking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

public class MeetingRoomBookingSystem {
    private BookingService bookingService;

    public MeetingRoomBookingSystem() {
        BookingRepository bookingRepository = new BookingRepository();
        MeetingRoomRepository meetingRoomRepository = new MeetingRoomRepository();
        MeetingRoomLockManager roomLockManager = new MeetingRoomLockManager();
        this.bookingService = new BookingService(bookingRepository, meetingRoomRepository, roomLockManager);
    }

    public MeetingRoomBookingSystem(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public boolean bookRoom(Booking booking) {
        return bookingService.bookRoom(booking);
    }

    public List<MeetingRoom> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
        return bookingService.getAvailableRooms(startTime, endTime);
    }

    public void cancelBooking(Booking booking) {
        bookingService.cancelBooking(booking);
    }

    public List<Booking> listBookingsForRoom(String roomId) {
        return bookingService.listBookingsforRoom(roomId);
    }

    public List<Booking> listBookingsForEmployee(String employeeId) {
        return bookingService.listBookingsforEmployee(employeeId);
    }
}

