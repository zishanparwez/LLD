
## Requirements

Build an online parking lot management system that can support the following requirements:

* Should have multiple floors.
* Multiple entries and exit gates.
* A user has to collect a ticket at entry and pay at or before exit.
* Pay at: Exit counter (Cash to the parking attendant)
* Dedicated automated booth on each floor - Payment counter

* Payment via type:
    - Cash
    - Credit Card
    - UPI
* Allow entry for a vehicle if a spot is available for it.
* Show on the display board at entry if a spot is not available.
* Parking Spots of 3 types:
    - Large
    - Medium
    - Small
* A car can only be parked at its spot. Not on any other (even larger).
* A display on each floor with the status of that floor.
* Fees calculated based on per hour price: e.g. 50 rs for the first hour, then 80 rs per  extra hour. invoice
    - Small - 50, 80
    - Medium - 80, 100
    - Large - 100, 120

## Use Case Diagram

```plantuml
@startuml
left to right direction
actor ParkingAttendant
actor Customer
actor Admin

rectangle FastAndCalm {
    Admin --> (Add a parking lot)
    Admin --> (Add a parking floor)
    Admin --> (Add a parking spot)
    Admin --> (Update status of parking spot)

    usecase "Pay" as Pay
    usecase "Pay Online" as PayOnline
    usecase "Pay Cash" as PayCash

    Customer --> (Pay)
    Customer --> (Check spot's status)

    PayOnline .> (Pay) : extends
    PayCash .> (Pay) : extends


    ParkingAttendant --> (Check empty slots)
    ParkingAttendant --> (Issue a ticket)
    ParkingAttendant --> (Collect payment)
    ParkingAttendant --> (Checkout)

    (Issue a ticket) .> (Allocate a slot) : includes
    Checkout .> (CheckPaymentStatus) : includes
}
@enduml
```

## Entities and Attributes

* ParkingLotSystem
    * ParkingLot
    * DisplayBoard

* ParkingLot
  * Name
  * Address
  * ParkingFloors
  * ParkingAttendent

* ParkingFloor
  * Floor Number
  * ParkingSpots

* ParkingSpot
  * Spot Number
  * Spot Type - `Large, Medium, Small`
  * Status - `Occupied, Free, Out of order`

* ParkingTicket
  * Ticket ID
  * ParkingSpot
  * Entry Time
  * Vehicle
  
* PaymentSystem
  * Ticket
  * PaymentStrategy
  
* Vehicle
  * License Plate
  * Vehicle Type - `Car, Truck, Bus, Bike, Scooter`

* ParkingAttendent
  * Name
  * DisplayBoard
  * PaymentSystem


## Facade + Observer + Strategy Design Pattern
* Facade - (ParkingLotSystem to hide all unnecessary details like Payment) 
* Observer - (ParkingLotObserver to update DisplayBoard)
* Strategy - (PaymentSystem to incorporate different methods of Payment)

```mermaid
    classDiagram
        class ParkingLotSystem {
            - parkingLot: ParkingLot
            - displayBoard: DisplayBoard
            + parkVehicle(Vehicle): ParkingTicket
            + unparkVehicle(Vehicle): void
            + getInstace(): ParkingLotSystem
            + showDisplayBoard(): void
        }

        class ParkingLot {
            - name: String
            - address: String
            - parkingFloors: ParkingFloor[]
            - parkingAttendent: ParkingAttendent
            - paymentSystem: PaymentSystem
            + getAvailableParkingSpot(SpotType): ParkingSpot
            + getParkingSpot(SpotType): ParkingSpot
            + occupyParkingSpot(SpotType): ParkingTicket
            + vacateParkingSpot(ParkingTicket): void
        }

        class DisplayBoard {
            - availableSpots: Map<Integer, Map<SpotType, Integer>>
            + update(ParkingLot): void
            + showDisplayBoard(): void
        }

        class ParkingAttendent {
            - name: String
            + issueParkingTicket(Vehicle): ParkingTicket
        }

        class ParkingTicket {
            - ticketId: String
            - parkingSpot: ParkingSpot
            - entryTime: time
            - vehicle: Vehicle
        }

        class PaymentSystem {
            - parkingTicket: ParkingTicket
            - paymnetStrategy: PaymentStrategy
            + calculateFare(ParkingTicket): double
            + pay(ParkingTicket): void
        }

        class PaymentStrategy {
            <<interface>>
            + calculateFare(ParkingTicket): double
            + pay(ParkingTicket): void
        }

        class PayViaCreditCard {
            - paymentMethodType: `CARD`
            - cardNumber: String
            - cvv: int
            - expiryData: String
            - serviceCharge: double
            + calculateFare(ParkingTicket): double
            + pay(ParkingTicket): void
        }

        class PayViaUPI {
            - paymentMethodType: `UPI`
            - upiId: String
            - serviceCharge: double
            + calculateFare(ParkingTicket): double
            + pay(ParkingTicket): void
        }

        class ParkingFloor {
            - floor: int
            - parkingSpots: ParkingSpot[]
        }

        class ParkingSpot {
            - spotId: String
            - spotType: SpotType
            - parkingStatus: ParkingStatus
        }

        class Vehicle {
            - lisensePlateNumber: String
            - vehicleType: VehicleType
        }

        class SpotType {
            <<enumeration>>
            SMALL,
            MEDIUM,
            LARGE
        }

        class VehicleType {
            <<enumeration>>
            BIKE,
            CAR,
            TRUCK
        }

        class PaymentMethodType {
            <<enumeration>>
            CARD,
            UPI
        }

        class ParkingStatus {
            <<enumeration>>
            OCCUPIED,
            FREE
        }

        ParkingLotSystem --* ParkingLot
        ParkingLotSystem --* DisplayBoard

        ParkingLot --* ParkingFloor
        ParkingLot --* ParkingAttendent
        ParkingLot --* PaymentSystem

        ParkingFloor --* ParkingSpot

        ParkingSpot --o SpotType
        ParkingSpot --o ParkingStatus

        PaymentSystem --o PaymentStrategy
        PaymentSystem --o ParkingTicket
        
        PaymentStrategy --o PaymentMethodType
        PaymentStrategy <|-- PayViaCreditCard
        PaymentStrategy <|-- PayViaUPI

        ParkingTicket --o Vehicle
        ParkingTicket --o ParkingSpot

        Vehicle --o VehicleType

        Observer Design Pattern to update Display Board

        class ParkingLotSubject {
            - observers: List<ParkingLotObserver>
            + addObserver(ParkingLotObserver): void
            + removeObserver(ParkingLotObserver): void
            + notifyObservers(): void
        }

        class ParkingLotObserver {
            + update(ParkingLot): void
        }

        ParkingLot --|> ParkingLotSubject

        ParkingLotSubject --* ParkingLotObserver
        DisplayBoard ..|> ParkingLotObserver

```