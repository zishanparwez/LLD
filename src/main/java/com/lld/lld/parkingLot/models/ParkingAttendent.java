package com.lld.lld.parkingLot.models;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingAttendent {
    private String name;

    public ParkingTicket issuParkingTicket(Vehicle vehicle, ParkingSpot parkingSpot) {
        ParkingTicket ticket = new ParkingTicket(vehicle.getNumberPlate(), parkingSpot, LocalDateTime.now(), vehicle);
        return ticket;
    }
}
