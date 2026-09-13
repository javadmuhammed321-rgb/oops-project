package com.localservice.model;

/** Concrete processor — POLYMORPHISM. */
public class CardPaymentProcessor extends PaymentProcessor {
    public CardPaymentProcessor(double amount) {
        super(amount);
    }

    @Override
    public String getMethodName() {
        return "CARD";
    }

    @Override
    public String process() {
        return generateRef("CARD");
    }
}
