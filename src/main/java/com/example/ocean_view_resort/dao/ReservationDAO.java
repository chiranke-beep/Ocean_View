package com.example.ocean_view_resort.dao;

import com.example.ocean_view_resort.model.Reservation;
import java.sql.SQLException;
import java.util.List;

public interface ReservationDAO {
    Reservation getReservationById(int reservationId);
    Reservation getReservationByNumber(String reservationNumber);
    List<Reservation> getAllReservations();
    void addReservation(Reservation reservation) throws SQLException;
    void updateReservation(Reservation reservation);
    void deleteReservation(int reservationId);
    String generateReservationNumber() throws SQLException; // Auto-generate reservation number
}
