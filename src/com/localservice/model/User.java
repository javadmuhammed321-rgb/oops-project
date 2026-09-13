package com.localservice.model;

/**
 * User class — INHERITANCE from Person.
 * Represents Customer / Admin roles; providers use ServiceProvider.
 */
public class User extends Person {
    private String role;   // CUSTOMER, ADMIN
    private String address;

    public User() {
        super();
    }

    public User(int id, String name, String email, String password, String phone, String role, String address) {
        super(id, name, email, password, phone);
        this.role = role;
        this.address = address;
    }

    public void setRole(String role) { this.role = role; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String getRole() {
        return role;
    }

    /** Method OVERRIDING — custom display for User. */
    @Override
    public String displayInfo() {
        return "User [" + role + "]: " + getName() + " | " + getEmail() + " | " + address;
    }
}
