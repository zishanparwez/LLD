package com.lld.lld.parkingLot.observer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lld.lld.parkingLot.enums.ParkingStatus;
import com.lld.lld.parkingLot.enums.SpotType;
import com.lld.lld.parkingLot.models.ParkingFloor;
import com.lld.lld.parkingLot.models.ParkingLot;
import com.lld.lld.parkingLot.models.ParkingSpot;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DisplayBoard implements ParkingLotObserver {
    private Map<Integer, Map<SpotType, Integer>> availableSpots;

    public DisplayBoard() {
        this.availableSpots = new ConcurrentHashMap<>();
    }

    @Override
    public void update(ParkingLot parkingLot) {
        Map<Integer, Map<SpotType, Integer>> currentAvailableSpots = new HashMap<>();
        for(ParkingFloor parkingFloor: parkingLot.getParkingFloors()) {
            Map<SpotType, Integer> curFloor = new HashMap<>();
            for(ParkingSpot parkingSpot: parkingFloor.getParkingSpots()) {
                if(parkingSpot.getParkingStatus().equals(ParkingStatus.FREE)) {
                    curFloor.put(parkingSpot.getSpotType(), curFloor.getOrDefault(parkingSpot.getSpotType(), 0) + 1);
                }
            }
            currentAvailableSpots.put(parkingFloor.getFloor(), curFloor);
        }
        setAvailableSpots(currentAvailableSpots);
    }

    public void showDisplayBoard() {
        availableSpots.forEach((floor, value) -> {
            System.out.println("Available spots in floor: " + floor);
            value.forEach((spotType, availableSpots) -> {
                System.out.println("Available spots for " + spotType + ": " + availableSpots);
            });
        });
    }
}
