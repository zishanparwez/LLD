package com.lld.lld.bookMyShow.models;

import java.time.LocalDateTime;
import java.util.List;

import com.lld.lld.bookMyShow.enums.BookingStatus;
import com.lld.lld.bookMyShow.enums.SeatStatus;
import com.lld.lld.bookMyShow.strategy.PaymentStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Booking {
    private User user;
    private Show show;
    private List<ShowSeat> bookedSeats;
    private BookingStatus status;

    public Ticket confirm(PaymentStrategy paymentStrategy) {

        selectSeat();
        Ticket ticket = Ticket.builder()
                            .user(user)
                            .show(show)
                            .seats(bookedSeats)
                            .bookedAt(LocalDateTime.now())
                            .build();
        Double amount = paymentStrategy.calculateAmount(ticket);
        if(paymentStrategy.pay(amount)) {
            bookSeat();
            setStatus(BookingStatus.CONFIRMED);
            return ticket;
        }
        unselectSeat();
        setStatus(BookingStatus.CANCELLED);
        return null;
    }

    public void cancel() {
        for(ShowSeat bookedSeat: bookedSeats) {
            bookedSeat.release();
        }
        setStatus(BookingStatus.CANCELLED);
    }

    public void bookSeat() {
        for(ShowSeat seat: bookedSeats) {
            if(seat.getSeatStatus().equals(SeatStatus.LOCKED)) {
                seat.book();
            }
        }
    }

    public void selectSeat() {
        for(ShowSeat seat: bookedSeats) {
            if(seat.isAvailable()) {
                seat.lock();
            }
        }
    }

    public void unselectSeat() {
        for(ShowSeat seat: bookedSeats) {
            if(seat.getSeatStatus().equals(SeatStatus.LOCKED)) {
                seat.release();
            }
        }
    }
}
