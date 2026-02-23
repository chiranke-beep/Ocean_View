package com.example.ocean_view_resort.controller;

import com.example.ocean_view_resort.service.StaffService;
import com.example.ocean_view_resort.service.RoomService;
import com.example.ocean_view_resort.service.impl.StaffServiceImpl;
import com.example.ocean_view_resort.service.impl.RoomServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Handles the Admin Dashboard UI and Staff/Room CRUD.
 * Room list is now loaded client-side via /api/rooms.
 * Staff list is still server-side (no public Staff API).
 */
@WebServlet(name = "AdminDashboardServlet", value = "/admin-dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final StaffService staffService = new StaffServiceImpl();
    private final RoomService  roomService  = new RoomServiceImpl();

    private void forwardWithStaff(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("staffList", staffService.getAllStaff());
        req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Rooms are fetched client-side; always load staff for the table
        forwardWithStaff(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        String message;

        switch (action == null ? "" : action) {

            // ── Staff CRUD ────────────────────────────────────────────────────
            case "add-staff": {
                boolean ok = staffService.addStaff(
                        req.getParameter("name"),
                        req.getParameter("username"),
                        req.getParameter("password"),
                        req.getParameter("contactNumber"),
                        req.getParameter("role"));
                message = ok ? "Staff added successfully!" : "Failed to add staff. Username may already exist.";
                break;
            }
            case "edit-staff": {
                int staffId = Integer.parseInt(req.getParameter("staffId"));
                boolean ok = staffService.editStaff(
                        staffId,
                        req.getParameter("name"),
                        req.getParameter("contactNumber"),
                        req.getParameter("role"));
                message = ok ? "Staff updated successfully!" : "Failed to update staff.";
                break;
            }
            case "update-staff": {
                int staffId = Integer.parseInt(req.getParameter("staffId"));
                boolean ok = staffService.updateStaff(
                        staffId,
                        req.getParameter("name"),
                        req.getParameter("password"),
                        req.getParameter("contactNumber"),
                        req.getParameter("role"));
                message = ok ? "Staff updated successfully!" : "Failed to update staff.";
                break;
            }
            case "delete-staff": {
                boolean ok = staffService.deleteStaff(Integer.parseInt(req.getParameter("staffId")));
                message = ok ? "Staff deleted successfully!" : "Failed to delete staff.";
                break;
            }

            // ── Room CRUD ─────────────────────────────────────────────────────
            case "add-room": {
                boolean ok = roomService.addRoom(
                        req.getParameter("roomNumber"),
                        req.getParameter("roomType"),
                        Double.parseDouble(req.getParameter("pricePerNight")),
                        Integer.parseInt(req.getParameter("capacity")));
                message = ok ? "Room added successfully!" : "Failed to add room. Room number may already exist.";
                break;
            }
            case "update-room": {
                boolean ok = roomService.updateRoom(
                        Integer.parseInt(req.getParameter("roomId")),
                        req.getParameter("roomNumber"),
                        req.getParameter("roomType"),
                        Double.parseDouble(req.getParameter("pricePerNight")),
                        Integer.parseInt(req.getParameter("capacity")),
                        req.getParameter("status"));
                message = ok ? "Room updated successfully!" : "Failed to update room.";
                break;
            }
            case "delete-room": {
                boolean ok = roomService.deleteRoom(Integer.parseInt(req.getParameter("roomId")));
                message = ok ? "Room deleted successfully!" : "Failed to delete room.";
                break;
            }

            default:
                message = "";
        }

        req.setAttribute("message", message);
        forwardWithStaff(req, resp);
    }
}
