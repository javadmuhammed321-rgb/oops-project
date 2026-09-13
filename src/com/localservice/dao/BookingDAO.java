package com.localservice.dao;

import com.localservice.model.Booking;
import com.localservice.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public int insert(Booking b) throws Exception {
        String sql = "INSERT INTO bookings (customer_id, service_id, provider_id, booking_date, booking_time, status, is_emergency, notes) "
                + "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, b.getCustomerId());
            ps.setInt(2, b.getServiceId());
            ps.setInt(3, b.getProviderId());
            ps.setString(4, b.getBookingDate());
            ps.setString(5, b.getBookingTime());
            ps.setString(6, b.getStatus() != null ? b.getStatus() : "PENDING");
            ps.setInt(7, b.isEmergency() ? 1 : 0);
            ps.setString(8, b.getNotes());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    b.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public Booking findById(int id) throws Exception {
        String sql = baseSelect() + " WHERE b.id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<Booking> findByCustomer(int customerId) throws Exception {
        List<Booking> list = new ArrayList<>();
        String sql = baseSelect() + " WHERE b.customer_id = ? ORDER BY b.created_at DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<Booking> findByProvider(int providerId) throws Exception {
        List<Booking> list = new ArrayList<>();
        String sql = baseSelect() + " WHERE b.provider_id = ? ORDER BY b.created_at DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, providerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<Booking> findAll() throws Exception {
        List<Booking> list = new ArrayList<>();
        String sql = baseSelect() + " ORDER BY b.created_at DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public boolean updateStatus(int id, String status) throws Exception {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(2, id);
            ps.setString(1, status);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean cancel(int id) throws Exception {
        return updateStatus(id, "CANCELLED");
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private String baseSelect() {
        return "SELECT b.*, u.name AS customer_name, s.name AS service_name, s.price AS service_price, "
                + "sp.business_name AS provider_name FROM bookings b "
                + "JOIN users u ON b.customer_id = u.id "
                + "JOIN services s ON b.service_id = s.id "
                + "JOIN service_providers sp ON b.provider_id = sp.id";
    }

    private Booking map(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setId(rs.getInt("id"));
        b.setCustomerId(rs.getInt("customer_id"));
        b.setServiceId(rs.getInt("service_id"));
        b.setProviderId(rs.getInt("provider_id"));
        b.setBookingDate(rs.getString("booking_date"));
        b.setBookingTime(rs.getString("booking_time"));
        b.setStatus(rs.getString("status"));
        b.setEmergency(rs.getInt("is_emergency") == 1);
        b.setNotes(rs.getString("notes"));
        b.setCreatedAt(rs.getString("created_at"));
        b.setCustomerName(rs.getString("customer_name"));
        b.setServiceName(rs.getString("service_name"));
        b.setProviderName(rs.getString("provider_name"));
        b.setServicePrice(rs.getDouble("service_price"));
        return b;
    }
}
