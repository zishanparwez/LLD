package com.lld.lld.bookMyShow.strategy;

import com.lld.lld.bookMyShow.models.Ticket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSystem {
    private PaymentStrategy paymentStrategy;
    
    public Double calculateAmount(Ticket ticket) {
        return paymentStrategy.calculateAmount(ticket);
    }

    public boolean pay(Double amount) {
        return paymentStrategy.pay(amount);
    }
}
