package com.localservice.model;

/**
 * Abstract PaymentProcessor — ABSTRACTION.
 * Concrete processors (Cash, UPI, Card) demonstrate POLYMORPHISM.
 */
public abstract class PaymentProcessor {
    protected double amount;

    public PaymentProcessor(double amount) {
        this.amount = amount;
    }

    public abstract String getMethodName();
    public abstract String process();

    /** Common helper used by all processors. */
    protected String generateRef(String prefix) {
        return prefix + "-" + System.currentTimeMillis();
    }
}
