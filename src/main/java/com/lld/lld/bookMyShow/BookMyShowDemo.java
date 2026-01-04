package com.lld.lld.bookMyShow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.lld.lld.bookMyShow.enums.AudioType;
import com.lld.lld.bookMyShow.enums.ScreenType;
import com.lld.lld.bookMyShow.enums.SeatStatus;
import com.lld.lld.bookMyShow.enums.SeatType;
import com.lld.lld.bookMyShow.models.Booking;
import com.lld.lld.bookMyShow.models.Location;
import com.lld.lld.bookMyShow.models.Movie;
import com.lld.lld.bookMyShow.models.Screen;
import com.lld.lld.bookMyShow.models.Seat;
import com.lld.lld.bookMyShow.models.Show;
import com.lld.lld.bookMyShow.models.ShowSeat;
import com.lld.lld.bookMyShow.models.Theatre;
import com.lld.lld.bookMyShow.models.Ticket;
import com.lld.lld.bookMyShow.models.User;
import com.lld.lld.bookMyShow.strategy.PayViaCreditCard;
import com.lld.lld.bookMyShow.strategy.PayViaUPI;
import com.lld.lld.bookMyShow.strategy.PaymentSystem;

public class BookMyShowDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Setup location and theatre
        Location location = new Location();
        location.setAddress("123 MG Road");
        location.setCity("Bengaluru");
        location.setState("KA");
        location.setPincode("560001");

        Theatre theatre = new Theatre();
        theatre.setName("PVR Orion Mall");
        theatre.setLocation(location);

        // Setup movie
        Movie movie = Movie.builder()
                        .name("Interstellar")
                        .rating(9)
                        .languages(Arrays.asList("EN", "HI"))
                        .build();

        // Setup screen
        Screen screen = new Screen();
        screen.setName("Screen 1");
        screen.setAudioType(AudioType.DOLBY);
        screen.setScreenType(ScreenType.S3D);

        // Setup seats for the screen
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 5; i++) { // Silver
            Seat seat = new Seat();
            seat.setSeatNumber("S" + i);
            seat.setSeatType(SeatType.SILVER);
            seats.add(seat);
        }
        for (int i = 1; i <= 5; i++) { // Gold
            Seat seat = new Seat();
            seat.setSeatNumber("G" + i);
            seat.setSeatType(SeatType.GOLD);
            seats.add(seat);
        }
        for (int i = 1; i <= 2; i++) { // Platinum
            Seat seat = new Seat();
            seat.setSeatNumber("P" + i);
            seat.setSeatType(SeatType.PLATINUM);
            seats.add(seat);
        }
        screen.setSeats(seats);

        // Setup a show
        Show show = new Show();
        show.setMovie(movie);
        show.setStartTime("18:00");
        show.setEndTime("21:00");
        show.setLanguage("EN");

        // Setup show seats with pricing and availability
        List<ShowSeat> showSeats = new ArrayList<>();
        for (Seat seat : seats) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setSeat(seat);
            showSeat.setSeatStatus(SeatStatus.AVAILABLE);
            switch (seat.getSeatType()) {
                case SILVER:
                    showSeat.setPrice(150.0);
                    break;
                case GOLD:
                    showSeat.setPrice(250.0);
                    break;
                case PLATINUM:
                    showSeat.setPrice(400.0);
                    break;
            }
            showSeats.add(showSeat);
        }
        show.setShowSeats(showSeats);

        // Attach show to screen and screen to theatre
        screen.setShows(Arrays.asList(show));
        theatre.setScreens(Arrays.asList(screen));

        System.out.println("=== BookMyShow Demo with Seat Lock Manager ===");
        System.out.println("Movie: " + movie.getName() + " | Show: " + show.getStartTime());
        System.out.println("Theatre: " + theatre.getName() + " | Screen: " + screen.getName());
        System.out.println("Available seats: " + show.getAvailableSeats().size());
        
        // Create users
        User user1 = createUser("alice@example.com", "Alice");
        User user2 = createUser("bob@example.com", "Bob");
        user1.setLocation(location);
        user2.setLocation(location);

        PaymentSystem paymentSystem = new PaymentSystem();
        PayViaUPI upi = new PayViaUPI();
        upi.setUpiId("demo@upi");
        paymentSystem.setPaymentStrategy(upi);
        
        BookingSystem bookingSystem = new BookingSystem(paymentSystem);

        // Test 1: Normal booking flow
        System.out.println("\n--- Test 1: Normal Booking Flow ---");
        List<ShowSeat> seatsToBook1 = Arrays.asList(showSeats.get(0), showSeats.get(5)); // S1, G1
        System.out.println("🔒 Alice booking seats S1 and G1...");
        
        try {
            Booking booking1 = bookingSystem.createBooking(user1, show, seatsToBook1);
            System.out.println("✅ Seats locked successfully");
            
            Ticket ticket1 = bookingSystem.confirmBooking(booking1);
            System.out.println("✅ Booking confirmed! Ticket: " + (ticket1 != null));
            System.out.println("💰 Total amount: ₹" + (150.0 + 250.0));
        } catch (Exception e) {
            System.out.println("❌ Booking failed: " + e.getMessage());
        }

        // Test 2: Concurrent booking attempt (same seats)
        System.out.println("\n--- Test 2: Concurrent Booking (Same Seats) ---");
        List<ShowSeat> seatsToBook2 = Arrays.asList(showSeats.get(0), showSeats.get(1)); // S1, S2
        System.out.println("🔒 Alice trying to book S1 and S2...");
        System.out.println("🔒 Bob trying to book S1 and S2...");
        
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Booking booking = bookingSystem.createBooking(user1, show, seatsToBook2);
                Thread.sleep(100); // Simulate processing
                Ticket ticket = bookingSystem.confirmBooking(booking);
                return "✅ Alice booking successful: " + (ticket != null);
            } catch (Exception e) {
                return "❌ Alice booking failed: " + e.getMessage();
            }
        }, executor);
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Booking booking = bookingSystem.createBooking(user2, show, seatsToBook2);
                Thread.sleep(50); // Simulate processing
                Ticket ticket = bookingSystem.confirmBooking(booking);
                return "✅ Bob booking successful: " + (ticket != null);
            } catch (Exception e) {
                return "❌ Bob booking failed: " + e.getMessage();
            }
        }, executor);
        
        String result1 = future1.get();
        String result2 = future2.get();
        
        System.out.println(result1);
        System.out.println(result2);

        // Test 3: Successful booking with different seats
        System.out.println("\n--- Test 3: Booking Different Seats ---");
        List<ShowSeat> seatsToBook3 = Arrays.asList(showSeats.get(2), showSeats.get(7)); // S3, G3
        List<ShowSeat> seatsToBook4 = Arrays.asList(showSeats.get(3), showSeats.get(8)); // S4, G4
        
        try {
            System.out.println("🔒 Alice booking S3 and G3...");
            Booking booking3 = bookingSystem.createBooking(user1, show, seatsToBook3);
            Ticket ticket3 = bookingSystem.confirmBooking(booking3);
            System.out.println("✅ Alice booking successful: " + (ticket3 != null));
            
            System.out.println("🔒 Bob booking S4 and G4...");
            Booking booking4 = bookingSystem.createBooking(user2, show, seatsToBook4);
            Ticket ticket4 = bookingSystem.confirmBooking(booking4);
            System.out.println("✅ Bob booking successful: " + (ticket4 != null));
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

        // Test 4: Payment method switching
        System.out.println("\n--- Test 4: Different Payment Methods ---");
        List<ShowSeat> seatsToBook5 = Arrays.asList(showSeats.get(4)); // S5
        
        try {
            System.out.println("🔒 Alice booking S5 with Credit Card...");
            Booking booking5 = bookingSystem.createBooking(user1, show, seatsToBook5);
            
            // Switch to credit card payment
            PayViaCreditCard card = new PayViaCreditCard();
            card.setCardNumber("4111111111111111");
            card.setCvv("123");
            card.setExpiryDate("12/30");
            paymentSystem.setPaymentStrategy(card);
            
            Ticket ticket5 = bookingSystem.confirmBooking(booking5);
            System.out.println("✅ Credit card booking successful: " + (ticket5 != null));
            System.out.println("💰 Amount with service charge: ₹" + (150.0 + 10.0));
            
        } catch (Exception e) {
            System.out.println("❌ Credit card booking failed: " + e.getMessage());
        }

        // Cleanup
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        bookingSystem.cleanupExpiredLocks();
        
        System.out.println("\n🧹 Cleaned up expired locks");
        System.out.println("\n=== Demo Complete ===");
        System.out.println("Final available seats: " + show.getAvailableSeats().size());
    }
    
    private static User createUser(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        return user;
    }
}
