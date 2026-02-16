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
import java.io.PrintWriter;
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

        // Handle AJAX request for available rooms
        String action = req.getParameter("action");
        if ("get-available-rooms".equalsIgnoreCase(action)) {
            String roomType = req.getParameter("roomType");
            String checkInStr = req.getParameter("checkInDate");
            String checkOutStr = req.getParameter("checkOutDate");
            String excludeReservationIdStr = req.getParameter("excludeReservationId");
            Integer excludeReservationId = null;
            if (excludeReservationIdStr != null && !excludeReservationIdStr.isEmpty()) {
                try {
                    excludeReservationId = Integer.parseInt(excludeReservationIdStr);
                } catch (NumberFormatException e) {
                    // Ignore invalid format
                }
            }

            try {
                LocalDate checkInDate = LocalDate.parse(checkInStr);
                LocalDate checkOutDate = LocalDate.parse(checkOutStr);

                System.out.println("Fetching available rooms for type: " + roomType + ", dates: " + checkInDate + " to " + checkOutDate + ", excludeReservationId: " + excludeReservationId);

                // Get available rooms from service using date range
                List<Room> availableRooms = (excludeReservationId != null) 
                    ? roomService.getAvailableRoomsByTypeExcluding(roomType, checkInDate, checkOutDate, excludeReservationId)
                    : roomService.getAvailableRoomsByType(roomType, checkInDate, checkOutDate);
                
                System.out.println("Found " + availableRooms.size() + " available rooms");

                // Build JSON response manually
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("[");
                for (int i = 0; i < availableRooms.size(); i++) {
                    Room room = availableRooms.get(i);
                    jsonBuilder.append("{")
                            .append("\"roomId\":").append(room.getRoomId()).append(",")
                            .append("\"roomNumber\":\"").append(room.getRoomNumber()).append("\",")
                            .append("\"roomType\":\"").append(room.getRoomType()).append("\",")
                            .append("\"pricePerNight\":").append(room.getPricePerNight()).append(",")
                            .append("\"status\":\"").append(room.getStatus()).append("\"")
                            .append("}");
                    if (i < availableRooms.size() - 1) {
                        jsonBuilder.append(",");
                    }
                }
                jsonBuilder.append("]");

                // Return as JSON
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                out.print(jsonBuilder.toString());
                out.flush();
                return;
            } catch (Exception e) {
                System.err.println("Error fetching available rooms: " + e.getMessage());
                e.printStackTrace();
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().print("[]");
                return;
            }
        }

        // Load all reservations, guests, and rooms
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

            } else if ("edit-guest".equalsIgnoreCase(action)) {
                int guestId = Integer.parseInt(req.getParameter("guestId"));
                String guestName = req.getParameter("guestName");
                String address = req.getParameter("address");
                String contactNumber = req.getParameter("contactNumber");
                
                Guest guest = new Guest();
                guest.setGuestId(guestId);
                guest.setName(guestName);
                guest.setAddress(address);
                guest.setContactNumber(contactNumber);
                guestService.updateGuest(guest);
                System.out.println("Guest updated with ID: " + guestId);
                message = "Guest updated successfully!";

            } else if ("add-reservation".equalsIgnoreCase(action)) {
                int guestId = Integer.parseInt(req.getParameter("guestId"));
                String roomType = req.getParameter("roomType");
                int roomId = Integer.parseInt(req.getParameter("roomId"));
                String checkInStr = req.getParameter("checkInDate");
                String checkOutStr = req.getParameter("checkOutDate");

                System.out.println("Adding reservation for guest ID: " + guestId + ", roomId: " + roomId);

                Reservation reservation = new Reservation();
                reservation.setGuestId(guestId);
                reservation.setRoomId(roomId);
                reservation.setRoomType(roomType);
                reservation.setCheckInDate(LocalDate.parse(checkInStr));
                reservation.setCheckOutDate(LocalDate.parse(checkOutStr));
                System.out.println("Adding reservation...");
                reservationService.addReservation(reservation);
                System.out.println("Reservation added with number: " + reservation.getReservationNumber());
                message = "Reservation added successfully! Reservation Number: " + reservation.getReservationNumber();

            } else if ("edit-reservation".equalsIgnoreCase(action)) {
                int reservationId = Integer.parseInt(req.getParameter("reservationId"));
                String roomType = req.getParameter("roomType");
                int roomId = Integer.parseInt(req.getParameter("roomId"));
                String checkInStr = req.getParameter("checkInDate");
                String checkOutStr = req.getParameter("checkOutDate");

                System.out.println("Editing reservation ID: " + reservationId);

                // Get the old reservation to preserve status and other details
                Reservation oldReservation = reservationService.getReservationById(reservationId);
                
                if (oldReservation == null) {
                    error = "Reservation not found!";
                } else {
                    Reservation reservation = new Reservation();
                    reservation.setReservationId(reservationId);
                    reservation.setGuestId(oldReservation.getGuestId());
                    reservation.setRoomType(roomType);
                    reservation.setRoomId(roomId);
                    reservation.setCheckInDate(LocalDate.parse(checkInStr));
                    reservation.setCheckOutDate(LocalDate.parse(checkOutStr));
                    reservation.setStatus(oldReservation.getStatus()); // Preserve existing status
                    reservation.setCreatedAt(oldReservation.getCreatedAt()); // Preserve created date
                    reservationService.updateReservation(reservation);
                    System.out.println("Reservation updated successfully");
                    message = "Reservation updated successfully!";
                }

            } else if ("delete-reservation".equalsIgnoreCase(action)) {
                int reservationId = Integer.parseInt(req.getParameter("reservationId"));
                reservationService.deleteReservation(reservationId);
                message = "Reservation deleted successfully!";
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

        // Load all reservations, guests, and rooms
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
