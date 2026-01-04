package com.lld.lld.meetingRoomBooking;

import com.lld.lld.meetingRoomBooking.concurrency.MeetingRoomLockManager;
import com.lld.lld.meetingRoomBooking.enums.BookingStatus;
import com.lld.lld.meetingRoomBooking.models.Booking;
import com.lld.lld.meetingRoomBooking.models.Employee;
import com.lld.lld.meetingRoomBooking.models.MeetingRoom;
import com.lld.lld.meetingRoomBooking.repository.BookingRepository;
import com.lld.lld.meetingRoomBooking.repository.MeetingRoomRepository;
import com.lld.lld.meetingRoomBooking.service.BookingService;
import com.lld.lld.meetingRoomBooking.service.RSVPService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Concurrency Test Demo for Meeting Room Booking System
 * 
 * This class demonstrates and tests concurrent booking scenarios:
 * 1. Multiple employees trying to book the same room at the same time
 * 2. Multiple employees booking different rooms concurrently
 * 3. Stress test with many concurrent booking attempts
 */
public class ConcurrencyTestDemo {
    
    private final BookingRepository bookingRepository;
    private final MeetingRoomRepository meetingRoomRepository;
    private final MeetingRoomLockManager roomLockManager;
    private final BookingService bookingService;
    private final RSVPService rsvpService;
    
    private final AtomicInteger successfulBookings = new AtomicInteger(0);
    private final AtomicInteger failedBookings = new AtomicInteger(0);
    private final AtomicInteger bookingIdCounter = new AtomicInteger(0);
    
    public ConcurrencyTestDemo() {
        this.bookingRepository = new BookingRepository();
        this.meetingRoomRepository = new MeetingRoomRepository();
        this.roomLockManager = new MeetingRoomLockManager();
        this.bookingService = new BookingService(bookingRepository, meetingRoomRepository, roomLockManager);
        this.rsvpService = new RSVPService();
    }
    
    public static void main(String[] args) throws InterruptedException {
        ConcurrencyTestDemo demo = new ConcurrencyTestDemo();
        
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   MEETING ROOM BOOKING - CONCURRENCY TEST DEMO             ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // Test 1: Multiple employees race to book the same room
        demo.testConcurrentBookingSameRoom();
        
        // Test 2: Multiple employees book different rooms concurrently
        demo.testConcurrentBookingDifferentRooms();
        
        // Test 3: Stress test with many concurrent requests
        demo.testStressConcurrency();
        
        System.out.println("\n════════════════════════════════════════════════════════════════");
        System.out.println("                    ALL TESTS COMPLETED                          ");
        System.out.println("════════════════════════════════════════════════════════════════");
    }
    
    /**
     * Test 1: Multiple employees trying to book the SAME room at the SAME time slot
     * Expected: Only ONE booking should succeed
     */
    private void testConcurrentBookingSameRoom() throws InterruptedException {
        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 1: Multiple employees racing to book the SAME room        │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        
        resetCounters();
        
        // Create a single room
        MeetingRoom conferenceRoom = new MeetingRoom();
        conferenceRoom.setRoomId("conf-room-1");
        conferenceRoom.setRoomNumber("Conference Room A");
        conferenceRoom.setBookingStatus(BookingStatus.AVAILABLE);
        meetingRoomRepository.addMeetingRoom(conferenceRoom);
        
        // Create multiple employees
        int numberOfEmployees = 5;
        List<Employee> employees = new ArrayList<>();
        for (int i = 1; i <= numberOfEmployees; i++) {
            Employee emp = Employee.builder()
                    .employeeId("emp-" + i)
                    .name("Employee-" + i)
                    .rsvpService(rsvpService)
                    .build();
            employees.add(emp);
        }
        
        // Define time slot
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1).withHour(11).withMinute(0);
        
        System.out.println("  → " + numberOfEmployees + " employees trying to book '" + 
                conferenceRoom.getRoomNumber() + "' at " + startTime.toLocalTime() + "-" + endTime.toLocalTime());
        
