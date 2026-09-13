package com.localservice.model;

/**
 * Booking entity with emergency flag support.
 */
public class Booking {
    private int id;
    private int customerId;
    private int serviceId;
    private int providerId;
    private String bookingDate;
    private String bookingTime;
    private String status; // PENDING, CONFIRMED, COMPLETED, CANCELLED
    private boolean emergency;
    private String notes;
    private String createdAt;
    // Display join fields
    private String customerName;
    private String serviceName;
    private String providerName;
    private double servicePrice;

    public Booking() {}

    public Booking(int id, int customerId, int serviceId, int providerId,
                   String bookingDate, String bookingTime, String status,
                   boolean emergency, String notes) {
        this.id = id;
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.providerId = providerId;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.status = status;
        this.emergency = emergency;
        this.notes = notes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getProviderId() { return providerId; }
    public void setProviderId(int providerId) { this.providerId = providerId; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public String getBookingTime() { return bookingTime; }
    public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isEmergency() { return emergency; }
    public void setEmergency(boolean emergency) { this.emergency = emergency; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }

    public double getServicePrice() { return servicePrice; }
    public void setServicePrice(double servicePrice) { this.servicePrice = servicePrice; }
}
