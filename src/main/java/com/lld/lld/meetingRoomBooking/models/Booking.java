package com.lld.lld.meetingRoomBooking.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.lld.lld.meetingRoomBooking.enums.BookingStatus;
import com.lld.lld.meetingRoomBooking.enums.RSVPStatus;
import com.lld.lld.meetingRoomBooking.observer.BookingSubject;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Booking extends BookingSubject {
    private String bookingId;
    private MeetingRoom meetingRoom;
    private Employee bookedBy;
    private BookingStatus bookingStatus;
    private String bookingAgenda;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Builder.Default
    private Map<Employee, RSVPStatus> participants = new HashMap<>();
}
