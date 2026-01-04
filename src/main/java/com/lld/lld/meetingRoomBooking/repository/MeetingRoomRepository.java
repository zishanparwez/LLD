package com.lld.lld.meetingRoomBooking.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.lld.lld.meetingRoomBooking.enums.BookingStatus;
import com.lld.lld.meetingRoomBooking.models.MeetingRoom;

public class MeetingRoomRepository {
    
    // Thread-safe list for concurrent access
    private final List<MeetingRoom> meetingRooms;

    public MeetingRoomRepository() {
        this.meetingRooms = new CopyOnWriteArrayList<>();
    }

    public void addMeetingRoom(MeetingRoom room) {
        this.meetingRooms.add(room);
    }

    public Optional<MeetingRoom> getMeetingRoomByRoomId(String roomId) {
        return meetingRooms.stream()
                .filter(room -> room.getRoomId().equals(roomId))
                .findFirst();
    }

    public List<MeetingRoom> getAvailableRooms() {
        return meetingRooms.stream()
                .filter(room -> room.getBookingStatus().equals(BookingStatus.AVAILABLE))
                .collect(Collectors.toList());
    }

    public boolean bookMeetingRoom(String roomId) {
        Optional<MeetingRoom> room = meetingRooms.stream()
                .filter(r -> r.getRoomId().equals(roomId))
                .findFirst();
        if (room.isPresent() && room.get().getBookingStatus().equals(BookingStatus.AVAILABLE)) {
            room.get().setBookingStatus(BookingStatus.BOOKED);
            return true;
        }
        return false;
    }

    public void unbookMeetingRoom(String roomId) {
        meetingRooms.stream()
                .filter(r -> r.getRoomId().equals(roomId))
                .findFirst()
                .ifPresent(room -> room.setBookingStatus(BookingStatus.AVAILABLE));
    }
}
