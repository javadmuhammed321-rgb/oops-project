package com.localservice.service;

import com.localservice.dao.*;
import com.localservice.model.*;

import java.util.List;

public class AdminService {
    private final UserDAO userDAO = new UserDAO();
    private final ProviderDAO providerDAO = new ProviderDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    public List<User> allUsers() throws Exception {
        return userDAO.findAll();
    }

    public List<ServiceProvider> allProviders() throws Exception {
        return providerDAO.findAll();
    }

    public List<Service> allServices() throws Exception {
        return serviceDAO.findAll();
    }

    public List<Booking> allBookings() throws Exception {
        return bookingDAO.findAll();
    }

    public boolean deleteUser(int id) throws Exception {
        return userDAO.delete(id);
    }

    public boolean setProviderAvailable(int providerId, boolean available) throws Exception {
        return providerDAO.setAvailable(providerId, available);
    }

    public boolean updateBookingStatus(int bookingId, String status) throws Exception {
        return bookingDAO.updateStatus(bookingId, status);
    }

    public boolean deleteBooking(int id) throws Exception {
        return bookingDAO.delete(id);
    }

    public int addService(int providerId, String name, String description,
                          String category, double price, int duration) throws Exception {
        Service s = new Service(0, providerId, name, description, category, price, duration, true);
        return serviceDAO.insert(s);
    }

    public boolean deleteService(int id) throws Exception {
        return serviceDAO.delete(id);
    }
}
