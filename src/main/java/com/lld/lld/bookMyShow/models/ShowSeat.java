package com.lld.lld.bookMyShow.models;

import com.lld.lld.bookMyShow.enums.SeatStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowSeat {
    private Seat seat;
    private SeatStatus seatStatus;
    private Double price;
    
    public boolean isAvailable() {
        return seatStatus.equals(SeatStatus.AVAILABLE);
    }

    public void lock() {
        setSeatStatus(SeatStatus.LOCKED);
    }

    public void book() {
        setSeatStatus(SeatStatus.BOOKED);
    }

    public void release() {
        setSeatStatus(SeatStatus.AVAILABLE);
    }
}
