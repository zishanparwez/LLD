package com.lld.lld.parkingLot.strategy;

import com.lld.lld.parkingLot.models.ParkingTicket;

public interface PaymentStrategy {
    public void pay(ParkingTicket parkingTicket);
    public Double calculateFair(ParkingTicket parkingTicket);
}
