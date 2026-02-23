package com.example.ocean_view_resort.controller;

import com.example.ocean_view_resort.model.Reservation;
import com.example.ocean_view_resort.service.ReservationService;
import com.example.ocean_view_resort.service.impl.ReservationServiceImpl;
import com.example.ocean_view_resort.utils.GsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * REST API: Reservations
 *   GET /api/reservations        → all reservations as JSON array
 *   GET /api/reservations?id=N   → single reservation by ID
 */
@WebServlet(name = "ReservationApiServlet", urlPatterns = "/api/reservations")
public class ReservationApiServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                Reservation reservation = reservationService.getReservationById(id);
                if (reservation == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Reservation not found\"}");
                } else {
                    out.print(GsonUtil.GSON.toJson(reservation));
                }
            } else {
                List<Reservation> reservations = reservationService.getAllReservations();
                out.print(GsonUtil.GSON.toJson(reservations));
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid id parameter\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }

        out.flush();
    }
}
