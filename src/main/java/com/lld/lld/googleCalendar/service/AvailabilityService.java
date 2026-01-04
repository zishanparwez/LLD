package com.lld.lld.googleCalendar.service;

import com.lld.lld.googleCalendar.enums.RSVPStatus;
import com.lld.lld.googleCalendar.models.Event;
import com.lld.lld.googleCalendar.models.User;
import com.lld.lld.googleCalendar.repository.EventRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class AvailabilityService {
    private EventRepository eventRepository;

    public AvailabilityService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<TimeSlot> findCommonFreeSlots(List<User> users, LocalDateTime from, LocalDateTime to, Duration duration) {
        if (users == null || users.isEmpty() || from == null || to == null || duration == null) {
            return new ArrayList<>();
        }

        // Get all events for all users (only accepted or neutral events count as busy)
        Map<String, List<Event>> userEventsMap = new HashMap<>();
        for (User user : users) {
            List<Event> events = eventRepository.getEventsForUser(user.getName());
            // Filter only accepted or neutral events (rejected events don't block time)
            List<Event> busyEvents = events.stream()
                    .filter(event -> {
                        RSVPStatus status = event.getRsvpStatus().getOrDefault(user, RSVPStatus.NEUTRAL);
                        return status == RSVPStatus.ACCEPTED || status == RSVPStatus.NEUTRAL;
                    })
                    .filter(event -> event.overlaps(from, to))
                    .sorted(Comparator.comparing(Event::getStartTime))
                    .toList();
            userEventsMap.put(user.getName(), busyEvents);
        }

        // Find all busy time slots across all users
        List<TimeSlot> allBusySlots = new ArrayList<>();
        for (List<Event> events : userEventsMap.values()) {
            for (Event event : events) {
                LocalDateTime eventStart = event.getStartTime().isBefore(from) ? from : event.getStartTime();
                LocalDateTime eventEnd = event.getEndTime().isAfter(to) ? to : event.getEndTime();
                allBusySlots.add(new TimeSlot(eventStart, eventEnd));
            }
        }

        // Merge overlapping busy slots
        List<TimeSlot> mergedBusySlots = mergeTimeSlots(allBusySlots);

        // Find free slots
        List<TimeSlot> freeSlots = new ArrayList<>();
        LocalDateTime current = from;

        for (TimeSlot busySlot : mergedBusySlots) {
            if (current.isBefore(busySlot.start)) {
                LocalDateTime freeEnd = busySlot.start.isBefore(to) ? busySlot.start : to;
                if (Duration.between(current, freeEnd).compareTo(duration) >= 0) {
                    freeSlots.add(new TimeSlot(current, freeEnd));
                }
            }
            if (busySlot.end.isAfter(current)) {
                current = busySlot.end;
            }
        }

        // Check if there's a free slot after the last busy slot
        if (current.isBefore(to)) {
            if (Duration.between(current, to).compareTo(duration) >= 0) {
                freeSlots.add(new TimeSlot(current, to));
            }
        }

        return freeSlots;
    }

    private List<TimeSlot> mergeTimeSlots(List<TimeSlot> slots) {
        if (slots.isEmpty()) {
            return new ArrayList<>();
        }

        slots.sort(Comparator.comparing(slot -> slot.start));
        List<TimeSlot> merged = new ArrayList<>();
        TimeSlot current = slots.get(0);

        for (int i = 1; i < slots.size(); i++) {
            TimeSlot next = slots.get(i);
            if (current.end.isBefore(next.start)) {
                merged.add(current);
                current = next;
            } else {
                // Merge overlapping slots
                current = new TimeSlot(
                    current.start,
                    current.end.isAfter(next.end) ? current.end : next.end
                );
            }
        }
        merged.add(current);

        return merged;
    }

    public static class TimeSlot {
        public LocalDateTime start;
        public LocalDateTime end;

        public TimeSlot(LocalDateTime start, LocalDateTime end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "TimeSlot{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}

