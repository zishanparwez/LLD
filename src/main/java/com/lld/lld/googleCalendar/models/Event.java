package com.lld.lld.googleCalendar.models;

import com.lld.lld.googleCalendar.enums.EventType;
import com.lld.lld.googleCalendar.enums.RSVPStatus;
import com.lld.lld.googleCalendar.observer.EventSubject;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Builder
public class Event extends EventSubject {
    private String eventId;
    private EventType eventType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private User owner;
    @Builder.Default
    private List<User> participants = new ArrayList<>();
    @Builder.Default
    private Map<User, RSVPStatus> rsvpStatus = new HashMap<>();
    private String title;

    public List<User> getParticipants() {
        return new ArrayList<>(participants);
    }

    public void addParticipant(User participant) {
        if (!participants.contains(participant)) {
            participants.add(participant);
            rsvpStatus.put(participant, RSVPStatus.NEUTRAL);
        }
    }

    public void removeParticipant(User participant) {
        participants.remove(participant);
        rsvpStatus.remove(participant);
    }

    public Map<User, RSVPStatus> getRsvpStatus() {
        return new HashMap<>(rsvpStatus);
    }

    public void setRsvpStatus(User user, RSVPStatus status) {
        if (participants.contains(user)) {
            rsvpStatus.put(user, status);
        }
    }

    public boolean overlaps(LocalDateTime start, LocalDateTime end) {
        return (startTime.isBefore(end) || startTime.isEqual(end)) &&
               (endTime.isAfter(start) || endTime.isEqual(start));
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", eventType=" + eventType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                ", owner=" + owner +
                ", title='" + title + '\'' +
                '}';
    }
}

