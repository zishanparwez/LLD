package com.lld.lld.googleCalendar;

import com.lld.lld.googleCalendar.enums.EventType;
import com.lld.lld.googleCalendar.enums.RSVPStatus;
import com.lld.lld.googleCalendar.models.Event;
import com.lld.lld.googleCalendar.models.User;
import com.lld.lld.googleCalendar.service.AvailabilityService;
import com.lld.lld.googleCalendar.service.RSVPService;
import com.lld.lld.googleCalendar.service.RSVPServiceImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class GoogleCalendarDemo {
    public static void main(String[] args) {
        System.out.println("=== Google Calendar System Demo ===\n");

        // Initialize calendar system
        CalendarSystem calendarSystem = new CalendarSystem();
        RSVPService rsvpService = new RSVPServiceImpl();

        // Create users
        System.out.println("1. Creating Users...");
        User alice = User.builder()
                .name("Alice")
                .rsvpService(rsvpService)
                .build();

        User bob = User.builder()
                .name("Bob")
                .rsvpService(rsvpService)
                .build();

        User charlie = User.builder()
                .name("Charlie")
                .rsvpService(rsvpService)
                .build();

        System.out.println("Created users: " + alice.getName() + ", " + bob.getName() + ", " + charlie.getName() + "\n");

        // Create events
        System.out.println("2. Creating Events...");
        LocalDateTime now = LocalDateTime.now();
        
        Event teamMeeting = Event.builder()
                .eventId("event-1")
                .eventType(EventType.MEETING)
                .startTime(now.plusDays(1).withHour(10).withMinute(0))
                .endTime(now.plusDays(1).withHour(11).withMinute(0))
                .location("Conference Room A")
                .owner(alice)
                .title("Team Standup Meeting")
                .build();

        // Add participants to the event
        teamMeeting.addParticipant(bob);
        teamMeeting.addParticipant(charlie);
        
        // Register participants as observers
        teamMeeting.addObserver(bob);
        teamMeeting.addObserver(charlie);

        Event birthday = Event.builder()
                .eventId("event-2")
                .eventType(EventType.BIRTHDAY)
                .startTime(now.plusDays(2).withHour(14).withMinute(0))
                .endTime(now.plusDays(2).withHour(16).withMinute(0))
                .location("Office Cafeteria")
                .owner(bob)
                .title("Bob's Birthday Party")
                .build();

        birthday.addParticipant(alice);
        birthday.addParticipant(charlie);
        birthday.addObserver(alice);
        birthday.addObserver(charlie);

        Event reminder = Event.builder()
                .eventId("event-3")
                .eventType(EventType.REMINDER)
                .startTime(now.plusHours(2))
                .endTime(now.plusHours(2).plusMinutes(15))
                .location("Home")
                .owner(charlie)
                .title("Call Mom")
                .build();

        System.out.println("Created events:");
        System.out.println("  - " + teamMeeting.getTitle() + " (ID: " + teamMeeting.getEventId() + ")");
        System.out.println("  - " + birthday.getTitle() + " (ID: " + birthday.getEventId() + ")");
        System.out.println("  - " + reminder.getTitle() + " (ID: " + reminder.getEventId() + ")\n");

        // Add events to calendar system
        System.out.println("3. Adding Events to Calendar System...");
        calendarSystem.addEvent(teamMeeting);
        calendarSystem.addEvent(birthday);
        calendarSystem.addEvent(reminder);
        System.out.println("All events added successfully!\n");

        // RSVP responses
        System.out.println("4. RSVP Responses...");
        bob.respond(teamMeeting, RSVPStatus.ACCEPTED);
        charlie.respond(teamMeeting, RSVPStatus.ACCEPTED);
        alice.respond(birthday, RSVPStatus.ACCEPTED);
        charlie.respond(birthday, RSVPStatus.REJECTED);
        System.out.println();

        // Display RSVP status
        System.out.println("5. RSVP Status for Team Meeting:");
        teamMeeting.getRsvpStatus().forEach((user, status) -> 
            System.out.println("  " + user.getName() + ": " + status)
        );
        System.out.println();

        // Get calendar for a user
        System.out.println("6. Getting Calendar for Alice:");
        List<Event> aliceCalendar = calendarSystem.getCalendarForUser(alice.getName());
        aliceCalendar.forEach(event -> 
            System.out.println("  - " + event.getTitle() + " (" + event.getEventType() + ") at " + event.getStartTime())
        );
        System.out.println();

        // Get event details
        System.out.println("7. Getting Event Details:");
        Event retrievedEvent = calendarSystem.getEvent("event-1");
        if (retrievedEvent != null) {
            System.out.println("Event ID: " + retrievedEvent.getEventId());
            System.out.println("Title: " + retrievedEvent.getTitle());
            System.out.println("Type: " + retrievedEvent.getEventType());
            System.out.println("Start: " + retrievedEvent.getStartTime());
            System.out.println("End: " + retrievedEvent.getEndTime());
            System.out.println("Location: " + retrievedEvent.getLocation());
            System.out.println("Owner: " + retrievedEvent.getOwner().getName());
            System.out.println("Participants: " + retrievedEvent.getParticipants().size());
        }
        System.out.println();

        // Edit an event
        System.out.println("8. Editing Event...");
        teamMeeting.setLocation("Conference Room B (Updated)");
        teamMeeting.setEndTime(now.plusDays(1).withHour(11).withMinute(30));
        calendarSystem.editEvent(teamMeeting);
        System.out.println("Event updated! New location: " + teamMeeting.getLocation());
        System.out.println("New end time: " + teamMeeting.getEndTime() + "\n");

        // Find common free slots
        System.out.println("9. Finding Common Free Slots...");
        LocalDateTime searchStart = now.plusDays(1).withHour(9).withMinute(0);
        LocalDateTime searchEnd = now.plusDays(1).withHour(17).withMinute(0);
        Duration slotDuration = Duration.ofHours(1);

        List<AvailabilityService.TimeSlot> freeSlots = calendarSystem.findCommonFreeSlots(
                Arrays.asList(alice, bob, charlie),
                searchStart,
                searchEnd,
                slotDuration
        );

        System.out.println("Common free slots for Alice, Bob, and Charlie:");
        System.out.println("Search period: " + searchStart + " to " + searchEnd);
        System.out.println("Minimum duration: " + slotDuration.toHours() + " hour(s)");
        if (freeSlots.isEmpty()) {
            System.out.println("  No common free slots found.");
        } else {
            freeSlots.forEach(slot -> 
                System.out.println("  Free slot: " + slot.start + " to " + slot.end)
            );
        }
        System.out.println();

        // Create another event to demonstrate overlapping
        System.out.println("10. Creating Additional Event to Test Availability...");
        Event projectReview = Event.builder()
                .eventId("event-4")
                .eventType(EventType.MEETING)
                .startTime(now.plusDays(1).withHour(13).withMinute(0))
                .endTime(now.plusDays(1).withHour(14).withMinute(0))
                .location("Conference Room C")
                .owner(alice)
                .title("Project Review")
                .build();

        projectReview.addParticipant(bob);
        projectReview.addParticipant(charlie);
        projectReview.addObserver(bob);
        projectReview.addObserver(charlie);

        bob.respond(projectReview, RSVPStatus.ACCEPTED);
        charlie.respond(projectReview, RSVPStatus.ACCEPTED);

        calendarSystem.addEvent(projectReview);
        System.out.println("Added: " + projectReview.getTitle() + "\n");

        // Find common free slots again
        System.out.println("11. Finding Common Free Slots Again (After Adding Project Review):");
        freeSlots = calendarSystem.findCommonFreeSlots(
                Arrays.asList(alice, bob, charlie),
                searchStart,
                searchEnd,
                slotDuration
        );

        if (freeSlots.isEmpty()) {
            System.out.println("  No common free slots found.");
        } else {
            freeSlots.forEach(slot -> 
                System.out.println("  Free slot: " + slot.start + " to " + slot.end)
            );
        }
        System.out.println();

        // Remove an event
        System.out.println("12. Removing Event...");
        calendarSystem.removeEvent("event-3");
        System.out.println("Removed event-3 (Call Mom reminder)");
        
        List<Event> charlieCalendar = calendarSystem.getCalendarForUser(charlie.getName());
        System.out.println("Charlie's calendar now has " + charlieCalendar.size() + " event(s)\n");

        // Display final calendar summary
        System.out.println("13. Final Calendar Summary:");
        System.out.println("Alice's events: " + calendarSystem.getCalendarForUser(alice.getName()).size());
        System.out.println("Bob's events: " + calendarSystem.getCalendarForUser(bob.getName()).size());
        System.out.println("Charlie's events: " + calendarSystem.getCalendarForUser(charlie.getName()).size());
        System.out.println();

        System.out.println("=== Demo Complete ===");
    }
}

