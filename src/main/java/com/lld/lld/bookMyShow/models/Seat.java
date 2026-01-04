package com.lld.lld.bookMyShow.models;

import com.lld.lld.bookMyShow.enums.SeatType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seat {
    private String seatNumber;
    private SeatType seatType;
}
