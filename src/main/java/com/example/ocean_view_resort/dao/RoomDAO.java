package com.example.ocean_view_resort.dao;

import com.example.ocean_view_resort.model.Room;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RoomDAO {
    Room getRoomById(int roomId);
    Room getRoomByNumber(String roomNumber);
    List<Room> getAllRooms();
    List<Room> getRoomsByStatus(String status);
    List<Room> getAvailableRoomsByType(String roomType, LocalDate checkInDate, LocalDate checkOutDate);
    BigDecimal getPriceByRoomType(String roomType); // Get first room price for a room type (fallback for bill generation)
    boolean addRoom(Room room);
    boolean updateRoom(Room room);
    boolean deleteRoom(int roomId);
}
