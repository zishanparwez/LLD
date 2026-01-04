package com.lld.lld.parkingLot.models;

import com.lld.lld.parkingLot.enums.ParkingStatus;
import com.lld.lld.parkingLot.enums.SpotType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingSpot {
    private String spotId;
    private ParkingStatus parkingStatus;
    private SpotType spotType;
}
