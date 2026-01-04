package com.lld.lld.bookMyShow.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Show {
    private Movie movie;
    private String startTime;
    private String endTime;
    private String language;
    private List<ShowSeat> showSeats;

    public List<ShowSeat> getAvailableSeats() {
        List<ShowSeat> availableSeats = new ArrayList<>();
        for(ShowSeat seat: showSeats) {
            if(seat.isAvailable()) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }
}
