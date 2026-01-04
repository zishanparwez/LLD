package com.lld.lld.meetingRoomBooking.models;

import com.lld.lld.meetingRoomBooking.enums.BookingStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingRoom {
    private String roomId;
    private String roomNumber;
    private BookingStatus bookingStatus;
}
