package com.localservice.model;

/**
 * ServiceProvider — INHERITANCE from Person.
 * Demonstrates METHOD OVERRIDING for displayInfo and getRole.
 */
public class ServiceProvider extends Person {
    private int providerId;
    private String businessName;
    private String description;
    private double rating;
    private boolean available;

    public ServiceProvider() {
        super();
    }

    public ServiceProvider(int id, String name, String email, String password, String phone,
                           int providerId, String businessName, String description,
                           double rating, boolean available) {
        super(id, name, email, password, phone);
        this.providerId = providerId;
        this.businessName = businessName;
        this.description = description;
        this.rating = rating;
        this.available = available;
    }

    public int getProviderId() { return providerId; }
    public void setProviderId(int providerId) { this.providerId = providerId; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String getRole() {
        return "PROVIDER";
    }

    /** OVERRIDING displayInfo for provider-specific output. */
    @Override
    public String displayInfo() {
        return "Provider: " + businessName + " (Rating: " + rating + ") — "
                + (available ? "Available" : "Unavailable") + " | Contact: " + getName();
    }
}
