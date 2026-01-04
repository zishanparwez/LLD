package com.lld.lld.googleCalendar.service;

import com.lld.lld.googleCalendar.models.Event;
import com.lld.lld.googleCalendar.repository.EventRepository;

import java.util.List;

public class EventService {
    private EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void addEvent(Event event) {
        if (event != null) {
            eventRepository.addEvent(event);
            // Notify all observers (participants) about the new event
            event.notifyObservers(event);
        }
    }

    public void removeEvent(String eventId) {
        Event event = eventRepository.getEvent(eventId);
        if (event != null) {
            eventRepository.removeEvent(event.getEventId());
        }
    }

    public void editEvent(Event event) {
        if (event != null && eventRepository.getEvent(event.getEventId()) != null) {
            eventRepository.updateEvent(event);
            // Notify all observers about the event update
            event.notifyObservers(event);
        }
    }

    public Event getEvent(String eventId) {
        return eventRepository.getEvent(eventId);
    }

    public List<Event> getEventsForUser(String userId) {
        return eventRepository.getEventsForUser(userId);
    }
}

