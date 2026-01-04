package com.lld.lld.parkingLot.strategy;

import java.time.Duration;
import java.time.LocalDateTime;

import com.lld.lld.parkingLot.enums.PaymentMethodType;
import com.lld.lld.parkingLot.models.ParkingTicket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayViaUPI implements PaymentStrategy {
    private final PaymentMethodType paymentMethodType = PaymentMethodType.UPI;
    private String upiId;
    private final Double serviceCharge = 0.0;

    @Override
    public void pay(ParkingTicket parkingTicket) {
        Double amount = calculateFair(parkingTicket);
        System.out.println("Paid: " + amount + "by UPI: " + upiId);
    }

    @Override
    public Double calculateFair(ParkingTicket parkingTicket) {
        LocalDateTime entryTime = parkingTicket.getEntryTime();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(entryTime, now);
        long hours = duration.toHours();
        return hours * 10.0 + serviceCharge;
    }
}
