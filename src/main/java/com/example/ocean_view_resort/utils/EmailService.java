package com.example.ocean_view_resort.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Sends HTML emails via Gmail SMTP.
 * Set SENDER_EMAIL and SENDER_PASSWORD to your Gmail address and App Password.
 * (Google account → Security → 2-Step Verification → App Passwords)
 */
public class EmailService {

    // ── CONFIGURE THESE ──────────────────────────────────────────────────────
    private static final String SMTP_HOST     = "smtp.gmail.com";
    private static final int    SMTP_PORT     = 587;
    private static final String SENDER_EMAIL  = "chiranke@gmail.com";   // ← change
    private static final String SENDER_PASSWORD = "xlwzgnwqkhdefeos";    // ← change
    // ─────────────────────────────────────────────────────────────────────────

    public static void sendReservationConfirmation(
            String toEmail,
            String guestName,
            String reservationNumber,
            String roomType,
            String roomNumber,
            String checkInDate,
            String checkOutDate,
            int nights,
            String totalCost) {

        if (toEmail == null || toEmail.trim().isEmpty()) {
            System.out.println("No email address for guest – skipping email.");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "Ocean View Resort"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Booking Confirmation " + reservationNumber);

            String html = "<div style='font-family:Arial,sans-serif;max-width:600px;margin:auto;border:1px solid #ddd;border-radius:8px;overflow:hidden;'>"
                + "<div style='background:#1a3c5e;padding:24px;text-align:center;'>"
                + "<h1 style='color:#fff;margin:0;font-size:22px;'>OCEAN VIEW RESORT</h1>"
                + "<p style='color:#aac8e8;margin:4px 0 0;font-size:13px;'>Premium Beachside Accommodation</p>"
                + "</div>"
                + "<div style='padding:28px;'>"
                + "<h2 style='color:#1a3c5e;margin-top:0;'>Booking Confirmed! 🎉</h2>"
                + "<p>Dear <strong>" + guestName + "</strong>,</p>"
                + "<p>Your reservation has been successfully created. Here are your booking details:</p>"
                + "<table style='width:100%;border-collapse:collapse;margin:16px 0;'>"
                + row("Reservation No.", "<strong>" + reservationNumber + "</strong>")
                + row("Room Number", roomNumber)
                + row("Room Type", roomType)
                + row("Check-In Date", checkInDate)
                + row("Check-Out Date", checkOutDate)
                + row("Duration", nights + " night" + (nights != 1 ? "s" : ""))
                + row("Total Cost", "<strong>LKR " + totalCost + "</strong>")
                + "</table>"
                + "<p style='color:#555;font-size:13px;'>If you have any questions, please contact us at "
                + "<a href='mailto:info@oceanviewresort.com'>info@oceanviewresort.com</a> "
                + "or call <strong>+94 (0) 91 223 5333</strong>.</p>"
                + "<p style='margin-bottom:0;'>Thank you for choosing Ocean View Resort!</p>"
                + "</div>"
                + "<div style='background:#f5f5f5;padding:14px;text-align:center;font-size:12px;color:#888;'>"
                + "© 2025 Ocean View Resort. All rights reserved."
                + "</div>"
                + "</div>";

            message.setContent(html, "text/html; charset=UTF-8");
            Transport.send(message);
            System.out.println("Confirmation email sent to: " + toEmail);

        } catch (Exception e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String row(String label, String value) {
        return "<tr>"
            + "<td style='padding:8px 12px;background:#f0f4f8;border:1px solid #dce6f0;color:#555;width:40%;'>" + label + "</td>"
            + "<td style='padding:8px 12px;border:1px solid #dce6f0;'>" + value + "</td>"
            + "</tr>";
    }
}
