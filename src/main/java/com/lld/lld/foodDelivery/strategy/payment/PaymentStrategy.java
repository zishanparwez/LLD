package com.lld.lld.foodDelivery.strategy.payment;

/**
 * Strategy interface for payment processing
 */
public interface PaymentStrategy {
    boolean processPayment(double amount);

    String getPaymentMethod();
}
