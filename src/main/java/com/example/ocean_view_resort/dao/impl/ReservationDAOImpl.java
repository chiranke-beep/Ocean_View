package com.example.ocean_view_resort.dao.impl;

import com.example.ocean_view_resort.dao.ReservationDAO;
import com.example.ocean_view_resort.model.Reservation;
import com.example.ocean_view_resort.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public Reservation getReservationById(int reservationId) {
        String sql = "SELECT * FROM reservation WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Reservation getReservationByNumber(String reservationNumber) {
        String sql = "SELECT * FROM reservation WHERE reservation_number = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reservationNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public void addReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation (reservation_number, guest_id, room_id, room_type, check_in_date, check_out_date, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, reservation.getReservationNumber());
            stmt.setInt(2, reservation.getGuestId());
            stmt.setInt(3, reservation.getRoomId());
            stmt.setString(4, reservation.getRoomType());
            stmt.setDate(5, Date.valueOf(reservation.getCheckInDate()));
            stmt.setDate(6, Date.valueOf(reservation.getCheckOutDate()));
            stmt.setString(7, "Confirmed");
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            int result = stmt.executeUpdate();
            System.out.println("Reservation insert result: " + result);
            
            // Get the generated reservation_id
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int resId = generatedKeys.getInt(1);
                    reservation.setReservationId(resId);
                    System.out.println("Generated reservation_id: " + resId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding reservation: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updateReservation(Reservation reservation) {
        String sql = "UPDATE reservation SET guest_id = ?, room_id = ?, room_type = ?, check_in_date = ?, check_out_date = ?, status = ? WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservation.getGuestId());
            stmt.setInt(2, reservation.getRoomId());
            stmt.setString(3, reservation.getRoomType());
            stmt.setDate(4, Date.valueOf(reservation.getCheckInDate()));
            stmt.setDate(5, Date.valueOf(reservation.getCheckOutDate()));
            stmt.setString(6, reservation.getStatus());
            stmt.setInt(7, reservation.getReservationId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteReservation(int reservationId) {
        String sql = "DELETE FROM reservation WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateReservationNumber() throws SQLException {
        String sql = "SELECT MAX(reservation_id) FROM reservation";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                String resNum = "RES-" + String.format("%06d", maxId + 1);
                System.out.println("Generated reservation number: " + resNum);
                return resNum;
            }
        } catch (SQLException e) {
            System.out.println("Error generating reservation number: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return "RES-000001";
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation(
                rs.getInt("reservation_id"),
                rs.getString("reservation_number"),
                rs.getInt("guest_id"),
                rs.getInt("room_id"),
                rs.getString("room_type"),
                rs.getDate("check_in_date").toLocalDate(),
                rs.getDate("check_out_date").toLocalDate(),
                rs.getString("status"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
        return reservation;
    }
}
