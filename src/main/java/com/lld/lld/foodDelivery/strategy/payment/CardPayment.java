package com.lld.lld.foodDelivery.strategy.payment;

/**
 * Credit/Debit Card payment strategy
 */
public class CardPayment implements PaymentStrategy {
    private String cardNumber;
    private String expiryDate;

    public CardPayment(String cardNumber, String expiryDate) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean processPayment(double amount) {
        // Simulate card payment processing
        System.out.println("Processing card payment of ₹" + amount);
        System.out.println("Card: ****" + cardNumber.substring(cardNumber.length() - 4));
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "CARD";
    }
}
