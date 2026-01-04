package com.lld.lld.googleCalendar;

import com.lld.lld.googleCalendar.models.Event;
import com.lld.lld.googleCalendar.models.User;
import com.lld.lld.googleCalendar.repository.EventRepository;
import com.lld.lld.googleCalendar.service.AvailabilityService;
import com.lld.lld.googleCalendar.service.EventService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class CalendarSystem {
    private EventService eventService;
    private AvailabilityService availabilityService;
    

    public CalendarSystem() {
        EventRepository eventRepository = new EventRepository();
        this.eventService = new EventService(eventRepository);
        this.availabilityService = new AvailabilityService(eventRepository);
    }

    public CalendarSystem(EventService eventService, AvailabilityService availabilityService) {
        this.eventService = eventService;
        this.availabilityService = availabilityService;
    }

    public void addEvent(Event event) {
        eventService.addEvent(event);
    }

    public void removeEvent(String eventId) {
        eventService.removeEvent(eventId);
    }

    public void editEvent(Event event) {
        eventService.editEvent(event);
    }

    public Event getEvent(String eventId) {
        return eventService.getEvent(eventId);
    }

    public List<Event> getCalendarForUser(String userId) {
        return eventService.getEventsForUser(userId);
    }

    public List<AvailabilityService.TimeSlot> findCommonFreeSlots(
            List<User> users, LocalDateTime from, LocalDateTime to, Duration duration) {
        return availabilityService.findCommonFreeSlots(users, from, to, duration);
    }
}

