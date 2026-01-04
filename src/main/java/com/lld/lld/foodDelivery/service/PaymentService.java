package com.lld.lld.foodDelivery.service;

import com.lld.lld.foodDelivery.strategy.payment.PaymentStrategy;

/**
 * Payment service using Strategy pattern
 */
public class PaymentService {

    private PaymentStrategy paymentStrategy;

    public PaymentService() {
    }

    public PaymentService(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public boolean processPayment(double amount) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set!");
        }
        return paymentStrategy.processPayment(amount);
    }

    public String getPaymentMethod() {
        if (paymentStrategy == null) {
            return "NONE";
        }
        return paymentStrategy.getPaymentMethod();
    }
}
