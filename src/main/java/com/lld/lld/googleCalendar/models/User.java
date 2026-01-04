package com.lld.lld.googleCalendar.models;

import com.lld.lld.googleCalendar.enums.RSVPStatus;
import com.lld.lld.googleCalendar.observer.EventObserver;
import com.lld.lld.googleCalendar.service.RSVPService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User implements EventObserver {
    private String name;
    private RSVPService rsvpService;

    public void respond(Event event, RSVPStatus status) {
        if (rsvpService != null) {
            rsvpService.respond(event, this, status);
        }
    }

    @Override
    public void update(Event event) {
        System.out.println("User " + name + " notified about event: " + event.getTitle());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name != null ? name.equals(user.name) : user.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{name='" + name + "'}";
    }
}

