package com.example.ocean_view_resort.dao.impl;

import com.example.ocean_view_resort.dao.GuestDAO;
import com.example.ocean_view_resort.model.Guest;
import com.example.ocean_view_resort.utils.DatabaseConnection;
import com.example.ocean_view_resort.utils.DatabaseResetUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GuestDAOImpl implements GuestDAO {

    @Override
    public Guest getGuestById(int guestId) {
        String sql = "SELECT * FROM guest WHERE guest_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuest(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Guest getGuestByName(String name) {
        String sql = "SELECT * FROM guest WHERE name = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuest(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guest ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                guests.add(mapResultSetToGuest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guests;
    }

    @Override
    public void addGuest(Guest guest) throws SQLException {
        String sql = "INSERT INTO guest (name, address, contact_number, created_at) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getAddress());
            stmt.setString(3, guest.getContactNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            int result = stmt.executeUpdate();
            System.out.println("Guest insert result: " + result);
            
            // Get the generated guest_id
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int guestId = generatedKeys.getInt(1);
                    guest.setGuestId(guestId);
                    System.out.println("Generated guest_id: " + guestId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding guest: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updateGuest(Guest guest) {
        String sql = "UPDATE guest SET name = ?, address = ?, contact_number = ? WHERE guest_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getAddress());
            stmt.setString(3, guest.getContactNumber());
            stmt.setInt(4, guest.getGuestId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteGuest(int guestId) {
        String sql = "DELETE FROM guest WHERE guest_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guestId);
            stmt.executeUpdate();
            DatabaseResetUtil.resetAutoIncrementIfEmpty("guest");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Guest mapResultSetToGuest(ResultSet rs) throws SQLException {
        return new Guest(
                rs.getInt("guest_id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("contact_number"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
