package com.lld.lld.parkingLot.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingFloor {
    private Integer floor;
    private List<ParkingSpot> parkingSpots;
}
