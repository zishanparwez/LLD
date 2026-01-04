package com.lld.lld.googleCalendar.service;

import com.lld.lld.googleCalendar.enums.RSVPStatus;
import com.lld.lld.googleCalendar.models.Event;
import com.lld.lld.googleCalendar.models.User;

public class RSVPServiceImpl implements RSVPService {
    @Override
    public void respond(Event event, User user, RSVPStatus status) {
        if (event != null && user != null && status != null) {
            event.setRsvpStatus(user, status);
            System.out.println("User " + user.getName() + " responded " + status + " to event: " + event.getTitle());
        }
    }
}

