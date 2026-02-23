package com.example.ocean_view_resort.controller;

import com.example.ocean_view_resort.model.Guest;
import com.example.ocean_view_resort.service.GuestService;
import com.example.ocean_view_resort.service.impl.GuestServiceImpl;
import com.example.ocean_view_resort.utils.GsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * REST API: Guests
 *   GET /api/guests        → all guests as JSON array
 *   GET /api/guests?id=N   → single guest by ID
 */
@WebServlet(name = "GuestApiServlet", urlPatterns = "/api/guests")
public class GuestApiServlet extends HttpServlet {

    private final GuestService guestService = new GuestServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                Guest guest = guestService.getGuestById(id);
                if (guest == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(GsonUtil.GSON.toJson(java.util.Collections.singletonMap("error", "Guest not found")));
                } else {
                    out.print(GsonUtil.GSON.toJson(guest));
                }
            } else {
                List<Guest> guests = guestService.getAllGuests();
                out.print(GsonUtil.GSON.toJson(guests));
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(GsonUtil.GSON.toJson(java.util.Collections.singletonMap("error", "Invalid id parameter")));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(GsonUtil.GSON.toJson(java.util.Collections.singletonMap("error", e.getMessage())));
        }

        out.flush();
    }
}

