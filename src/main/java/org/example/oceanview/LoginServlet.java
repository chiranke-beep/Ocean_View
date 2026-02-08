package org.example.oceanview;

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
        // Ensure admin table and default admin exist
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            String create = "CREATE TABLE IF NOT EXISTS admin (" +
                    "admin_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL" +
                    ") ENGINE=InnoDB";
            try (PreparedStatement ps = conn.prepareStatement(create)) {
                ps.execute();
            }

            // insert default admin if not exists (username: admin, password: chiran123)
            String check = "SELECT COUNT(*) FROM admin WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(check)) {
                ps.setString(1, "admin");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        if (count == 0) {
                            String insert = "INSERT INTO admin (username, password) VALUES (?, ?)";
                            try (PreparedStatement ips = conn.prepareStatement(insert)) {
                                ips.setString(1, "admin");
                                ips.setString(2, PasswordUtil.hash("chiran123"));
                                ips.executeUpdate();
                            }
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
            } else {
                // For now, other roles not yet implemented server-side; show message
                req.setAttribute("error", "Role login for '" + role + "' is not implemented yet.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            if (authenticated) {
                HttpSession session = req.getSession(true);
                session.setAttribute("username", username);
                session.setAttribute("role", role);
                resp.sendRedirect(req.getContextPath() + "/");
            } else {
                req.setAttribute("error", "Invalid credentials.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        }
    }
}
