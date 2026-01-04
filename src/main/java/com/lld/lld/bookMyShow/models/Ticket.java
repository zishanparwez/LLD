package com.lld.lld.bookMyShow.models;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Ticket {
    private User user;
    private Show show;
    private List<ShowSeat> seats;
    private LocalDateTime bookedAt;
}
