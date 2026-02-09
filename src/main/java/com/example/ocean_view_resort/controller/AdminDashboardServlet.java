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

@WebServlet(name = "AdminDashboardServlet", value = "/admin-dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private StaffService staffService = new StaffServiceImpl();
    private RoomService roomService = new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        
        // Check if user is authenticated
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        
        if (action == null || action.isEmpty() || "home".equals(action)) {
            // Load dashboard data
            req.setAttribute("staffList", staffService.getAllStaff());
            req.setAttribute("roomList", roomService.getAllRooms());
            req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);
        } else if ("staff-list".equals(action)) {
            req.setAttribute("staffList", staffService.getAllStaff());
            req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);
        } else if ("room-list".equals(action)) {
            req.setAttribute("roomList", roomService.getAllRooms());
            req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");

        if ("add-staff".equals(action)) {
            String name = req.getParameter("name");
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String contactNumber = req.getParameter("contactNumber");
            String role = req.getParameter("role");

            boolean result = staffService.addStaff(name, username, password, contactNumber, role);
            req.setAttribute("message", result ? "Staff added successfully!" : "Failed to add staff. Username may already exist.");
            req.setAttribute("staffList", staffService.getAllStaff());
            req.setAttribute("roomList", roomService.getAllRooms());
            req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);
        } 
        else if ("update-staff".equals(action)) {
            int staffId = Integer.parseInt(req.getParameter("staffId"));
            String name = req.getParameter("name");
            String password = req.getParameter("password");
            String contactNumber = req.getParameter("contactNumber");
            String role = req.getParameter("role");

            boolean result = staffService.updateStaff(staffId, name, password, contactNumber, role);
            req.setAttribute("message", result ? "Staff updated successfully!" : "Failed to update staff.");
            req.setAttribute("staffList", staffService.getAllStaff());
            req.setAttribute("roomList", roomService.getAllRooms());
            req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);
        }
        else if ("delete-staff".equals(action)) {
            int staffId = Integer.parseInt(req.getParameter("staffId"));
            boolean result = staffService.deleteStaff(staffId);
            req.setAttribute("message", result ? "Staff deleted successfully!" : "Failed to delete staff.");
            req.setAttribute("roomList", roomService.getAllRooms());
            req.setAttribute("staffList", staffService.getAllStaff());
            req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);
        }
        else if ("add-room".equals(action)) {
            String roomNumber = req.getParameter("roomNumber");
            String roomType = req.getParameter("roomType");
            double pricePerNight = Double.parseDouble(req.getParameter("pricePerNight"));
            int capacity = Integer.parseInt(req.getParameter("capacity"));

            boolean result = roomService.addRoom(roomNumber, roomType, pricePerNight, capacity);
            req.setAttribute("staffList", staffService.getAllStaff());
            req.setAttribute("message", result ? "Room added successfully!" : "Failed to add room. Room number may already exist.");
            req.setAttribute("roomList", roomService.getAllRooms());
            req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);
        }
        else if ("update-room".equals(action)) {
            int roomId = Integer.parseInt(req.getParameter("roomId"));
            String roomNumber = req.getParameter("roomNumber");
            String roomType = req.getParameter("roomType");
            double pricePerNight = Double.parseDouble(req.getParameter("pricePerNight"));
            int capacity = Integer.parseInt(req.getParameter("capacity"));
            String status = req.getParameter("status");

            boolean result = roomService.updateRoom(roomId, roomNumber, roomType, pricePerNight, capacity, status);
            req.setAttribute("message", result ? "Room updated successfully!" : "Failed to update room.");
            req.setAttribute("staffList", staffService.getAllStaff());
            req.setAttribute("roomList", roomService.getAllRooms());
            req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);
        }
        else if ("delete-room".equals(action)) {
            int roomId = Integer.parseInt(req.getParameter("roomId"));
            boolean result = roomService.deleteRoom(roomId);
            req.setAttribute("message", result ? "Room deleted successfully!" : "Failed to delete room.");
            req.setAttribute("staffList", staffService.getAllStaff());
            req.setAttribute("roomList", roomService.getAllRooms());
            req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);
        }
    }
}
