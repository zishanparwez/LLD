package com.lld.lld.googleCalendar.observer;

import com.lld.lld.googleCalendar.models.Event;

import java.util.ArrayList;
import java.util.List;

public class EventSubject {
    private List<EventObserver> observers;

    public EventSubject() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(EventObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(EventObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Event event) {
        for (EventObserver observer : observers) {
            observer.update(event);
        }
    }

    public List<EventObserver> getObservers() {
        return new ArrayList<>(observers);
    }
}

