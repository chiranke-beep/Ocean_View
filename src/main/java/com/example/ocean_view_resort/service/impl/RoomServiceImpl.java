package com.example.ocean_view_resort.service.impl;

import com.example.ocean_view_resort.dao.RoomDAO;
import com.example.ocean_view_resort.dao.impl.RoomDAOImpl;
import com.example.ocean_view_resort.model.Room;
import com.example.ocean_view_resort.service.RoomService;

import java.util.List;

public class RoomServiceImpl implements RoomService {
    private RoomDAO roomDAO = new RoomDAOImpl();

    @Override
    public Room getRoomById(int roomId) {
        return roomDAO.getRoomById(roomId);
    }

    @Override
    public Room getRoomByNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.isEmpty()) {
            return null;
        }
        return roomDAO.getRoomByNumber(roomNumber);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    @Override
    public List<Room> getAvailableRooms() {
        return roomDAO.getRoomsByStatus("Available");
    }

    @Override
    public boolean addRoom(String roomNumber, String roomType, double pricePerNight, int capacity) {
        if (roomNumber == null || roomNumber.isEmpty() || roomType == null || roomType.isEmpty() ||
            pricePerNight <= 0 || capacity <= 0) {
            return false;
        }
        
        // Check if room number already exists
        if (roomDAO.getRoomByNumber(roomNumber) != null) {
            return false;
        }
        
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setPricePerNight(pricePerNight);
        room.setCapacity(capacity);
        room.setStatus("Available");
        
        return roomDAO.addRoom(room);
    }

    @Override
    public boolean updateRoom(int roomId, String roomNumber, String roomType, double pricePerNight, int capacity, String status) {
        if (roomNumber == null || roomNumber.isEmpty() || roomType == null || roomType.isEmpty() ||
            pricePerNight <= 0 || capacity <= 0 || status == null || status.isEmpty()) {
            return false;
        }
        
        Room room = roomDAO.getRoomById(roomId);
        if (room == null) {
            return false;
        }
        
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setPricePerNight(pricePerNight);
        room.setCapacity(capacity);
        room.setStatus(status);
        
        return roomDAO.updateRoom(room);
    }

    @Override
    public boolean deleteRoom(int roomId) {
        return roomDAO.deleteRoom(roomId);
    }
}
