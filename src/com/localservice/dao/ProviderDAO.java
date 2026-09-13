package com.localservice.dao;

import com.localservice.model.ServiceProvider;
import com.localservice.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProviderDAO {

    public int insert(ServiceProvider p) throws Exception {
        // user already inserted; insert provider row
        String sql = "INSERT INTO service_providers (user_id, business_name, description, rating, available) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getId());
            ps.setString(2, p.getBusinessName());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getRating());
            ps.setInt(5, p.isAvailable() ? 1 : 0);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int pid = keys.getInt(1);
                    p.setProviderId(pid);
                    return pid;
                }
            }
        }
        return -1;
    }

    public ServiceProvider findByUserId(int userId) throws Exception {
        String sql = "SELECT u.*, sp.id AS provider_id, sp.business_name, sp.description AS biz_desc, "
                + "sp.rating, sp.available FROM users u "
                + "JOIN service_providers sp ON u.id = sp.user_id WHERE u.id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public ServiceProvider findByProviderId(int providerId) throws Exception {
        String sql = "SELECT u.*, sp.id AS provider_id, sp.business_name, sp.description AS biz_desc, "
                + "sp.rating, sp.available FROM users u "
                + "JOIN service_providers sp ON u.id = sp.user_id WHERE sp.id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, providerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<ServiceProvider> findAll() throws Exception {
        List<ServiceProvider> list = new ArrayList<>();
        String sql = "SELECT u.*, sp.id AS provider_id, sp.business_name, sp.description AS biz_desc, "
                + "sp.rating, sp.available FROM users u "
                + "JOIN service_providers sp ON u.id = sp.user_id ORDER BY sp.id";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<ServiceProvider> findAvailable() throws Exception {
        List<ServiceProvider> list = new ArrayList<>();
        String sql = "SELECT u.*, sp.id AS provider_id, sp.business_name, sp.description AS biz_desc, "
                + "sp.rating, sp.available FROM users u "
                + "JOIN service_providers sp ON u.id = sp.user_id WHERE sp.available = 1 ORDER BY sp.rating DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public boolean setAvailable(int providerId, boolean available) throws Exception {
        String sql = "UPDATE service_providers SET available = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, available ? 1 : 0);
            ps.setInt(2, providerId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int providerId) throws Exception {
        String sql = "DELETE FROM service_providers WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, providerId);
            return ps.executeUpdate() > 0;
        }
    }

    private ServiceProvider map(ResultSet rs) throws SQLException {
        ServiceProvider p = new ServiceProvider();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setEmail(rs.getString("email"));
        p.setPassword(rs.getString("password"));
        p.setPhone(rs.getString("phone"));
        p.setProviderId(rs.getInt("provider_id"));
        p.setBusinessName(rs.getString("business_name"));
        p.setDescription(rs.getString("biz_desc"));
        p.setRating(rs.getDouble("rating"));
        p.setAvailable(rs.getInt("available") == 1);
        return p;
    }
}
