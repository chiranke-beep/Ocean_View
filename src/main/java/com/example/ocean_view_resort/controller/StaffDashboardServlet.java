package com.example.ocean_view_resort.controller;

import com.example.ocean_view_resort.model.Guest;
import com.example.ocean_view_resort.model.Reservation;
import com.example.ocean_view_resort.service.GuestService;
import com.example.ocean_view_resort.service.ReservationService;
import com.example.ocean_view_resort.service.impl.GuestServiceImpl;
import com.example.ocean_view_resort.service.impl.ReservationServiceImpl;

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

        req.setAttribute("reservations", reservations);
        req.setAttribute("guests", guests);
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
                String roomType = req.getParameter("roomType");
                String checkInStr = req.getParameter("checkInDate");
                String checkOutStr = req.getParameter("checkOutDate");

                System.out.println("Adding reservation for guest ID: " + guestId);

                Reservation reservation = new Reservation();
                reservation.setGuestId(guestId);
                reservation.setRoomType(roomType);
                reservation.setCheckInDate(LocalDate.parse(checkInStr));
                reservation.setCheckOutDate(LocalDate.parse(checkOutStr));
                System.out.println("Adding reservation...");
                reservationService.addReservation(reservation);
                System.out.println("Reservation added with number: " + reservation.getReservationNumber());
                message = "Reservation added successfully! Reservation Number: " + reservation.getReservationNumber();

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

        // Load all reservations and guests
        List<Reservation> reservations = reservationService.getAllReservations();
        List<Guest> guests = guestService.getAllGuests();

        req.setAttribute("reservations", reservations);
        req.setAttribute("guests", guests);
        req.setAttribute("message", message);
        req.setAttribute("error", error);
        req.getRequestDispatcher("/staff-dashboard.jsp").forward(req, resp);
    }
}
