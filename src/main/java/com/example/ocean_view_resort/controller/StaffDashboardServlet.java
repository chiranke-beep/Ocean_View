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
import com.example.ocean_view_resort.utils.EmailService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Handles the Staff Dashboard UI (GET) and all guest/reservation CRUD (POST).
 * Available-room lookups are now delegated to RoomApiServlet (/api/rooms?action=available).
 */
@WebServlet(name = "StaffDashboardServlet", value = "/staff-dashboard")
public class StaffDashboardServlet extends HttpServlet {

    private final GuestService guestService           = new GuestServiceImpl();
    private final ReservationService reservationService = new ReservationServiceImpl();
    private final RoomService roomService             = new RoomServiceImpl();

    // ─── GET: serve the JSP ───────────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null
                || session.getAttribute("role") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (!"Staff".equalsIgnoreCase((String) session.getAttribute("role"))) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Forward to JSP — all data (guests, reservations, rooms) is loaded
        // client-side via the API servlets (/api/guests, /api/reservations, /api/rooms).
        req.getRequestDispatcher("/staff-dashboard.jsp").forward(req, resp);
    }

    // ─── POST: CRUD for guests & reservations ─────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null
                || session.getAttribute("role") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (!"Staff".equalsIgnoreCase((String) session.getAttribute("role"))) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action  = req.getParameter("action");
        String message = "";
        String error   = "";

        try {
            if ("add-guest".equalsIgnoreCase(action)) {
                Guest guest = new Guest();
                guest.setName(req.getParameter("guestName"));
                guest.setAddress(req.getParameter("address"));
                guest.setContactNumber(req.getParameter("contactNumber"));
                guest.setEmail(req.getParameter("email"));
                guestService.addGuest(guest);
                message = "Guest added successfully!";

            } else if ("delete-guest".equalsIgnoreCase(action)) {
                guestService.deleteGuest(Integer.parseInt(req.getParameter("guestId")));
                message = "Guest deleted successfully!";

            } else if ("edit-guest".equalsIgnoreCase(action)) {
                Guest guest = new Guest();
                guest.setGuestId(Integer.parseInt(req.getParameter("guestId")));
                guest.setName(req.getParameter("guestName"));
                guest.setAddress(req.getParameter("address"));
                guest.setContactNumber(req.getParameter("contactNumber"));
                guest.setEmail(req.getParameter("email"));
                guestService.updateGuest(guest);
                message = "Guest updated successfully!";

            } else if ("add-reservation".equalsIgnoreCase(action)) {
                Reservation res = new Reservation();
                res.setGuestId(Integer.parseInt(req.getParameter("guestId")));
                res.setRoomId(Integer.parseInt(req.getParameter("roomId")));
                res.setRoomType(req.getParameter("roomType"));
                res.setCheckInDate(LocalDate.parse(req.getParameter("checkInDate")));
                res.setCheckOutDate(LocalDate.parse(req.getParameter("checkOutDate")));
                reservationService.addReservation(res);
                message = "Reservation added! Number: " + res.getReservationNumber();

                // Send confirmation email asynchronously (don't block the response)
                final Reservation savedRes = res;
                new Thread(() -> {
                    try {
                        Guest guest = guestService.getGuestById(savedRes.getGuestId());
                        if (guest != null && guest.getEmail() != null && !guest.getEmail().isEmpty()) {
                            Room room = roomService.getRoomById(savedRes.getRoomId());
                            String roomNumber = room != null ? room.getRoomNumber() : savedRes.getRoomType();
                            long nights = java.time.temporal.ChronoUnit.DAYS.between(
                                    savedRes.getCheckInDate(), savedRes.getCheckOutDate());
                            double pricePerNight = room != null ? room.getPricePerNight() : 0;
                            String total = String.format("%.2f", nights * pricePerNight);
                            EmailService.sendReservationConfirmation(
                                    guest.getEmail(),
                                    guest.getName(),
                                    savedRes.getReservationNumber(),
                                    savedRes.getRoomType(),
                                    roomNumber,
                                    savedRes.getCheckInDate().toString(),
                                    savedRes.getCheckOutDate().toString(),
                                    (int) nights,
                                    total
                            );
                        }
                    } catch (Exception ex) {
                        System.err.println("Email send failed: " + ex.getMessage());
                    }
                }, "email-sender").start();

            } else if ("edit-reservation".equalsIgnoreCase(action)) {
                int resId = Integer.parseInt(req.getParameter("reservationId"));
                Reservation old = reservationService.getReservationById(resId);
                if (old == null) {
                    error = "Reservation not found!";
                } else {
                    Reservation res = new Reservation();
                    res.setReservationId(resId);
                    res.setGuestId(old.getGuestId());
                    res.setRoomType(req.getParameter("roomType"));
                    res.setRoomId(Integer.parseInt(req.getParameter("roomId")));
                    res.setCheckInDate(LocalDate.parse(req.getParameter("checkInDate")));
                    res.setCheckOutDate(LocalDate.parse(req.getParameter("checkOutDate")));
                    res.setStatus(old.getStatus());
                    res.setCreatedAt(old.getCreatedAt());
                    reservationService.updateReservation(res);
                    message = "Reservation updated successfully!";
                }

            } else if ("delete-reservation".equalsIgnoreCase(action)) {
                reservationService.deleteReservation(Integer.parseInt(req.getParameter("reservationId")));
                message = "Reservation deleted successfully!";
            }

        } catch (IllegalArgumentException e) {
            error = "Validation error: " + e.getMessage();
            e.printStackTrace();
        } catch (RuntimeException e) {
            error = "Database error: " + e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            error = "An error occurred: " + e.getMessage();
            e.printStackTrace();
        }

        req.setAttribute("message", message);
        req.setAttribute("error", error);
        req.getRequestDispatcher("/staff-dashboard.jsp").forward(req, resp);
    }
}
