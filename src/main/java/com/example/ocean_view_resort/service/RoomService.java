package com.example.ocean_view_resort.service;

import com.example.ocean_view_resort.model.Room;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service Layer for Room Business Logic
 * Encapsulates business rules, validation, and complex operations
 * Acts as intermediary between Controller and DAO
 */
public interface RoomService {
    Room getRoomById(int roomId);
    Room getRoomByNumber(String roomNumber);
    List<Room> getAllRooms();
    List<Room> getAvailableRooms();
    List<Room> getAvailableRoomsByType(String roomType, LocalDate checkInDate, LocalDate checkOutDate);
    BigDecimal getPriceByRoomType(String roomType); // Get first room price for a room type
    boolean addRoom(String roomNumber, String roomType, double pricePerNight, int capacity);
    boolean updateRoom(int roomId, String roomNumber, String roomType, double pricePerNight, int capacity, String status);
    boolean deleteRoom(int roomId);
}
