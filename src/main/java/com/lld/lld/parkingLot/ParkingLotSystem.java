package com.lld.lld.parkingLot;

import com.lld.lld.parkingLot.enums.SpotType;
import com.lld.lld.parkingLot.models.ParkingLot;
import com.lld.lld.parkingLot.models.ParkingTicket;
import com.lld.lld.parkingLot.models.Vehicle;
import com.lld.lld.parkingLot.observer.DisplayBoard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLotSystem {
    private ParkingLot parkingLot;
    private final DisplayBoard displayBoard;
    private static volatile ParkingLotSystem instance;

    public ParkingLotSystem() {
        this.displayBoard = new DisplayBoard();
    }

    public static synchronized ParkingLotSystem getInstance() {
        if(instance == null) {
            instance = new ParkingLotSystem();
        }
        return instance;
    }

    public ParkingTicket parkVehivle(Vehicle vehicle) {
        switch (vehicle.getVehicleType()) {
            case BIKE:
                return parkingLot.occupyParkingSpot(SpotType.SMALL, vehicle);
            case CAR:
                return parkingLot.occupyParkingSpot(SpotType.MEDIUM, vehicle);
            case TRUCK:
                return parkingLot.occupyParkingSpot(SpotType.LARGE, vehicle);
            default:
                break;
        }
        return null;
    }

    public void unparkVehicle(ParkingTicket parkingTicket) {
        parkingLot.vacateParkingSpot(parkingTicket);
    }

    public void showDisplayBoard() {
        displayBoard.showDisplayBoard();
    }
    
}
