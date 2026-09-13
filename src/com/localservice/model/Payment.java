package com.localservice.model;

/**
 * Payment entity.
 */
public class Payment {
    private int id;
    private int bookingId;
    private double amount;
    private String method; // CASH, UPI, CARD
    private String status; // PENDING, COMPLETED, FAILED, REFUNDED
    private String transactionRef;
    private String paidAt;

    public Payment() {}

    public Payment(int id, int bookingId, double amount, String method, String status) {
        this.id = id;
        this.bookingId = bookingId;
        this.amount = amount;
        this.method = method;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    public String getPaidAt() { return paidAt; }
    public void setPaidAt(String paidAt) { this.paidAt = paidAt; }
}
