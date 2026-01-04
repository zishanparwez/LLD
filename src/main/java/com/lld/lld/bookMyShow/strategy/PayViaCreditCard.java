package com.lld.lld.bookMyShow.strategy;

import java.util.List;

import com.lld.lld.bookMyShow.enums.PaymentMethodType;
import com.lld.lld.bookMyShow.models.ShowSeat;
import com.lld.lld.bookMyShow.models.Ticket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayViaCreditCard implements PaymentStrategy {

    private final PaymentMethodType paymentMethodType = PaymentMethodType.CARD;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private final Double serviceCharge = 10.0;
    

    @Override
    public Double calculateAmount(Ticket ticket) {

        List<ShowSeat> seats = ticket.getSeats();
        Double amount = 0.0;
        for(ShowSeat seat: seats) {
            amount += seat.getPrice();
        }
        return amount + serviceCharge;
    }

    @Override
    public boolean pay(Double amount) {
        System.out.println("Paid: " + amount + "by "+ paymentMethodType);
        return true;
    }
    
}
