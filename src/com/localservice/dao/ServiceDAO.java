package com.localservice.dao;

import com.localservice.model.Service;
import com.localservice.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    public List<Service> findAllActive() throws Exception {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT s.*, sp.business_name AS provider_name FROM services s "
                + "JOIN service_providers sp ON s.provider_id = sp.id "
                + "WHERE s.active = 1 ORDER BY s.category, s.name";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Service> findAll() throws Exception {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT s.*, sp.business_name AS provider_name FROM services s "
                + "JOIN service_providers sp ON s.provider_id = sp.id ORDER BY s.id";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Service> findByProvider(int providerId) throws Exception {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT s.*, sp.business_name AS provider_name FROM services s "
                + "JOIN service_providers sp ON s.provider_id = sp.id WHERE s.provider_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, providerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<Service> findByCategory(String category) throws Exception {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT s.*, sp.business_name AS provider_name FROM services s "
                + "JOIN service_providers sp ON s.provider_id = sp.id "
                + "WHERE s.active = 1 AND s.category = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<Service> findEmergencyServices() throws Exception {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT s.*, sp.business_name AS provider_name FROM services s "
                + "JOIN service_providers sp ON s.provider_id = sp.id "
                + "WHERE s.active = 1 AND (s.name LIKE '%Emergency%' OR s.description LIKE '%emergency%')";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Service findById(int id) throws Exception {
        String sql = "SELECT s.*, sp.business_name AS provider_name FROM services s "
                + "JOIN service_providers sp ON s.provider_id = sp.id WHERE s.id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public int insert(Service s) throws Exception {
        String sql = "INSERT INTO services (provider_id, name, description, category, price, duration_minutes, active) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getProviderId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getDescription());
            ps.setString(4, s.getCategory());
            ps.setDouble(5, s.getPrice());
            ps.setInt(6, s.getDurationMinutes());
            ps.setInt(7, s.isActive() ? 1 : 0);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    s.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM services WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Service map(ResultSet rs) throws SQLException {
        Service s = new Service();
        s.setId(rs.getInt("id"));
        s.setProviderId(rs.getInt("provider_id"));
        s.setName(rs.getString("name"));
        s.setDescription(rs.getString("description"));
        s.setCategory(rs.getString("category"));
        s.setPrice(rs.getDouble("price"));
        s.setDurationMinutes(rs.getInt("duration_minutes"));
        s.setActive(rs.getInt("active") == 1);
        try {
            s.setProviderName(rs.getString("provider_name"));
        } catch (SQLException ignored) {}
        return s;
    }
}
