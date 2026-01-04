package com.lld.lld.meetingRoomBooking;

import com.lld.lld.meetingRoomBooking.concurrency.MeetingRoomLockManager;
import com.lld.lld.meetingRoomBooking.enums.BookingStatus;
import com.lld.lld.meetingRoomBooking.enums.RSVPStatus;
import com.lld.lld.meetingRoomBooking.models.Booking;
import com.lld.lld.meetingRoomBooking.models.Employee;
import com.lld.lld.meetingRoomBooking.models.MeetingRoom;
import com.lld.lld.meetingRoomBooking.repository.BookingRepository;
import com.lld.lld.meetingRoomBooking.repository.MeetingRoomRepository;
import com.lld.lld.meetingRoomBooking.service.BookingService;
import com.lld.lld.meetingRoomBooking.service.RSVPService;

import java.time.LocalDateTime;
import java.util.List;

public class MeetingRoomBookingDemo {
    public static void main(String[] args) {
        System.out.println("=== Meeting Room Booking System Demo ===\n");

        // Initialize system components
        BookingRepository bookingRepository = new BookingRepository();
        MeetingRoomRepository meetingRoomRepository = new MeetingRoomRepository();
        MeetingRoomLockManager roomLockManager = new MeetingRoomLockManager();
        BookingService bookingService = new BookingService(bookingRepository, meetingRoomRepository, roomLockManager);
        MeetingRoomBookingSystem system = new MeetingRoomBookingSystem(bookingService);
        RSVPService rsvpService = new RSVPService();

        // Create meeting rooms
        System.out.println("1. Creating Meeting Rooms...");
        MeetingRoom roomA = new MeetingRoom();
        roomA.setRoomId("room-1");
        roomA.setRoomNumber("101");
        roomA.setBookingStatus(BookingStatus.AVAILABLE);

        MeetingRoom roomB = new MeetingRoom();
        roomB.setRoomId("room-2");
        roomB.setRoomNumber("102");
        roomB.setBookingStatus(BookingStatus.AVAILABLE);

        MeetingRoom roomC = new MeetingRoom();
        roomC.setRoomId("room-3");
        roomC.setRoomNumber("103");
        roomC.setBookingStatus(BookingStatus.AVAILABLE);

        meetingRoomRepository.addMeetingRoom(roomA);
        meetingRoomRepository.addMeetingRoom(roomB);
        meetingRoomRepository.addMeetingRoom(roomC);

        System.out.println("Created rooms: " + roomA.getRoomNumber() + ", " + roomB.getRoomNumber() + ", " + roomC.getRoomNumber() + "\n");

        // Create employees
        System.out.println("2. Creating Employees...");
        Employee alice = Employee.builder()
                .employeeId("emp-1")
                .name("Alice")
                .rsvpService(rsvpService)
                .build();

        Employee bob = Employee.builder()
                .employeeId("emp-2")
                .name("Bob")
                .rsvpService(rsvpService)
                .build();

        Employee charlie = Employee.builder()
                .employeeId("emp-3")
                .name("Charlie")
                .rsvpService(rsvpService)
                .build();

        System.out.println("Created employees: " + alice.getName() + ", " + bob.getName() + ", " + charlie.getName() + "\n");

        // Get available rooms for a time slot
        System.out.println("3. Checking Available Rooms...");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime1 = now.plusDays(1).withHour(10).withMinute(0);
        LocalDateTime endTime1 = now.plusDays(1).withHour(11).withMinute(0);

        List<MeetingRoom> availableRooms = system.getAvailableRooms(startTime1, endTime1);
        System.out.println("Available rooms from " + startTime1 + " to " + endTime1 + ":");
        availableRooms.forEach(room -> 
            System.out.println("  - Room " + room.getRoomNumber() + " (ID: " + room.getRoomId() + ")")
        );
        System.out.println();

        // Book a room
        System.out.println("4. Booking Room A...");
        Booking booking1 = Booking.builder()
                .bookingId("booking-1")
                .meetingRoom(roomA)
                .bookedBy(alice)
                .bookingStatus(BookingStatus.PENDING)
                .bookingAgenda("Team Standup Meeting")
                .startTime(startTime1)
                .endTime(endTime1)
                .build();

        // Add participants and register them as observers
        booking1.getParticipants().put(bob, RSVPStatus.NEUTRAL);
        booking1.getParticipants().put(charlie, RSVPStatus.NEUTRAL);
        booking1.addObserver(bob);
        booking1.addObserver(charlie);

        boolean booked1 = system.bookRoom(booking1);
        if (booked1) {
            System.out.println("✓ Successfully booked Room " + roomA.getRoomNumber() + " for: " + booking1.getBookingAgenda());
        } else {
            System.out.println("✗ Failed to book Room " + roomA.getRoomNumber());
        }
        System.out.println();

        // Try to book the same room at overlapping time (should fail)
        System.out.println("5. Attempting Overlapping Booking (Should Fail)...");
        LocalDateTime startTime2 = now.plusDays(1).withHour(10).withMinute(30);
        LocalDateTime endTime2 = now.plusDays(1).withHour(11).withMinute(30);

        Booking booking2 = Booking.builder()
                .bookingId("booking-2")
                .meetingRoom(roomA)
                .bookedBy(bob)
                .bookingStatus(BookingStatus.PENDING)
                .bookingAgenda("Client Call")
                .startTime(startTime2)
                .endTime(endTime2)
                .build();

        boolean booked2 = system.bookRoom(booking2);
        if (booked2) {
            System.out.println("✓ Successfully booked Room " + roomA.getRoomNumber());
        } else {
            System.out.println("✗ Failed to book Room " + roomA.getRoomNumber() + " - Room is already booked during this time");
        }
        System.out.println();

        // Book a different room successfully
        System.out.println("6. Booking Room B (Different Room)...");
        Booking booking3 = Booking.builder()
                .bookingId("booking-3")
                .meetingRoom(roomB)
                .bookedBy(bob)
                .bookingStatus(BookingStatus.PENDING)
                .bookingAgenda("Client Call")
                .startTime(startTime2)
                .endTime(endTime2)
                .build();

        booking3.getParticipants().put(alice, RSVPStatus.NEUTRAL);
        booking3.addObserver(alice);

        boolean booked3 = system.bookRoom(booking3);
        if (booked3) {
            System.out.println("✓ Successfully booked Room " + roomB.getRoomNumber() + " for: " + booking3.getBookingAgenda());
        } else {
            System.out.println("✗ Failed to book Room " + roomB.getRoomNumber());
        }
        System.out.println();

        // RSVP responses
        System.out.println("7. RSVP Responses...");
        bob.respond(booking1, RSVPStatus.ACCEPTED);
        charlie.respond(booking1, RSVPStatus.ACCEPTED);
        alice.respond(booking3, RSVPStatus.ACCEPTED);
        System.out.println();

        // List bookings for a room
        System.out.println("8. List Bookings for Room A:");
        List<Booking> roomABookings = system.listBookingsForRoom(roomA.getRoomId());
        if (roomABookings.isEmpty()) {
            System.out.println("  No bookings found for Room " + roomA.getRoomNumber());
        } else {
            roomABookings.forEach(booking -> 
                System.out.println("  - " + booking.getBookingAgenda() + 
                    " by " + booking.getBookedBy().getName() + 
                    " (" + booking.getStartTime() + " to " + booking.getEndTime() + ")")
            );
        }
        System.out.println();

        // List bookings for an employee
        System.out.println("9. List Bookings for Alice:");
        List<Booking> aliceBookings = system.listBookingsForEmployee(alice.getEmployeeId());
        if (aliceBookings.isEmpty()) {
            System.out.println("  No bookings found for " + alice.getName());
        } else {
            aliceBookings.forEach(booking -> 
                System.out.println("  - " + booking.getBookingAgenda() + 
                    " in Room " + booking.getMeetingRoom().getRoomNumber() + 
                    " (" + booking.getStartTime() + " to " + booking.getEndTime() + ")")
            );
        }
        System.out.println();

        // Cancel a booking
        System.out.println("10. Canceling Booking...");
        System.out.println("Canceling booking: " + booking1.getBookingAgenda());
        system.cancelBooking(booking1);
        System.out.println("Booking canceled. Room " + roomA.getRoomNumber() + " is now available.\n");

        // Check available rooms again
        System.out.println("11. Checking Available Rooms Again (After Cancellation)...");
        availableRooms = system.getAvailableRooms(startTime1, endTime1);
        System.out.println("Available rooms from " + startTime1 + " to " + endTime1 + ":");
        if (availableRooms.isEmpty()) {
            System.out.println("  No rooms available");
        } else {
            availableRooms.forEach(room -> 
                System.out.println("  - Room " + room.getRoomNumber() + " (ID: " + room.getRoomId() + ")")
            );
        }
        System.out.println();

        // Book room again after cancellation
        System.out.println("12. Booking Room A Again (After Cancellation)...");
        LocalDateTime startTime3 = now.plusDays(1).withHour(14).withMinute(0);
        LocalDateTime endTime3 = now.plusDays(1).withHour(15).withMinute(0);

        Booking booking4 = Booking.builder()
                .bookingId("booking-4")
                .meetingRoom(roomA)
                .bookedBy(charlie)
                .bookingStatus(BookingStatus.PENDING)
                .bookingAgenda("Project Review")
                .startTime(startTime3)
                .endTime(endTime3)
                .build();

        booking4.getParticipants().put(alice, RSVPStatus.NEUTRAL);
        booking4.getParticipants().put(bob, RSVPStatus.NEUTRAL);
        booking4.addObserver(alice);
        booking4.addObserver(bob);

        boolean booked4 = system.bookRoom(booking4);
        if (booked4) {
            System.out.println("✓ Successfully booked Room " + roomA.getRoomNumber() + " for: " + booking4.getBookingAgenda());
        } else {
            System.out.println("✗ Failed to book Room " + roomA.getRoomNumber());
        }
        System.out.println();

        // Final summary
        System.out.println("13. Final Summary:");
        System.out.println("Total bookings in Room A: " + system.listBookingsForRoom(roomA.getRoomId()).size());
        System.out.println("Total bookings in Room B: " + system.listBookingsForRoom(roomB.getRoomId()).size());
        System.out.println("Total bookings in Room C: " + system.listBookingsForRoom(roomC.getRoomId()).size());
        System.out.println("Total bookings for Alice: " + system.listBookingsForEmployee(alice.getEmployeeId()).size());
        System.out.println("Total bookings for Bob: " + system.listBookingsForEmployee(bob.getEmployeeId()).size());
        System.out.println("Total bookings for Charlie: " + system.listBookingsForEmployee(charlie.getEmployeeId()).size());
        System.out.println();

        System.out.println("=== Demo Complete ===");
    }
}

