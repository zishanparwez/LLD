package com.lld.lld.foodDelivery.strategy.payment;

/**
 * Cash on Delivery payment strategy
 */
public class CashOnDelivery implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Cash on Delivery selected for ₹" + amount);
        System.out.println("Payment will be collected upon delivery.");
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "CASH_ON_DELIVERY";
    }
}
