package com.localservice.model;

/**
 * Abstract base class demonstrating ABSTRACTION and ENCAPSULATION.
 * All user types inherit from Person.
 */
public abstract class Person {
    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;

    public Person() {}

    public Person(int id, String name, String email, String password, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    // Encapsulation: private fields with getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    /** Abstract method — subclasses must implement (ABSTRACTION). */
    public abstract String getRole();

    /** Display info — can be overridden (POLYMORPHISM via overriding). */
    public String displayInfo() {
        return "Person: " + name + " (" + email + ")";
    }

    /**
     * Method OVERLOADING — same method name, different parameters.
     */
    public String greet() {
        return "Hello, " + name + "!";
    }

    public String greet(String title) {
        return "Hello, " + title + " " + name + "!";
    }

    public String greet(String title, boolean formal) {
        if (formal) {
            return "Good day, " + title + " " + name + ". Welcome to Local Service Booking.";
        }
        return greet(title);
    }
}
