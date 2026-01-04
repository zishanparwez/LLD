package com.lld.lld.meetingRoomBooking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.lld.lld.meetingRoomBooking.concurrency.MeetingRoomLockManager;
import com.lld.lld.meetingRoomBooking.enums.BookingStatus;
import com.lld.lld.meetingRoomBooking.models.Booking;
import com.lld.lld.meetingRoomBooking.models.MeetingRoom;
import com.lld.lld.meetingRoomBooking.repository.BookingRepository;
import com.lld.lld.meetingRoomBooking.repository.MeetingRoomRepository;

public class BookingService {
    private BookingRepository bookingRepository;
    private MeetingRoomRepository meetingRoomRepository;
    private MeetingRoomLockManager roomLockManager;

    public BookingService(BookingRepository bookingRepository, MeetingRoomRepository meetingRoomRepository, MeetingRoomLockManager roomLockManager) {
        this.bookingRepository = bookingRepository;
        this.meetingRoomRepository = meetingRoomRepository;
        this.roomLockManager = roomLockManager;
    }

    public boolean bookRoom(Booking booking) {
        String roomId = booking.getMeetingRoom().getRoomId();
        String employeeId = booking.getBookedBy().getEmployeeId();
        
        // Try to acquire lock
        if (!roomLockManager.lockRoom(roomId, employeeId)) {
            throw new RuntimeException("Room is not available or already locked by another user!");
        }
        
        try {
            Optional<MeetingRoom> roomOpt = meetingRoomRepository.getMeetingRoomByRoomId(roomId);
            if (roomOpt.isPresent()) {
                MeetingRoom room = roomOpt.get();
                if (room.getBookingStatus().equals(BookingStatus.AVAILABLE) && 
                    isRoomAvailable(roomId, booking.getStartTime(), booking.getEndTime())) {
                    meetingRoomRepository.bookMeetingRoom(roomId);
                    booking.setBookingStatus(BookingStatus.BOOKED);
                    bookingRepository.createBooking(booking);
                    booking.notifyObservers(booking);
                    return true;
                }
            }
            return false;
        } finally {
            // Always release the lock
            roomLockManager.releaseLock(roomId);
        }
    }

    private boolean isRoomAvailable(String roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> existingBookings = bookingRepository.listBookingsforRoom(roomId);
        for (Booking existing : existingBookings) {
            if (existing.getBookingStatus() == BookingStatus.BOOKED && 
                existing.getStartTime().isBefore(endTime) && 
                existing.getEndTime().isAfter(startTime)) {
                return false;
            }
        }
        return true;
    }

    public List<MeetingRoom> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
        List<MeetingRoom> allAvailable = meetingRoomRepository.getAvailableRooms();
        // Filter rooms that are available and don't have overlapping bookings
        return allAvailable.stream()
                .filter(room -> isRoomAvailable(room.getRoomId(), startTime, endTime))
                .toList();
    }

    public void cancelBooking(Booking booking) {
        // first cancel booking and then update Meeting room status
        if(bookingRepository.cancelBooking(booking)) {
            Optional<MeetingRoom> room = meetingRoomRepository.getMeetingRoomByRoomId(booking.getMeetingRoom().getRoomId());
            if (room.isPresent()) {
                room.get().setBookingStatus(BookingStatus.AVAILABLE);
                booking.setBookingStatus(BookingStatus.CANCELED);
                booking.notifyObservers(booking);
            }
        }
    }

    public List<Booking> listBookingsforRoom(String roomId) {
        return bookingRepository.listBookingsforRoom(roomId);
    }

    public List<Booking> listBookingsforEmployee(String employeeId) {
        return bookingRepository.listBookingsforEmployee(employeeId);
    }

}
