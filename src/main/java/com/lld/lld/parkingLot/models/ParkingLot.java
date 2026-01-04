package com.lld.lld.parkingLot.models;

import java.util.List;

import com.lld.lld.parkingLot.enums.ParkingStatus;
import com.lld.lld.parkingLot.enums.SpotType;
import com.lld.lld.parkingLot.observer.ParkingLotSubject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLot extends ParkingLotSubject {
    private String name;
    private String address;
    private List<ParkingFloor> parkingFloors;
    private ParkingAttendent parkingAttendent;
    private PaymentSystem paymentSystem;

    public ParkingSpot getAvailableParkingSpot(SpotType spotType) {
        for(ParkingFloor parkingFloor: parkingFloors) {
            for(ParkingSpot parkingSpot: parkingFloor.getParkingSpots()) {
                if(parkingSpot.getParkingStatus().equals(ParkingStatus.FREE) && parkingSpot.getSpotType().equals(spotType)) {
                    return parkingSpot;
                }
            }
        }
        return null;
    }

    public ParkingSpot getParkingSpot(String spotId) {
        for(ParkingFloor parkingFloor: parkingFloors) {
            for(ParkingSpot parkingSpot: parkingFloor.getParkingSpots()) {
                if(parkingSpot.getSpotId().equals(spotId)) {
                    return parkingSpot;
                }
            }
        }
        return null;
    }

    public ParkingTicket occupyParkingSpot(SpotType spotType, Vehicle vehicle) {

        ParkingSpot parkingSpot = getAvailableParkingSpot(spotType);
        ParkingTicket parkingTicket = null;
        if(parkingSpot != null) {
            // notify observer to update display board
            parkingSpot.setParkingStatus(ParkingStatus.OCCUPIED);
            parkingTicket = parkingAttendent.issuParkingTicket(vehicle, parkingSpot);
            notifyObservers();
        }
        return parkingTicket;
    }

    public void vacateParkingSpot(ParkingTicket parkingTicket) {
        ParkingSpot parkingSpot = getParkingSpot(parkingTicket.getParkingSpot().getSpotId());
        if(parkingSpot != null) {
            // notify observer to update display board
            parkingSpot.setParkingStatus(ParkingStatus.FREE);
            paymentSystem.pay(parkingTicket);
            notifyObservers();
        }
    }
}
