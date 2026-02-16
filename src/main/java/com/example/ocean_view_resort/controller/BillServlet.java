package com.example.ocean_view_resort.controller;

import com.example.ocean_view_resort.dao.impl.RoomDAOImpl;
import com.example.ocean_view_resort.model.Bill;
import com.example.ocean_view_resort.model.Guest;
import com.example.ocean_view_resort.model.Reservation;
import com.example.ocean_view_resort.service.impl.GuestServiceImpl;
import com.example.ocean_view_resort.service.impl.ReservationServiceImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "BillServlet", value = "/download-bill")
public class BillServlet extends HttpServlet {
    private final ReservationServiceImpl reservationService = new ReservationServiceImpl();
    private final GuestServiceImpl guestService = new GuestServiceImpl();
    private final RoomDAOImpl roomDAO = new RoomDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        try {
            int reservationId = Integer.parseInt(req.getParameter("reservationId"));
            
            // Get reservation details
            Reservation reservation = reservationService.getReservationById(reservationId);
            if (reservation == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Reservation not found");
                return;
            }

            // Get guest details
            Guest guest = guestService.getGuestById(reservation.getGuestId());
            if (guest == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Guest not found");
                return;
            }

            // Get room price - use actual room price if available, otherwise get average by room type
            BigDecimal pricePerNight;
            
            // Get room details if room_id exists
            String roomNumber = reservation.getRoomType(); // default to room type
            com.example.ocean_view_resort.model.Room room = null;
            
            if (reservation.getRoomId() > 0) {
                room = roomDAO.getRoomById(reservation.getRoomId());
                if (room != null) {
                    roomNumber = room.getRoomNumber();
                    pricePerNight = BigDecimal.valueOf(room.getPricePerNight());
                } else {
                    // Fallback to room type average if room not found
                    pricePerNight = roomDAO.getPriceByRoomType(reservation.getRoomType());
                }
            } else {
                // No room_id, use room type average
                pricePerNight = roomDAO.getPriceByRoomType(reservation.getRoomType());
            }

            // Calculate number of nights
            long nights = java.time.temporal.ChronoUnit.DAYS.between(
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate()
            );

            // Calculate total cost
            BigDecimal totalCost = pricePerNight.multiply(BigDecimal.valueOf(nights));

            // Create Bill object
            Bill bill = new Bill();
            bill.setReservationId(reservationId);
            bill.setGuestName(guest.getName());
            bill.setGuestContactNumber(guest.getContactNumber());
            bill.setRoomType(reservation.getRoomType());
            bill.setRoomNumber(roomNumber);
            bill.setCheckInDate(reservation.getCheckInDate());
            bill.setCheckOutDate(reservation.getCheckOutDate());
            bill.setNumberOfNights((int) nights);
            bill.setPricePerNight(pricePerNight);
            bill.setTotalCost(totalCost);

            // Generate PDF
            PDDocument document = generatePdfBill(bill, reservation.getReservationNumber());

            // Send as downloadable PDF
            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition", "attachment; filename=Bill_" + reservation.getReservationNumber() + ".pdf");

            OutputStream out = resp.getOutputStream();
            document.save(out);
            document.close();
            out.flush();
            out.close();

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid reservation ID");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating bill: " + e.getMessage());
        }
    }

    private PDDocument generatePdfBill(Bill bill, String reservationNumber) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        float margin = 40;
        float yPosition = pageHeight - margin;

        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        PDFont fontRegular = PDType1Font.HELVETICA;
        PDFont fontSmall = PDType1Font.HELVETICA;

        // Header - Company Name
        contentStream.setFont(fontBold, 24);
        yPosition -= 25;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("OCEAN VIEW RESORT");
        contentStream.endText();

        // Subtitle
        contentStream.setFont(fontRegular, 11);
        yPosition -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Premium Beachside Accommodation");
        contentStream.endText();

        // Divider line
        yPosition -= 10;
        contentStream.setLineWidth(1);
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(pageWidth - margin, yPosition);
        contentStream.stroke();

        // Invoice title
        contentStream.setFont(fontBold, 16);
        yPosition -= 25;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("INVOICE");
        contentStream.endText();

        // Invoice details
        contentStream.setFont(fontRegular, 10);
        yPosition -= 20;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Invoice No: " + reservationNumber);
        contentStream.endText();

        yPosition -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        contentStream.endText();

        // Guest Details Section
        yPosition -= 25;
        contentStream.setFont(fontBold, 11);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("GUEST INFORMATION");
        contentStream.endText();

        yPosition -= 15;
        contentStream.setFont(fontRegular, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Name: " + bill.getGuestName());
        contentStream.endText();

        yPosition -= 14;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Contact: " + bill.getGuestContactNumber());
        contentStream.endText();

        // Reservation Details Section
        yPosition -= 25;
        contentStream.setFont(fontBold, 11);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("RESERVATION DETAILS");
        contentStream.endText();

        yPosition -= 15;
        contentStream.setFont(fontRegular, 10);
        
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Room Number: " + bill.getRoomNumber());
        contentStream.endText();

        yPosition -= 14;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Room Type: " + bill.getRoomType());
        contentStream.endText();

        yPosition -= 14;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Check-In: " + bill.getCheckInDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        contentStream.endText();

        yPosition -= 14;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Check-Out: " + bill.getCheckOutDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        contentStream.endText();

        yPosition -= 14;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Number of Nights: " + bill.getNumberOfNights());
        contentStream.endText();

        // Bill Summary Section
        yPosition -= 25;
        contentStream.setFont(fontBold, 11);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("BILL SUMMARY");
        contentStream.endText();

        // Summary table headers
        yPosition -= 18;
        contentStream.setFont(fontBold, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Description");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 250, yPosition);
        contentStream.showText("Unit Price");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 350, yPosition);
        contentStream.showText("Quantity");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 430, yPosition);
        contentStream.showText("Amount");
        contentStream.endText();

        yPosition -= 10;
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(pageWidth - margin, yPosition);
        contentStream.stroke();

        // Summary details
        yPosition -= 18;
        contentStream.setFont(fontRegular, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Room Charges (per night)");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 250, yPosition);
        contentStream.showText("LKR " + String.format("%.2f", bill.getPricePerNight()));
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 350, yPosition);
        contentStream.showText(String.valueOf(bill.getNumberOfNights()));
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 430, yPosition);
        String amount = String.format("%.2f", bill.getPricePerNight().multiply(BigDecimal.valueOf(bill.getNumberOfNights())));
        contentStream.showText("LKR " + amount);
        contentStream.endText();

        yPosition -= 15;
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(pageWidth - margin, yPosition);
        contentStream.stroke();

        // Total
        yPosition -= 20;
        contentStream.setFont(fontBold, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 350, yPosition);
        contentStream.showText("TOTAL:");
        contentStream.endText();

        contentStream.setFont(fontBold, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 430, yPosition);
        contentStream.showText("LKR " + String.format("%.2f", bill.getTotalCost()));
        contentStream.endText();

        // Footer
        yPosition = 60;
        contentStream.setFont(fontSmall, 9);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Thank you for choosing Ocean View Resort!");
        contentStream.endText();

        yPosition -= 12;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Contact: info@oceanviewresort.com | Phone: +94 (0) 91 223 5333");
        contentStream.endText();

        contentStream.close();
        return document;
    }
}

