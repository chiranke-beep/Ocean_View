package com.example.ocean_view_resort.service.impl;

import com.example.ocean_view_resort.dao.ReservationDAO;
import com.example.ocean_view_resort.dao.impl.ReservationDAOImpl;
import com.example.ocean_view_resort.model.Reservation;
import com.example.ocean_view_resort.service.ReservationService;

import java.time.LocalDate;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();

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
        if (reservation.getCheckInDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }

        try {
            // Generate reservation number
            String reservationNumber = reservationDAO.generateReservationNumber();
            reservation.setReservationNumber(reservationNumber);
            System.out.println("Setting reservation number: " + reservationNumber);

            reservationDAO.addReservation(reservation);
            System.out.println("Reservation added successfully");
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
        reservationDAO.updateReservation(reservation);
    }

    @Override
    public void deleteReservation(int reservationId) {
        reservationDAO.deleteReservation(reservationId);
    }
}
