package com.example.OceanView;

import com.example.ocean_view_resort.utils.DatabaseConnection;
import java.io.*;
import java.sql.Connection;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Welcome to Ocean View Resort";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");

        // Test database connection
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            if (conn != null && !conn.isClosed()) {
                out.println("<p style='color: green;'><strong>Database Connection: OK</strong></p>");
            } else {
                out.println("<p style='color: red;'><strong>Database Connection: FAILED</strong></p>");
            }
        } catch (Exception e) {
            out.println("<p style='color: red;'><strong>Database Connection Error: " + e.getMessage() + "</strong></p>");
        }

        out.println("</body></html>");
    }

    public void destroy() {
        // No-op: each connection is now opened and closed independently per request.
    }
}