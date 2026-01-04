package com.lld.lld.bookMyShow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.lld.lld.bookMyShow.enums.SeatStatus;
import com.lld.lld.bookMyShow.enums.SeatType;
import com.lld.lld.bookMyShow.models.Booking;
import com.lld.lld.bookMyShow.models.Location;
import com.lld.lld.bookMyShow.models.Movie;
import com.lld.lld.bookMyShow.models.Seat;
import com.lld.lld.bookMyShow.models.Show;
import com.lld.lld.bookMyShow.models.ShowSeat;
import com.lld.lld.bookMyShow.models.Ticket;
import com.lld.lld.bookMyShow.models.User;
import com.lld.lld.bookMyShow.strategy.PayViaUPI;
import com.lld.lld.bookMyShow.strategy.PaymentSystem;

public class ConcurrencyDemo {
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("=== BookMyShow Seat Lock Manager Demo ===");
        System.out.println("This demo shows how the Seat Lock Manager prevents double booking");
        
        // Setup basic data
        Show show = setupShow();
        PaymentSystem paymentSystem = new PaymentSystem();
        PayViaUPI upi = new PayViaUPI();
        upi.setUpiId("demo@upi");
        paymentSystem.setPaymentStrategy(upi);
        
        BookingSystem bookingSystem = new BookingSystem(paymentSystem);
        
        // Create two users trying to book the same seat
        User user1 = createUser("user1@example.com", "Alice");
        User user2 = createUser("user2@example.com", "Bob");
        
        // Get the same seat for both users
        ShowSeat seat1 = show.getShowSeats().get(0); // S1
        ShowSeat seat2 = show.getShowSeats().get(0); // Same seat S1
        
        System.out.println("Initial seat status: " + seat1.getSeatStatus());
        System.out.println("\n--- Scenario 1: Two users trying to book the same seat ---");
        
        // Simulate concurrent booking attempts
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("🔒 User 1 (Alice) attempting to book seat S1...");
                Booking booking1 = bookingSystem.createBooking(user1, show, Arrays.asList(seat1));
                System.out.println("✅ User 1 successfully locked seat S1");
                
                // Simulate some processing time
                Thread.sleep(100);
                
                Ticket ticket1 = bookingSystem.confirmBooking(booking1);
                return "✅ User 1 booking successful: " + (ticket1 != null);
            } catch (Exception e) {
                return "❌ User 1 booking failed: " + e.getMessage();
            }
        }, executor);
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("🔒 User 2 (Bob) attempting to book seat S1...");
                Booking booking2 = bookingSystem.createBooking(user2, show, Arrays.asList(seat2));
                System.out.println("✅ User 2 successfully locked seat S1");
                
                // Simulate some processing time
                Thread.sleep(50);
                
                Ticket ticket2 = bookingSystem.confirmBooking(booking2);
                return "✅ User 2 booking successful: " + (ticket2 != null);
            } catch (Exception e) {
                return "❌ User 2 booking failed: " + e.getMessage();
            }
        }, executor);
        
        // Wait for both bookings to complete
        String result1 = future1.get();
        String result2 = future2.get();
        
        System.out.println("\n=== Results ===");
        System.out.println(result1);
        System.out.println(result2);
        System.out.println("Final seat status: " + seat1.getSeatStatus());
        
        // Cleanup
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        // Demonstrate successful booking with different seats
        System.out.println("\n--- Scenario 2: Users booking different seats ---");
        try {
            ShowSeat seat3 = show.getShowSeats().get(1); // S2
            ShowSeat seat4 = show.getShowSeats().get(2); // S3
            
            System.out.println("🔒 User 1 (Alice) booking seat S2...");
            Booking booking3 = bookingSystem.createBooking(user1, show, Arrays.asList(seat3));
            System.out.println("🔒 User 2 (Bob) booking seat S3...");
            Booking booking4 = bookingSystem.createBooking(user2, show, Arrays.asList(seat4));
            
            Ticket ticket3 = bookingSystem.confirmBooking(booking3);
            Ticket ticket4 = bookingSystem.confirmBooking(booking4);
            
            System.out.println("✅ User 1 booking seat S2: " + (ticket3 != null));
            System.out.println("✅ User 2 booking seat S3: " + (ticket4 != null));
            
        } catch (Exception e) {
            System.out.println("❌ Error in different seat booking: " + e.getMessage());
        }
        
        // Cleanup expired locks
        bookingSystem.cleanupExpiredLocks();
        System.out.println("\n🧹 Cleaned up expired locks");
    }
    
    private static Show setupShow() {
        // Create movie
        Movie movie = Movie.builder()
                .name("Avengers: Endgame")
                .rating(9)
                .languages(Arrays.asList("EN"))
                .build();
        
        // Create show
        Show show = new Show();
        show.setMovie(movie);
        show.setStartTime("19:00");
        show.setEndTime("22:00");
        show.setLanguage("EN");
        
        // Create seats
        List<ShowSeat> showSeats = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Seat seat = new Seat();
            seat.setSeatNumber("S" + i);
            seat.setSeatType(SeatType.SILVER);
            
            ShowSeat showSeat = new ShowSeat();
            showSeat.setSeat(seat);
            showSeat.setSeatStatus(SeatStatus.AVAILABLE);
            showSeat.setPrice(150.0);
            
            showSeats.add(showSeat);
        }
        
        show.setShowSeats(showSeats);
        return show;
    }
    
    private static User createUser(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        
        Location location = new Location();
        location.setCity("Mumbai");
        location.setState("MH");
        user.setLocation(location);
        
        return user;
    }
}
