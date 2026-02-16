package com.example.ocean_view_resort.service;

import com.example.ocean_view_resort.model.Reservation;
import java.util.List;

public interface ReservationService {
    Reservation getReservationById(int reservationId);
    Reservation getReservationByNumber(String reservationNumber);
    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByGuestId(int guestId);
    void addReservation(Reservation reservation) throws RuntimeException;
    void updateReservation(Reservation reservation);
    void deleteReservation(int reservationId);
}
