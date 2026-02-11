package com.example.ocean_view_resort.service;

import com.example.ocean_view_resort.model.Guest;
import java.util.List;

public interface GuestService {
    Guest getGuestById(int guestId);
    List<Guest> getAllGuests();
    void addGuest(Guest guest); // with validation
    void updateGuest(Guest guest);
    void deleteGuest(int guestId);
}
