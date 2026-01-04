package com.lld.lld.meetingRoomBooking.models;
import com.lld.lld.meetingRoomBooking.enums.RSVPStatus;
import com.lld.lld.meetingRoomBooking.observer.BookingObserver;
import com.lld.lld.meetingRoomBooking.service.RSVPService;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Employee implements BookingObserver {

    private String employeeId;
    private String name;
    private RSVPService rsvpService;

    public void respond(Booking booking, RSVPStatus status) {
        if (rsvpService != null) {
            rsvpService.respond(booking, this, status);
        }
    }

    @Override
    public void update(Booking booking) {
       switch (booking.getBookingStatus()) {
        case BOOKED:
            System.out.println("Employee " + name + " notified: Meeting room " + booking.getMeetingRoom().getRoomNumber() + " is booked for agenda: " + booking.getBookingAgenda());
            break;
        case CANCELED:
            System.out.println("Employee " + name + " notified: Meeting room " + booking.getMeetingRoom().getRoomNumber() + " is canceled for agenda: " + booking.getBookingAgenda());
            break;
        default:
            break;
       }
    }
}
