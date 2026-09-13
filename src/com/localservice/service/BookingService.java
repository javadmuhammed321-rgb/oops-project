package com.localservice.service;

import com.localservice.dao.BookingDAO;
import com.localservice.dao.PaymentDAO;
import com.localservice.dao.ServiceDAO;
import com.localservice.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Booking & payment business logic.
 * Demonstrates POLYMORPHISM via PaymentProcessor hierarchy.
 */
public class BookingService {
    private final BookingDAO bookingDAO = new BookingDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    public Booking createBooking(int customerId, int serviceId, String date, String time,
                                 boolean emergency, String notes) throws Exception {
        Service svc = serviceDAO.findById(serviceId);
        if (svc == null) throw new Exception("Service not found");
        Booking b = new Booking(0, customerId, serviceId, svc.getProviderId(),
                date, time, emergency ? "CONFIRMED" : "PENDING", emergency, notes);
        bookingDAO.insert(b);
        return bookingDAO.findById(b.getId());
    }

    public boolean cancelBooking(int bookingId, int customerId) throws Exception {
        Booking b = bookingDAO.findById(bookingId);
        if (b == null) throw new Exception("Booking not found");
        if (b.getCustomerId() != customerId) throw new Exception("Not your booking");
        if ("CANCELLED".equals(b.getStatus()) || "COMPLETED".equals(b.getStatus())) {
            throw new Exception("Cannot cancel a " + b.getStatus() + " booking");
        }
        return bookingDAO.cancel(bookingId);
    }

    public boolean updateStatus(int bookingId, String status) throws Exception {
        return bookingDAO.updateStatus(bookingId, status);
    }

    public List<Booking> historyForCustomer(int customerId) throws Exception {
        return bookingDAO.findByCustomer(customerId);
    }

    public List<Booking> forProvider(int providerId) throws Exception {
        return bookingDAO.findByProvider(providerId);
    }

    public List<Booking> allBookings() throws Exception {
        return bookingDAO.findAll();
    }

    /**
     * Process payment using polymorphic PaymentProcessor.
     */
    public Payment processPayment(int bookingId, String method) throws Exception {
        Booking b = bookingDAO.findById(bookingId);
        if (b == null) throw new Exception("Booking not found");
        if (paymentDAO.findByBookingId(bookingId) != null) {
            throw new Exception("Payment already exists for this booking");
        }

        PaymentProcessor processor = createProcessor(method, b.getServicePrice());
        String ref = processor.process();

        Payment p = new Payment();
        p.setBookingId(bookingId);
        p.setAmount(b.getServicePrice());
        p.setMethod(processor.getMethodName());
        p.setStatus("COMPLETED");
        p.setTransactionRef(ref);
        p.setPaidAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        paymentDAO.insert(p);

        if ("PENDING".equals(b.getStatus())) {
            bookingDAO.updateStatus(bookingId, "CONFIRMED");
        }
        return p;
    }

    /** Factory method selecting concrete PaymentProcessor (POLYMORPHISM). */
    private PaymentProcessor createProcessor(String method, double amount) throws Exception {
        if (method == null) throw new Exception("Payment method required");
        switch (method.toUpperCase()) {
            case "CASH": return new CashPaymentProcessor(amount);
            case "UPI":  return new UpiPaymentProcessor(amount);
            case "CARD": return new CardPaymentProcessor(amount);
            default: throw new Exception("Invalid payment method: " + method);
        }
    }

    public Payment getPayment(int bookingId) throws Exception {
        return paymentDAO.findByBookingId(bookingId);
    }

    public List<Payment> allPayments() throws Exception {
        return paymentDAO.findAll();
    }
}
