package com.lld.lld.googleCalendar.service;

import com.lld.lld.googleCalendar.enums.RSVPStatus;
import com.lld.lld.googleCalendar.models.Event;
import com.lld.lld.googleCalendar.models.User;

public interface RSVPService {
    void respond(Event event, User user, RSVPStatus status);
}

