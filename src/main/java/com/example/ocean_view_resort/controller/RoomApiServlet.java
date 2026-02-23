package com.example.ocean_view_resort.controller;

import com.example.ocean_view_resort.model.Room;
import com.example.ocean_view_resort.service.RoomService;
import com.example.ocean_view_resort.service.impl.RoomServiceImpl;
import com.example.ocean_view_resort.utils.GsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

/**
 * REST API: Rooms
 *   GET /api/rooms                                                        → all rooms as JSON array
 *   GET /api/rooms?id=N                                                   → single room by ID
 *   GET /api/rooms?action=available&roomType=X&checkInDate=Y&checkOutDate=Z              → available rooms
 *   GET /api/rooms?action=available&roomType=X&checkInDate=Y&checkOutDate=Z&excludeReservationId=W → available rooms excluding one reservation
 */
@WebServlet(name = "RoomApiServlet", urlPatterns = "/api/rooms")
public class RoomApiServlet extends HttpServlet {

    private final RoomService roomService = new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String action = req.getParameter("action");

            if ("available".equalsIgnoreCase(action)) {
                // ── Available rooms query (replaces StaffDashboard's inline AJAX) ──
                String roomType      = req.getParameter("roomType");
                String checkInStr    = req.getParameter("checkInDate");
                String checkOutStr   = req.getParameter("checkOutDate");
                String excludeIdStr  = req.getParameter("excludeReservationId");

                if (roomType == null || checkInStr == null || checkOutStr == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"roomType, checkInDate and checkOutDate are required\"}");
                    out.flush();
                    return;
                }

                LocalDate checkIn  = LocalDate.parse(checkInStr);
                LocalDate checkOut = LocalDate.parse(checkOutStr);

                List<Room> rooms;
                if (excludeIdStr != null && !excludeIdStr.isEmpty()) {
                    int excludeId = Integer.parseInt(excludeIdStr);
                    rooms = roomService.getAvailableRoomsByTypeExcluding(roomType, checkIn, checkOut, excludeId);
                } else {
                    rooms = roomService.getAvailableRoomsByType(roomType, checkIn, checkOut);
                }

                out.print(GsonUtil.GSON.toJson(rooms));

            } else {
                // ── Single room by ID or full list ──
                String idParam = req.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    int id = Integer.parseInt(idParam);
                    Room room = roomService.getRoomById(id);
                    if (room == null) {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Room not found\"}");
                    } else {
                        out.print(GsonUtil.GSON.toJson(room));
                    }
                } else {
                    List<Room> rooms = roomService.getAllRooms();
                    out.print(GsonUtil.GSON.toJson(rooms));
                }
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid numeric parameter\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }

        out.flush();
    }
}
