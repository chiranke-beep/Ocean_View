package com.example.ocean_view_resort.dao.impl;

import com.example.ocean_view_resort.dao.RoomDAO;
import com.example.ocean_view_resort.model.Room;
import com.example.ocean_view_resort.utils.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    @Override
    public Room getRoomById(int roomId) {
        String sql = "SELECT * FROM room WHERE room_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRoom(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Room getRoomByNumber(String roomNumber) {
        String sql = "SELECT * FROM room WHERE room_number = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRoom(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    @Override
    public List<Room> getRoomsByStatus(String status) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room WHERE status = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    @Override
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO room (room_number, room_type, price_per_night, capacity, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getPricePerNight());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getStatus());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateRoom(Room room) {
        String sql = "UPDATE room SET room_number=?, room_type=?, price_per_night=?, capacity=?, status=? WHERE room_id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getPricePerNight());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getStatus());
            ps.setInt(6, room.getRoomId());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM room WHERE room_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public BigDecimal getPriceByRoomType(String roomType) {
        String sql = "SELECT price_per_night FROM room WHERE room_type = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double price = rs.getDouble("price_per_night");
                    if (price > 0) {
                        return BigDecimal.valueOf(price);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<Room> getAvailableRoomsByType(String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Room> availableRooms = new ArrayList<>();
        
        String sql = "SELECT DISTINCT r.* FROM room r " +
                "WHERE r.room_type = ? " +
                "AND r.room_id NOT IN (" +
                "  SELECT DISTINCT res.room_id FROM reservation res " +
                "  WHERE res.room_id IS NOT NULL " +
                "  AND res.status != 'Cancelled' " +
                "  AND (" +
                "    (res.check_in_date < ? AND res.check_out_date > ?) " +
                "    OR (res.check_in_date >= ? AND res.check_in_date < ?) " +
                "    OR (res.check_out_date > ? AND res.check_out_date <= ?)" +
                "  )" +
                ")";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomType);
            ps.setDate(2, java.sql.Date.valueOf(checkOutDate));
            ps.setDate(3, java.sql.Date.valueOf(checkInDate));
            ps.setDate(4, java.sql.Date.valueOf(checkOutDate));
            ps.setDate(5, java.sql.Date.valueOf(checkInDate));
            ps.setDate(6, java.sql.Date.valueOf(checkInDate));
            ps.setDate(7, java.sql.Date.valueOf(checkOutDate));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    availableRooms.add(mapResultSetToRoom(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return availableRooms;
    }

    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("room_id"),
                rs.getString("room_number"),
                rs.getString("room_type"),
                rs.getDouble("price_per_night"),
                rs.getInt("capacity"),
                rs.getString("status"),
                rs.getString("created_date")
        );
    }
}
