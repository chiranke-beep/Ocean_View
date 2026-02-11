package com.example.ocean_view_resort.dao;

import com.example.ocean_view_resort.model.Guest;
import java.sql.SQLException;
import java.util.List;

public interface GuestDAO {
    Guest getGuestById(int guestId);
    Guest getGuestByName(String name);
    List<Guest> getAllGuests();
    void addGuest(Guest guest) throws SQLException;
    void updateGuest(Guest guest);
    void deleteGuest(int guestId);
}
