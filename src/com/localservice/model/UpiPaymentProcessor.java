package com.localservice.model;

/** Concrete processor — POLYMORPHISM. */
public class UpiPaymentProcessor extends PaymentProcessor {
    public UpiPaymentProcessor(double amount) {
        super(amount);
    }

    @Override
    public String getMethodName() {
        return "UPI";
    }

    @Override
    public String process() {
        return generateRef("UPI");
    }
}
