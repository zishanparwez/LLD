package com.lld.lld.parkingLot.models;

import com.lld.lld.parkingLot.enums.VehicleType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vehicle {
    private String numberPlate;
    private VehicleType vehicleType;
}
