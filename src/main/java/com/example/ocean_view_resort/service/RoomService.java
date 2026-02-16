package com.example.ocean_view_resort.service;

import com.example.ocean_view_resort.model.Room;
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
    boolean addRoom(String roomNumber, String roomType, double pricePerNight, int capacity);
    boolean updateRoom(int roomId, String roomNumber, String roomType, double pricePerNight, int capacity, String status);
    boolean deleteRoom(int roomId);
}