        // Use CountDownLatch to synchronize thread start
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfEmployees);
        
        ExecutorService executor = Executors.newFixedThreadPool(numberOfEmployees);
        
        for (Employee emp : employees) {
            executor.submit(() -> {
                try {
                    startLatch.await(); // Wait for all threads to be ready
                    
                    Booking booking = Booking.builder()
                            .bookingId("booking-" + bookingIdCounter.incrementAndGet())
                            .meetingRoom(conferenceRoom)
                            .bookedBy(emp)
                            .bookingStatus(BookingStatus.PENDING)
                            .bookingAgenda("Meeting by " + emp.getName())
                            .startTime(startTime)
                            .endTime(endTime)
                            .build();
                    
                    boolean success = bookingService.bookRoom(booking);
                    if (success) {
                        successfulBookings.incrementAndGet();
                        System.out.println("  ✓ " + emp.getName() + " successfully booked the room!");
                    } else {
                        failedBookings.incrementAndGet();
                        System.out.println("  ✗ " + emp.getName() + " - booking failed (room not available)");
                    }
                } catch (Exception e) {
                    failedBookings.incrementAndGet();
                    System.out.println("  ✗ " + emp.getName() + " - booking failed: " + e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            });
        }
        
        // Start all threads simultaneously
        startLatch.countDown();
        
        // Wait for all threads to complete
        doneLatch.await();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        // Verify results
        System.out.println("\n  ─── Results ───");
        System.out.println("  Successful bookings: " + successfulBookings.get());
        System.out.println("  Failed bookings: " + failedBookings.get());
        
        List<Booking> roomBookings = bookingRepository.listBookingsforRoom(conferenceRoom.getRoomId());
        System.out.println("  Actual bookings in repository: " + roomBookings.size());
        
        if (successfulBookings.get() == 1 && roomBookings.size() == 1) {
            System.out.println("  ✓ TEST PASSED: Only one booking succeeded as expected!\n");
        } else {
            System.out.println("  ✗ TEST FAILED: Expected exactly 1 successful booking!\n");
        }
    }
    
    /**
     * Test 2: Multiple employees booking DIFFERENT rooms concurrently
     * Expected: All bookings should succeed
     */
    private void testConcurrentBookingDifferentRooms() throws InterruptedException {
        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 2: Multiple employees booking DIFFERENT rooms              │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        
        resetCounters();
        
        int numberOfRooms = 5;
        List<MeetingRoom> rooms = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        
        // Create rooms and employees
        for (int i = 1; i <= numberOfRooms; i++) {
            MeetingRoom room = new MeetingRoom();
            room.setRoomId("room-multi-" + i);
            room.setRoomNumber("Room " + (200 + i));
            room.setBookingStatus(BookingStatus.AVAILABLE);
            meetingRoomRepository.addMeetingRoom(room);
            rooms.add(room);
            
            Employee emp = Employee.builder()
                    .employeeId("emp-multi-" + i)
                    .name("User-" + i)
                    .rsvpService(rsvpService)
                    .build();
            employees.add(emp);
        }
        
        LocalDateTime startTime = LocalDateTime.now().plusDays(2).withHour(14).withMinute(0);
        LocalDateTime endTime = LocalDateTime.now().plusDays(2).withHour(15).withMinute(0);
        
        System.out.println("  → " + numberOfRooms + " employees booking " + numberOfRooms + " different rooms simultaneously");
        
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfRooms);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfRooms);
        
        for (int i = 0; i < numberOfRooms; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    
                    Booking booking = Booking.builder()
                            .bookingId("booking-multi-" + bookingIdCounter.incrementAndGet())
                            .meetingRoom(rooms.get(index))
                            .bookedBy(employees.get(index))
                            .bookingStatus(BookingStatus.PENDING)
                            .bookingAgenda("Meeting in " + rooms.get(index).getRoomNumber())
                            .startTime(startTime)
                            .endTime(endTime)
                            .build();
                    
                    boolean success = bookingService.bookRoom(booking);
                    if (success) {
                        successfulBookings.incrementAndGet();
                        System.out.println("  ✓ " + employees.get(index).getName() + " booked " + rooms.get(index).getRoomNumber());
                    } else {
                        failedBookings.incrementAndGet();
                        System.out.println("  ✗ " + employees.get(index).getName() + " failed to book " + rooms.get(index).getRoomNumber());
                    }
                } catch (Exception e) {
                    failedBookings.incrementAndGet();
                    System.out.println("  ✗ " + employees.get(index).getName() + " error: " + e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            });
        }
        
        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        System.out.println("\n  ─── Results ───");
        System.out.println("  Successful bookings: " + successfulBookings.get());
        System.out.println("  Failed bookings: " + failedBookings.get());
        
        if (successfulBookings.get() == numberOfRooms) {
            System.out.println("  ✓ TEST PASSED: All " + numberOfRooms + " concurrent bookings succeeded!\n");
        } else {
            System.out.println("  ✗ TEST FAILED: Expected " + numberOfRooms + " successful bookings!\n");
        }
    }
    
    /**
     * Test 3: Stress test with many concurrent booking attempts on limited rooms
     */
    private void testStressConcurrency() throws InterruptedException {
        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 3: Stress test - Many requests, limited rooms              │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        
        resetCounters();
        
        int numberOfRooms = 3;
        int numberOfEmployees = 20;
        
        List<MeetingRoom> rooms = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        
        // Create limited rooms
        for (int i = 1; i <= numberOfRooms; i++) {
            MeetingRoom room = new MeetingRoom();
            room.setRoomId("stress-room-" + i);
            room.setRoomNumber("Stress Room " + i);
            room.setBookingStatus(BookingStatus.AVAILABLE);
            meetingRoomRepository.addMeetingRoom(room);
            rooms.add(room);
        }
        
        // Create many employees
        for (int i = 1; i <= numberOfEmployees; i++) {
            Employee emp = Employee.builder()
                    .employeeId("stress-emp-" + i)
                    .name("StressUser-" + i)
                    .rsvpService(rsvpService)
                    .build();
            employees.add(emp);
        }
        
        LocalDateTime startTime = LocalDateTime.now().plusDays(3).withHour(10).withMinute(0);
        LocalDateTime endTime = LocalDateTime.now().plusDays(3).withHour(11).withMinute(0);
        
        System.out.println("  → " + numberOfEmployees + " employees competing for " + numberOfRooms + " rooms");
        System.out.println("  → Same time slot: " + startTime.toLocalTime() + " - " + endTime.toLocalTime());
        
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfEmployees);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfEmployees);
        
        for (int i = 0; i < numberOfEmployees; i++) {
            final int empIndex = i;
            final int roomIndex = i % numberOfRooms; // Each employee picks a room in round-robin
            
            executor.submit(() -> {
                try {
                    startLatch.await();
                    
                    // Add small random delay to simulate real-world scenarios
                    Thread.sleep(ThreadLocalRandom.current().nextInt(10));
                    
                    Booking booking = Booking.builder()
                            .bookingId("stress-booking-" + bookingIdCounter.incrementAndGet())
                            .meetingRoom(rooms.get(roomIndex))
                            .bookedBy(employees.get(empIndex))
                            .bookingStatus(BookingStatus.PENDING)
                            .bookingAgenda("Stress Meeting " + empIndex)
                            .startTime(startTime)
                            .endTime(endTime)
                            .build();
                    
                    boolean success = bookingService.bookRoom(booking);
                    if (success) {
                        successfulBookings.incrementAndGet();
                    } else {
                        failedBookings.incrementAndGet();
                    }
                } catch (Exception e) {
                    failedBookings.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }
        
        long startTimeMs = System.currentTimeMillis();
        startLatch.countDown();
        doneLatch.await();
        long endTimeMs = System.currentTimeMillis();
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        System.out.println("\n  ─── Results ───");
        System.out.println("  Time taken: " + (endTimeMs - startTimeMs) + " ms");
        System.out.println("  Successful bookings: " + successfulBookings.get());
        System.out.println("  Failed bookings: " + failedBookings.get());
        System.out.println("  Total requests: " + (successfulBookings.get() + failedBookings.get()));
        
        // Verify each room has at most 1 booking for this time slot
        int totalRoomBookings = 0;
        for (MeetingRoom room : rooms) {
            List<Booking> bookings = bookingRepository.listBookingsforRoom(room.getRoomId());
            int overlappingBookings = (int) bookings.stream()
                    .filter(b -> b.getStartTime().equals(startTime) && b.getEndTime().equals(endTime))
                    .count();
            totalRoomBookings += overlappingBookings;
            System.out.println("  " + room.getRoomNumber() + ": " + overlappingBookings + " booking(s)");
        }
        
        if (successfulBookings.get() <= numberOfRooms && totalRoomBookings == successfulBookings.get()) {
            System.out.println("  ✓ TEST PASSED: No double bookings detected!\n");
        } else {
            System.out.println("  ✗ TEST FAILED: Concurrency issue detected!\n");
        }
    }
    
    private void resetCounters() {
        successfulBookings.set(0);
        failedBookings.set(0);
    }
}

