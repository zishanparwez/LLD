package com.lld.lld.googleCalendar.observer;

import com.lld.lld.googleCalendar.models.Event;

public interface EventObserver {
    void update(Event event);
}

