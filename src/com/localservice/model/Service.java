package com.localservice.model;

/**
 * Service entity — ENCAPSULATION of service details.
 */
public class Service {
    private int id;
    private int providerId;
    private String name;
    private String description;
    private String category;
    private double price;
    private int durationMinutes;
    private boolean active;
    private String providerName; // join field for display

    public Service() {}

    public Service(int id, int providerId, String name, String description,
                   String category, double price, int durationMinutes, boolean active) {
        this.id = id;
        this.providerId = providerId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.active = active;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProviderId() { return providerId; }
    public void setProviderId(int providerId) { this.providerId = providerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }

    /** OVERLOADING: format price with/without currency symbol. */
    public String formatPrice() {
        return "₹" + String.format("%.2f", price);
    }

    public String formatPrice(boolean includeDuration) {
        if (includeDuration) {
            return formatPrice() + " (" + durationMinutes + " min)";
        }
        return formatPrice();
    }
}
