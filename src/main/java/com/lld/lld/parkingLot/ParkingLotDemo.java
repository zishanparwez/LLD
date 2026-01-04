package com.lld.lld.parkingLot;

import java.util.ArrayList;
import java.util.List;

import com.lld.lld.parkingLot.enums.ParkingStatus;
import com.lld.lld.parkingLot.enums.SpotType;
import com.lld.lld.parkingLot.enums.VehicleType;
import com.lld.lld.parkingLot.models.ParkingAttendent;
import com.lld.lld.parkingLot.models.ParkingFloor;
import com.lld.lld.parkingLot.models.ParkingLot;
import com.lld.lld.parkingLot.models.ParkingSpot;
import com.lld.lld.parkingLot.models.ParkingTicket;
import com.lld.lld.parkingLot.models.PaymentSystem;
import com.lld.lld.parkingLot.models.Vehicle;
import com.lld.lld.parkingLot.observer.DisplayBoard;
import com.lld.lld.parkingLot.strategy.PayViaUPI;

public class ParkingLotDemo {
    public static void main(String[] args) {
        ParkingLotSystem instance = ParkingLotSystem.getInstance();
        
        // Create and setup the parking lot
        setupParkingLot(instance);
        
        // Create different types of vehicles
        Vehicle bike = createBike("BIKE001", "Honda CB350");
        Vehicle car = createCar("CAR001", "Toyota Camry");
        Vehicle truck = createTruck("TRUCK001", "Ford F-150");
        
        // Test parking operations
        System.out.println("=== Parking Lot Demo ===");
        System.out.println("Initial Display Board:");
        instance.showDisplayBoard();
        
        // Park vehicles
        System.out.println("\n--- Parking Vehicles ---");
        ParkingTicket bikeTicket = instance.parkVehivle(bike);
        ParkingTicket carTicket = instance.parkVehivle(car);
        ParkingTicket truckTicket = instance.parkVehivle(truck);
        
        System.out.println("\nAfter parking vehicles:");
        instance.showDisplayBoard();
        
        
        // Test unparking
        System.out.println("\n--- Unparking Vehicles ---");
        instance.unparkVehicle(bikeTicket);
        instance.unparkVehicle(carTicket);
        instance.unparkVehicle(truckTicket);
        
        System.out.println("\nAfter unparking some vehicles:");
        instance.showDisplayBoard();
    }
    
    private static void setupParkingLot(ParkingLotSystem instance) {
        // Create parking lot
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName("Downtown Parking Plaza");
        parkingLot.setAddress("123 Main Street, Downtown");
        
        // Create parking floors
        List<ParkingFloor> floors = new ArrayList<>();
        
        // Floor 1 - Ground Floor
        ParkingFloor floor1 = new ParkingFloor();
        floor1.setFloor(1);
        floor1.setParkingSpots(createParkingSpotsForFloor(1));
        floors.add(floor1);
        
        // Floor 2 - First Floor
        ParkingFloor floor2 = new ParkingFloor();
        floor2.setFloor(2);
        floor2.setParkingSpots(createParkingSpotsForFloor(2));
        floors.add(floor2);
        
        parkingLot.setParkingFloors(floors);
        
        // Create parking attendant
        ParkingAttendent attendant = new ParkingAttendent();
        attendant.setName("John Smith");
        parkingLot.setParkingAttendent(attendant);
        
        // Create payment system with UPI strategy
        PaymentSystem paymentSystem = new PaymentSystem();
        PayViaUPI upiPayment = new PayViaUPI();
        upiPayment.setUpiId("john@paytm");
        paymentSystem.setPaymentStrategy(upiPayment);
        parkingLot.setPaymentSystem(paymentSystem);
        
        // Set the parking lot in the system
        instance.setParkingLot(parkingLot);
        
        // Setup observer pattern
        DisplayBoard displayBoard = instance.getDisplayBoard();
        parkingLot.addObserver(displayBoard);
    }
    
    private static List<ParkingSpot> createParkingSpotsForFloor(int floorNumber) {
        List<ParkingSpot> spots = new ArrayList<>();
        
        // Create small spots (for bikes)
        for (int i = 1; i <= 5; i++) {
            ParkingSpot spot = new ParkingSpot();
            spot.setSpotId("F" + floorNumber + "S" + i);
            spot.setSpotType(SpotType.SMALL);
            spot.setParkingStatus(ParkingStatus.FREE);
            spots.add(spot);
        }
        
        // Create medium spots (for cars)
        for (int i = 1; i <= 8; i++) {
            ParkingSpot spot = new ParkingSpot();
            spot.setSpotId("F" + floorNumber + "M" + i);
            spot.setSpotType(SpotType.MEDIUM);
            spot.setParkingStatus(ParkingStatus.FREE);
            spots.add(spot);
        }
        
        // Create large spots (for trucks)
        for (int i = 1; i <= 3; i++) {
            ParkingSpot spot = new ParkingSpot();
            spot.setSpotId("F" + floorNumber + "L" + i);
            spot.setSpotType(SpotType.LARGE);
            spot.setParkingStatus(ParkingStatus.FREE);
            spots.add(spot);
        }
        
        return spots;
    }
    
    private static Vehicle createBike(String numberPlate, String model) {
        Vehicle bike = new Vehicle();
        bike.setNumberPlate(numberPlate);
        bike.setVehicleType(VehicleType.BIKE);
        return bike;
    }
    
    private static Vehicle createCar(String numberPlate, String model) {
        Vehicle car = new Vehicle();
        car.setNumberPlate(numberPlate);
        car.setVehicleType(VehicleType.CAR);
        return car;
    }
    
    private static Vehicle createTruck(String numberPlate, String model) {
        Vehicle truck = new Vehicle();
        truck.setNumberPlate(numberPlate);
        truck.setVehicleType(VehicleType.TRUCK);
        return truck;
    }
    
}
