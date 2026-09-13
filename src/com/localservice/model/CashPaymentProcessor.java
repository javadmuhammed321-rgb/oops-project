package com.localservice.model;

/** Concrete processor — POLYMORPHISM (runtime). */
public class CashPaymentProcessor extends PaymentProcessor {
    public CashPaymentProcessor(double amount) {
        super(amount);
    }

    @Override
    public String getMethodName() {
        return "CASH";
    }

    @Override
    public String process() {
        return generateRef("CASH");
    }
}
