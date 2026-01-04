package com.lld.lld.parkingLot.strategy;
import java.time.Duration;
import java.time.LocalDateTime;

import com.lld.lld.parkingLot.enums.PaymentMethodType;
import com.lld.lld.parkingLot.models.ParkingTicket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PayViaCreditCard implements PaymentStrategy {
    private ParkingTicket parkingTicket;
    private final PaymentMethodType paymentMethodType = PaymentMethodType.CARD;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private final Double serviceCharge = 10.0;

    @Override
    public void pay(ParkingTicket parkingTicket) {
        Double amount = calculateFair(parkingTicket);
        System.out.println("Paid: " + amount + "by Card: " + cardNumber);
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
