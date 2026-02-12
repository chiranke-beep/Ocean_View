package com.example.ocean_view_resort.controller;

import com.example.ocean_view_resort.model.Guest;
import com.example.ocean_view_resort.model.Reservation;
import com.example.ocean_view_resort.model.Room;
import com.example.ocean_view_resort.service.GuestService;
import com.example.ocean_view_resort.service.ReservationService;
import com.example.ocean_view_resort.service.RoomService;
import com.example.ocean_view_resort.service.impl.GuestServiceImpl;
import com.example.ocean_view_resort.service.impl.ReservationServiceImpl;
import com.example.ocean_view_resort.service.impl.RoomServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "StaffDashboardServlet", value = "/staff-dashboard")
public class StaffDashboardServlet extends HttpServlet {
    private final GuestService guestService = new GuestServiceImpl();
    private final ReservationService reservationService = new ReservationServiceImpl();
    private final RoomService roomService = new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null || session.getAttribute("role") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Check if user is staff
        String role = (String) session.getAttribute("role");
        if (!"Staff".equalsIgnoreCase(role)) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Load all reservations and guests
        List<Reservation> reservations = reservationService.getAllReservations();
        List<Guest> guests = guestService.getAllGuests();
        List<Room> rooms = roomService.getAllRooms();

        req.setAttribute("reservations", reservations);
        req.setAttribute("guests", guests);
        req.setAttribute("rooms", rooms);
        req.getRequestDispatcher("/staff-dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null || session.getAttribute("role") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Check if user is staff
        String role = (String) session.getAttribute("role");
        if (!"Staff".equalsIgnoreCase(role)) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        String message = "";
        String error = "";

        try {
            if ("add-guest".equalsIgnoreCase(action)) {
                String guestName = req.getParameter("guestName");
                String address = req.getParameter("address");
                String contactNumber = req.getParameter("contactNumber");

                System.out.println("Adding guest: " + guestName);

                Guest guest = new Guest();
                guest.setName(guestName);
                guest.setAddress(address);
                guest.setContactNumber(contactNumber);
                guestService.addGuest(guest);
                System.out.println("Guest added with ID: " + guest.getGuestId());
                message = "Guest added successfully!";

            } else if ("delete-guest".equalsIgnoreCase(action)) {
                int guestId = Integer.parseInt(req.getParameter("guestId"));
                guestService.deleteGuest(guestId);
                message = "Guest deleted successfully!";

            } else if ("add-reservation".equalsIgnoreCase(action)) {
                int guestId = Integer.parseInt(req.getParameter("guestId"));
                int roomId = Integer.parseInt(req.getParameter("roomId"));
                String roomType = req.getParameter("roomType");
                String checkInStr = req.getParameter("checkInDate");
                String checkOutStr = req.getParameter("checkOutDate");

                System.out.println("Adding reservation for guest ID: " + guestId + ", room ID: " + roomId);

                // Validate room exists and is of correct type
                Room room = roomService.getRoomById(roomId);
                if (room == null) {
                    error = "Selected room not found!";
                } else if (!room.getRoomType().equalsIgnoreCase(roomType)) {
                    error = "Room type mismatch! Selected room is " + room.getRoomType();
                } else {
                    LocalDate checkInDate = LocalDate.parse(checkInStr);
                    LocalDate checkOutDate = LocalDate.parse(checkOutStr);
                    
                    // Check if room is available for the dates
                    List<Room> availableRooms = roomService.getAvailableRoomsByType(roomType, checkInDate, checkOutDate);
                    boolean roomAvailable = availableRooms.stream().anyMatch(r -> r.getRoomId() == roomId);
                    
                    if (!roomAvailable) {
                        error = "Selected room is not available for the requested dates!";
                    } else {
                        Reservation reservation = new Reservation();
                        reservation.setGuestId(guestId);
                        reservation.setRoomId(roomId);
                        reservation.setRoomType(roomType);
                        reservation.setCheckInDate(checkInDate);
                        reservation.setCheckOutDate(checkOutDate);
                        System.out.println("Adding reservation...");
                        reservationService.addReservation(reservation);
                        System.out.println("Reservation added with number: " + reservation.getReservationNumber());
                        message = "Reservation added successfully! Reservation Number: " + reservation.getReservationNumber();
                    }
                }

            } else if ("delete-reservation".equalsIgnoreCase(action)) {
                int reservationId = Integer.parseInt(req.getParameter("reservationId"));
                reservationService.deleteReservation(reservationId);
                message = "Reservation deleted successfully!";

            } else if ("edit-reservation".equalsIgnoreCase(action)) {
                int reservationId = Integer.parseInt(req.getParameter("reservationId"));
                int guestId = Integer.parseInt(req.getParameter("guestId"));
                int roomId = Integer.parseInt(req.getParameter("roomId"));
                String roomType = req.getParameter("roomType");
                String checkInStr = req.getParameter("checkInDate");
                String checkOutStr = req.getParameter("checkOutDate");

                System.out.println("Editing reservation ID: " + reservationId);

                // Validate room exists and is of correct type
                Room room = roomService.getRoomById(roomId);
                if (room == null) {
                    error = "Selected room not found!";
                } else if (!room.getRoomType().equalsIgnoreCase(roomType)) {
                    error = "Room type mismatch! Selected room is " + room.getRoomType();
                } else {
                    LocalDate checkInDate = LocalDate.parse(checkInStr);
                    LocalDate checkOutDate = LocalDate.parse(checkOutStr);
                    
                    // Check if room is available for the dates
                    List<Room> availableRooms = roomService.getAvailableRoomsByType(roomType, checkInDate, checkOutDate);
                    
                    // Get the current reservation to check if room is the same
                    Reservation currentReservation = reservationService.getReservationById(reservationId);
                    boolean roomAvailable = availableRooms.stream().anyMatch(r -> r.getRoomId() == roomId);
                    
                    // Room is available if it's the current room OR it's in the available list
                    if (!roomAvailable && (currentReservation == null || currentReservation.getRoomId() != roomId)) {
                        error = "Selected room is not available for the requested dates!";
                    } else {
                        Reservation reservation = new Reservation();
                        reservation.setReservationId(reservationId);
                        reservation.setGuestId(guestId);
                        reservation.setRoomId(roomId);
                        reservation.setRoomType(roomType);
                        reservation.setCheckInDate(checkInDate);
                        reservation.setCheckOutDate(checkOutDate);
                        // Preserve the current status
                        if (currentReservation != null) {
                            reservation.setStatus(currentReservation.getStatus());
                        } else {
                            reservation.setStatus("Confirmed");
                        }
                        
                        System.out.println("Updating reservation...");
                        reservationService.updateReservation(reservation);
                        System.out.println("Reservation updated successfully");
                        message = "Reservation updated successfully!";
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            error = "Validation error: " + e.getMessage();
            System.out.println("Validation error: " + e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) {
            error = "Database error: " + e.getMessage();
            System.out.println("Runtime error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            error = "An error occurred: " + e.getMessage();
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }

        // Load all reservations and guests
        List<Reservation> reservations = reservationService.getAllReservations();
        List<Guest> guests = guestService.getAllGuests();
        List<Room> rooms = roomService.getAllRooms();

        req.setAttribute("reservations", reservations);
        req.setAttribute("guests", guests);
        req.setAttribute("rooms", rooms);
        req.setAttribute("message", message);
        req.setAttribute("error", error);
        req.getRequestDispatcher("/staff-dashboard.jsp").forward(req, resp);
    }
}
