package com.example.ocean_view_resort.service.impl;

import com.example.ocean_view_resort.dao.GuestDAO;
import com.example.ocean_view_resort.dao.impl.GuestDAOImpl;
import com.example.ocean_view_resort.model.Guest;
import com.example.ocean_view_resort.service.GuestService;

import java.util.List;

public class GuestServiceImpl implements GuestService {
    private final GuestDAO guestDAO = new GuestDAOImpl();

    @Override
    public Guest getGuestById(int guestId) {
        return guestDAO.getGuestById(guestId);
    }

    @Override
    public List<Guest> getAllGuests() {
        return guestDAO.getAllGuests();
    }

    @Override
    public void addGuest(Guest guest) throws IllegalArgumentException {
        // Validation
        if (guest.getName() == null || guest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name is required");
        }
        if (guest.getAddress() == null || guest.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (guest.getContactNumber() == null || guest.getContactNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Contact number is required");
        }
        if (guest.getContactNumber().length() < 10) {
            throw new IllegalArgumentException("Contact number must be at least 10 digits");
        }
        try {
            guestDAO.addGuest(guest);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Database error while adding guest: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateGuest(Guest guest) {
        if (guest.getName() == null || guest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name is required");
        }
        if (guest.getAddress() == null || guest.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (guest.getContactNumber() == null || guest.getContactNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Contact number is required");
        }
        guestDAO.updateGuest(guest);
    }

    @Override
    public void deleteGuest(int guestId) {
        guestDAO.deleteGuest(guestId);
    }
}
