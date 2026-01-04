package com.lld.lld.parkingLot.models;

import com.lld.lld.parkingLot.strategy.PaymentStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSystem {
    private ParkingTicket parkingTicket;
    private PaymentStrategy paymentStrategy;

    public Double calculateFair(ParkingTicket parkingTicket) {
        return paymentStrategy.calculateFair(parkingTicket);
    }

    public void pay(ParkingTicket parkingTicket) {
        paymentStrategy.pay(parkingTicket);
    }
}
