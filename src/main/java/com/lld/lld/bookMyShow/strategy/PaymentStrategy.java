package com.lld.lld.bookMyShow.strategy;

import com.lld.lld.bookMyShow.models.Ticket;

public interface PaymentStrategy {
    public Double calculateAmount(Ticket ticket);
    public boolean pay(Double amount);
}
