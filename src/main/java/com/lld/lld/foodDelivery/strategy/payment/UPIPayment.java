package com.lld.lld.foodDelivery.strategy.payment;

/**
 * UPI payment strategy
 */
public class UPIPayment implements PaymentStrategy {
    private String upiId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public boolean processPayment(double amount) {
        // Simulate UPI payment processing
        System.out.println("Processing UPI payment of ₹" + amount);
        System.out.println("UPI ID: " + upiId);
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "UPI";
    }
}
