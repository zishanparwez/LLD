package com.lld.lld.googleCalendar.repository;

import com.lld.lld.googleCalendar.models.Event;

import java.util.*;

public class EventRepository {
    private Map<String, Event> events;
    private Map<String, List<Event>> userEvents; // userId -> list of events

    public EventRepository() {
        this.events = new HashMap<>();
        this.userEvents = new HashMap<>();
    }

    public void addEvent(Event event) {
        events.put(event.getEventId(), event);
        
        // Add event to owner's calendar
        String ownerId = event.getOwner().getName();
        userEvents.computeIfAbsent(ownerId, k -> new ArrayList<>()).add(event);
        
        // Add event to participants' calendars
        for (var participant : event.getParticipants()) {
            String participantId = participant.getName();
            userEvents.computeIfAbsent(participantId, k -> new ArrayList<>()).add(event);
        }
    }

    public void removeEvent(String eventId) {
        Event event = events.remove(eventId);
        if (event != null) {
            // Remove from owner's calendar
            String ownerId = event.getOwner().getName();
            List<Event> ownerEvents = userEvents.get(ownerId);
            if (ownerEvents != null) {
                ownerEvents.remove(event);
            }
            
            // Remove from participants' calendars
            for (var participant : event.getParticipants()) {
                String participantId = participant.getName();
                List<Event> participantEvents = userEvents.get(participantId);
                if (participantEvents != null) {
                    participantEvents.remove(event);
                }
            }
        }
    }

    public Event getEvent(String eventId) {
        return events.get(eventId);
    }

    public List<Event> getEventsForUser(String userId) {
        return userEvents.getOrDefault(userId, new ArrayList<>());
    }

    public List<Event> getAllEvents() {
        return new ArrayList<>(events.values());
    }

    public void updateEvent(Event event) {
        if (events.containsKey(event.getEventId())) {
            // Remove old event from user calendars
            removeEvent(event.getEventId());
            // Add updated event
            addEvent(event);
        }
    }
}

