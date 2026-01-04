package com.lld.lld.meetingRoomBooking.observer;

import java.util.ArrayList;
import java.util.List;
import com.lld.lld.meetingRoomBooking.models.Booking;

public class BookingSubject {
    
    private List<BookingObserver> observers;

    public BookingSubject() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BookingObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Booking booking) {
        for (BookingObserver observer : observers) {
            observer.update(booking);
        }
    }
    
}
