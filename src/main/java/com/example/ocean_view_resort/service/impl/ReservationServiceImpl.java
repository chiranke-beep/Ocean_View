package com.example.ocean_view_resort.service.impl;

import com.example.ocean_view_resort.dao.ReservationDAO;
import com.example.ocean_view_resort.dao.impl.ReservationDAOImpl;
import com.example.ocean_view_resort.model.Reservation;
import com.example.ocean_view_resort.model.Room;
import com.example.ocean_view_resort.service.ReservationService;
import com.example.ocean_view_resort.service.RoomService;
import java.time.LocalDate;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final RoomService roomService = new RoomServiceImpl();

    @Override
    public Reservation getReservationById(int reservationId) {
        return reservationDAO.getReservationById(reservationId);
    }

    @Override
    public Reservation getReservationByNumber(String reservationNumber) {
        return reservationDAO.getReservationByNumber(reservationNumber);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }

    @Override
    public void addReservation(Reservation reservation) {
        // Validation
        if (reservation.getRoomType() == null || reservation.getRoomType().trim().isEmpty()) {
            throw new IllegalArgumentException("Room type is required");
        }
        if (reservation.getCheckInDate() == null) {
            throw new IllegalArgumentException("Check-in date is required");
        }
        if (reservation.getCheckOutDate() == null) {
            throw new IllegalArgumentException("Check-out date is required");
        }
        if (reservation.getCheckOutDate().isBefore(reservation.getCheckInDate()) ||
                reservation.getCheckOutDate().equals(reservation.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }


        try {
            // Generate reservation number
            String reservationNumber = reservationDAO.generateReservationNumber();
            reservation.setReservationNumber(reservationNumber);
            System.out.println("Setting reservation number: " + reservationNumber);

            reservationDAO.addReservation(reservation);
            System.out.println("Reservation added successfully");
            
            // Update room status to Occupied if room_id is set
            if (reservation.getRoomId() > 0) {
                Room room = roomService.getRoomById(reservation.getRoomId());
                if (room != null) {
                    roomService.updateRoom(room.getRoomId(), room.getRoomNumber(), room.getRoomType(), 
                            room.getPricePerNight(), room.getCapacity(), "Occupied");
                    System.out.println("Room status updated to Occupied for room: " + reservation.getRoomId());
                }
            }
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Database error while adding reservation: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateReservation(Reservation reservation) {
        if (reservation.getRoomType() == null || reservation.getRoomType().trim().isEmpty()) {
            throw new IllegalArgumentException("Room type is required");
        }
        if (reservation.getCheckOutDate().isBefore(reservation.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        
        // Get the old reservation to check if room changed
        Reservation oldReservation = reservationDAO.getReservationById(reservation.getReservationId());
        
        // Update the reservation
        reservationDAO.updateReservation(reservation);
        
        // Handle room status changes
        if (oldReservation != null && oldReservation.getRoomId() > 0) {
            // If room changed, revert old room status to Available
            if (oldReservation.getRoomId() != reservation.getRoomId()) {
                Room oldRoom = roomService.getRoomById(oldReservation.getRoomId());
                if (oldRoom != null) {
                    roomService.updateRoom(oldRoom.getRoomId(), oldRoom.getRoomNumber(), oldRoom.getRoomType(),
                            oldRoom.getPricePerNight(), oldRoom.getCapacity(), "Available");
                    System.out.println("Room status updated back to Available for old room: " + oldReservation.getRoomId());
                }
            }
            
            // Set new room status to Occupied
            if (reservation.getRoomId() > 0) {
                Room newRoom = roomService.getRoomById(reservation.getRoomId());
                if (newRoom != null) {
                    roomService.updateRoom(newRoom.getRoomId(), newRoom.getRoomNumber(), newRoom.getRoomType(),
                            newRoom.getPricePerNight(), newRoom.getCapacity(), "Occupied");
                    System.out.println("Room status updated to Occupied for new room: " + reservation.getRoomId());
                }
            }
        }
    }

    @Override
    public List<Reservation> getReservationsByGuestId(int guestId) {
        return reservationDAO.getReservationsByGuestId(guestId);
    }

    @Override
    public void deleteReservation(int reservationId) {
        // Get reservation before deleting to get room_id
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        
        reservationDAO.deleteReservation(reservationId);
        
        // Update room status back to Available if room_id exists
        if (reservation != null && reservation.getRoomId() > 0) {
            Room room = roomService.getRoomById(reservation.getRoomId());
            if (room != null) {
                roomService.updateRoom(room.getRoomId(), room.getRoomNumber(), room.getRoomType(), 
                        room.getPricePerNight(), room.getCapacity(), "Available");
                System.out.println("Room status updated back to Available for room: " + reservation.getRoomId());
            }
        }
    }
}
