package com.lld.lld.parkingLot.observer;

import java.util.ArrayList;
import java.util.List;

import com.lld.lld.parkingLot.models.ParkingLot;

public abstract class ParkingLotSubject {
    
    private final List<ParkingLotObserver> observers = new ArrayList<>();

    public void addObserver(ParkingLotObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ParkingLotObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for(ParkingLotObserver parkingLotObserver: observers) {
            parkingLotObserver.update((ParkingLot) this);
        }
    }
}
