package com.lld.lld.meetingRoomBooking.service;

import com.lld.lld.meetingRoomBooking.enums.RSVPStatus;
import com.lld.lld.meetingRoomBooking.models.Booking;
import com.lld.lld.meetingRoomBooking.models.Employee;

public class RSVPService {
    public void respond(Booking booking, Employee employee, RSVPStatus rsvpStatus) {
        if (booking != null && employee != null && rsvpStatus != null) {
            booking.getParticipants().put(employee, rsvpStatus);
            System.out.println("Employee " + employee.getName() + " responded " + rsvpStatus + " to meeting: " + booking.getBookingAgenda());
        }
    }
}
