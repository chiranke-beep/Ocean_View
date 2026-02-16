package com.example.ocean_view_resort.dao;

import com.example.ocean_view_resort.model.Room;
import java.util.List;

public interface RoomDAO {
    Room getRoomById(int roomId);
    Room getRoomByNumber(String roomNumber);
    List<Room> getAllRooms();
    List<Room> getRoomsByStatus(String status);
    boolean addRoom(Room room);
    boolean updateRoom(Room room);
    boolean deleteRoom(int roomId);
    java.math.BigDecimal getPriceByRoomType(String roomType);
}
