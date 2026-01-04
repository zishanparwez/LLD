
### Design BookMyShow

## Create a movie booking management system like BookMyShow to allow a user to search and book movie tickets.

## Requirements
* Support for multiple cities
* Each city will have multiple cinema theatre
* Each cinema can have multiple screens
* Each screen will play one movie at a time
* A theatre will play multiple movies
* Each movie can have multiple shows
* Each screen can have multiple types of seats
    - GOLD
    - DIAMOND
    - PLATINUM
* Allow the user to search a movie by name
* Allow the user to filter movies by the following fields
    - Location
    - Cinema
    - Language
    - Rating
    - Category
* Users can book tickets and pay via multiple payment methods
    - UPI
    - Credit Card
    - Netbanking
* A user can apply a coupon or a promo code at checkout
* A user can see the availability of seats in a hall
* The price of a ticket will be decided by multiple parameters
    - Seat Type
    - Day of the week
    - Time of the Day
    - Movie
    - Cinema hall
* A user can also cancel or update a booking
* A user cannot book or cancel after the cutoff time which is 1 hour before the movie starts


## Entities and Attributes

* User
    - name: String
    - email: String
    - location: Location

* Screen
    - screenType: `2D 3D 4D`
    - audioType: `DOLBY NORMAL`
    - name: String
    - shows: Show[]
    - seats: Seats[]

* Show
    - movie: Movie
    - time: String
    - language: String


* Movie
    - name: String
    - rating: Number
    - genre: String


* Theatre
    - screens: Screen[]
    - name: String
    - address: String
    - city: String
    - state: String
    - pinCode: String

* Seat
    - status: `BOOKED AVAILABLE`
    - seatNumner: String
    - seatType: `SILVER GOLD PLATINUM`
    - price: Double

* Ticket
    - ticketId: String
    - bookingStatus: `IN-PROGRESS FAILED COMPLETED CANCELED`
    - seats: Seat[]
    - bookedAt: String

* BookingSystem
    - checkSeatAvailablilty(Seat[], Screen): boolean
    - issueTicket(Seat[], User, Screen): Ticket
    - cancelTicket(Ticket): boolean

* Search
    - search(input, type)

* PaymentSystem
    - pay()

## Problems
* Concurrency not handled

```mermaid
    classDiagram
        class BookingSystem {
            - paymentSystem: PaymentSystem
            + createBooking(User, Show, Seat[]): Booking
            + confirmBooking(Booking, PaymentStrategy): Ticket
            + cancelBooking(Booking): void
        }

        class Movie {
            - name: String
            - rating: Number
            - genre: String
            - languages: String[]
        }

        class Show {
            - movie: Movie
            - startTime: DateTime
            - endTime: DateTime
            - language: String
            - showSeats: ShowSeat[]
            + getAvailableSeats(): ShowSeat[]
        }

        class ShowSeat {
            - seat: Seat
            - status: SeatStatus
            - price: double
            + isAvailable(): boolean
            + lock(): void
            + book(): void
            + release(): void
        }

        class Screen {
            - screenType: ScreenType
            - audioType: AudioType
            - name: String
            - shows: Show[]
            - seats: Seat[]
        }

        class Seat {
            - seatNumber: String
            - seatType: SeatType
        }

        class Theatre {
            - name: String
            - screens: Screen[]
            - location: Location
        }

        class Location {
            address: String
            city: String
            state: String
            pinCode: String
        }

        class User {
            - name: String
            - email: String
            - location: Location
        }

        class Booking {
            - user: User
            - show: Show
            - bookedSeats: ShowSeat[]
            - status: BookingStatus
            + confirm(PaymentStrategy): Ticket
            + cancel(): void
            + bookSeat(): void
            + selectSeat(): void
            + unselectSeat(): void
        }

        class BookingStatus {
            <<enumeration>>
            INITIATED,
            CONFIRMED,
            CANCELLED,
            EXPIRED
        }

        class Ticket {
            - user: User
            - show: Show
            - seats: ShowSeat[]
            - bookedAt: DateTime
        }

        class PaymentSystem {
            + pay(Ticket, PaymentStrategy): boolean
            + calculateAmount(Ticket): double
        }

        class PaymentStrategy {
            <<interface>>
            + calculateAmount(Ticket): double
            + pay(Ticket): boolean
        }

        class PayViaCreditCard {
            - payMethodType: PaymentMethodType
            - cardNumber: String
            - cvv: int
            - expiryDate: String
            - serviceCharge: double
            + calculateAmount(Ticket): double
            + pay(Ticket): boolean
        }

        class PayViaUPI {
            - payMethodType: PaymentMethodType
            - upiId: String
            - serviceCharge: double
            + calculateAmount(Ticket): double
            + pay(Ticket): boolean
        }

        PayViaCreditCard --o PaymentMethodType
        PayViaUPI --o PaymentMethodType

        class PaymentMethodType {
            <<enumeration>>
            CARD,
            UPI
        }

        class SeatType {
            <<enumeration>>
            SILVER,
            GOLD,
            PLATINUM
        }

        class ScreenType {
            <<enumeration>>
            2D,
            3D,
            4D
        }

        class AudioType {
            <<enumeration>>
            NORMAL,
            DOLBY
        }

        class SeatStatus {
            <<enumeration>>
            AVAILABLE,
            LOCKED,
            BOOKED
        }
        
        BookingSystem --> Booking
        BookingSystem --o PaymentSystem
        Booking --o BookingStatus
        Booking --* User
        Booking --* Show
        Booking --* ShowSeat
        Booking --* Ticket

        Ticket --* ShowSeat
        Ticket --* User
        Ticket --* Show

        Show --* Movie
        Show --* ShowSeat

        ShowSeat --o SeatStatus

        ShowSeat --* Seat

        Seat --o SeatType

        Screen --* Seat
        Screen --* Show
        Screen --o ScreenType
        Screen --o AudioType

        Theatre --* Screen
        Theatre --* Location

        User --* Location

        PaymentSystem --* PaymentStrategy
        PaymentStrategy <|.. PayViaCreditCard
        PaymentStrategy <|.. PayViaUPI
```

