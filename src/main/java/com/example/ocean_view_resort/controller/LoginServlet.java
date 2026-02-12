package com.example.ocean_view_resort.controller;

import com.example.ocean_view_resort.utils.DatabaseConnection;
import com.example.ocean_view_resort.utils.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        // Ensure admin and staff tables and default credentials exist
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Create admin table
            String createAdmin = "CREATE TABLE IF NOT EXISTS admin (" +
                    "admin_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL" +
                    ") ENGINE=InnoDB";
            try (PreparedStatement ps = conn.prepareStatement(createAdmin)) {
                ps.execute();
            }

            // Create staff table
            String createStaff = "CREATE TABLE IF NOT EXISTS staff (" +
                    "staff_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "contact_number VARCHAR(20), " +
                    "role VARCHAR(50), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB";
            try (PreparedStatement ps = conn.prepareStatement(createStaff)) {
                ps.execute();
            }

            // Create guest table (if not exists - preserves data on restart)
            String createGuest = "CREATE TABLE IF NOT EXISTS guest (" +
                    "guest_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "address VARCHAR(255) NOT NULL, " +
                    "contact_number VARCHAR(20) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB";
            try (PreparedStatement ps = conn.prepareStatement(createGuest)) {
                ps.execute();
            }

            // Create reservation table (if not exists - preserves data on restart)
            String createReservation = "CREATE TABLE IF NOT EXISTS reservation (" +
                    "reservation_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "reservation_number VARCHAR(20) UNIQUE NOT NULL, " +
                    "guest_id INT NOT NULL, " +
                    "room_id INT, " +
                    "room_type VARCHAR(50) NOT NULL, " +
                    "check_in_date DATE NOT NULL, " +
                    "check_out_date DATE NOT NULL, " +
                    "status VARCHAR(50) DEFAULT 'Confirmed', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (guest_id) REFERENCES guest(guest_id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE SET NULL" +
                    ") ENGINE=InnoDB";
            try (PreparedStatement ps = conn.prepareStatement(createReservation)) {
                ps.execute();
            }

            // Add room_id column to existing reservation table if it doesn't exist
            try {
                String alterReservation = "ALTER TABLE reservation ADD COLUMN room_id INT, " +
                        "ADD FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE SET NULL";
                try (PreparedStatement ps = conn.prepareStatement(alterReservation)) {
                    ps.execute();
                }
            } catch (Exception e) {
                // Column already exists or other error - ignore
            }

            // Insert default admin if not exists
            String checkAdmin = "SELECT COUNT(*) FROM admin WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkAdmin)) {
                ps.setString(1, "admin");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        String insertAdmin = "INSERT INTO admin (username, password) VALUES (?, ?)";
                        try (PreparedStatement ips = conn.prepareStatement(insertAdmin)) {
                            ips.setString(1, "admin");
                            ips.setString(2, PasswordUtil.hash("chiran123"));
                            ips.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException("Failed to initialize login data", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                req.setAttribute("error", "Username and password are required.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            String hashed = PasswordUtil.hash(password);
            boolean authenticated = false;

            if ("Admin".equalsIgnoreCase(role)) {
                try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
                    String sql = "SELECT password FROM admin WHERE username = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, username);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                String stored = rs.getString("password");
                                if (stored != null && stored.equals(hashed)) {
                                    authenticated = true;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new ServletException(e);
                }
            } else if ("Staff".equalsIgnoreCase(role)) {
                try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
                    String sql = "SELECT password FROM staff WHERE username = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, username);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                String stored = rs.getString("password");
                                if (stored != null && stored.equals(hashed)) {
                                    authenticated = true;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new ServletException(e);
                }
            }

            if (authenticated) {
                HttpSession session = req.getSession(true);
                session.setAttribute("username", username);
                session.setAttribute("role", role);
                
                // Redirect based on role
                if ("Staff".equalsIgnoreCase(role)) {
                    resp.sendRedirect(req.getContextPath() + "/staff-dashboard");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/admin-dashboard");
                }
            } else {
                req.setAttribute("error", "Invalid credentials.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        }
    }
}