## Problems
* ~~Concurrency not handled~~

```mermaid
    classDiagram
        class BookingSystem {
            - paymentSystem: PaymentSystem
            - seatLockManager: SeatLockManager
            + createBooking(User, Show, ShowSeat[]): Booking
            + confirmBooking(Booking): Ticket
            + cancelBooking(Booking): void
            + releaseSeatLocks(ShowSeat[], String): void
            + cleanupExpiredLocks(): void
        }

        class Movie {
            - name: String
            - rating: Number
            - genre: String
            - languages: String[]
        }

        class Show {
            - movie: Movie
            - startTime: DateTime
            - endTime: DateTime
            - language: String
            - showSeats: ShowSeat[]
            + getAvailableSeats(): ShowSeat[]
        }

        class ShowSeat {
            - seat: Seat
            - status: SeatStatus
            - price: double
            + isAvailable(): boolean
            + lock(): void
            + book(): void
            + release(): void
        }

        class Screen {
            - screenType: ScreenType
            - audioType: AudioType
            - name: String
            - shows: Show[]
            - seats: Seat[]
        }

        class Seat {
            - seatNumber: String
            - seatType: SeatType
        }

        class Theatre {
            - name: String
            - screens: Screen[]
            - location: Location
        }

        class Location {
            address: String
            city: String
            state: String
            pinCode: String
        }

        class User {
            - name: String
            - email: String
            - location: Location
        }

        class Booking {
            - user: User
            - show: Show
            - bookedSeats: ShowSeat[]
            - status: BookingStatus
            + confirm(PaymentStrategy): Ticket
            + cancel(): void
            + bookSeat(): void
            + selectSeat(): void
            + unselectSeat(): void
        }

        class BookingStatus {
            <<enumeration>>
            INITIATED,
            CONFIRMED,
            CANCELLED,
            EXPIRED
        }

        class Ticket {
            - user: User
            - show: Show
            - seats: ShowSeat[]
            - bookedAt: DateTime
        }

        class PaymentSystem {
            + pay(Ticket, PaymentStrategy): boolean
            + calculateAmount(Ticket): double
        }

        class PaymentStrategy {
            <<interface>>
            + calculateAmount(Ticket): double
            + pay(Ticket): boolean
        }

        class PayViaCreditCard {
            - payMethodType: PaymentMethodType
            - cardNumber: String
            - cvv: int
            - expiryDate: String
            - serviceCharge: double
            + calculateAmount(Ticket): double
            + pay(Ticket): boolean
        }

        class PayViaUPI {
            - payMethodType: PaymentMethodType
            - upiId: String
            - serviceCharge: double
            + calculateAmount(Ticket): double
            + pay(Ticket): boolean
        }

        PayViaCreditCard --o PaymentMethodType
        PayViaUPI --o PaymentMethodType

        class PaymentMethodType {
            <<enumeration>>
            CARD,
            UPI
        }

        class SeatType {
            <<enumeration>>
            SILVER,
            GOLD,
            PLATINUM
        }

        class ScreenType {
            <<enumeration>>
            2D,
            3D,
            4D
        }

        class AudioType {
            <<enumeration>>
            NORMAL,
            DOLBY
        }

        class SeatStatus {
            <<enumeration>>
            AVAILABLE,
            LOCKED,
            BOOKED
        }

        class SeatLockManager {
            - seatLocks: Map<String, ReentrantLock>
            - lockTimestamps: Map<String, LocalDateTime>
            - lockOwners: Map<String, String>
            - LOCK_TIMEOUT_MINUTES: int
            + lockSeat(String, String): boolean
            + releaseLock(String): void
            + validateLock(String, String): boolean
            - isLockExpired(String): boolean
            + cleanupExpiredLocks(): void
        }

        class ReentrantLock {
            <<external>>
            + tryLock(): boolean
            + unlock(): void
            + isHeldByCurrentThread(): boolean
        }

        class LocalDateTime {
            <<external>>
            + now(): LocalDateTime
            + isAfter(LocalDateTime): boolean
            + plusMinutes(int): LocalDateTime
        }
        
        BookingSystem --> Booking
        BookingSystem --o PaymentSystem
        BookingSystem --o SeatLockManager
        SeatLockManager --* ReentrantLock
        SeatLockManager --* LocalDateTime
        Booking --o BookingStatus
        Booking --* User
        Booking --* Show
        Booking --* ShowSeat
        Booking --* Ticket

        Ticket --* ShowSeat
        Ticket --* User
        Ticket --* Show

        Show --* Movie
        Show --* ShowSeat

        ShowSeat --o SeatStatus

        ShowSeat --* Seat

        Seat --o SeatType

        Screen --* Seat
        Screen --* Show
        Screen --o ScreenType
        Screen --o AudioType

        Theatre --* Screen
        Theatre --* Location

        User --* Location

        PaymentSystem --* PaymentStrategy
        PaymentStrategy <|.. PayViaCreditCard
        PaymentStrategy <|.. PayViaUPI
```








