<%@ page import="java.util.List" %>
<%@ page import="com.example.ocean_view_resort.model.Reservation" %>
<%@ page import="com.example.ocean_view_resort.model.Guest" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // Check if user is logged in
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
    if (username == null || role == null || !"Staff".equalsIgnoreCase(role)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
    List<Guest> guests = (List<Guest>) request.getAttribute("guests");
    String message = (String) request.getAttribute("message");
    String error = (String) request.getAttribute("error");

    if (reservations == null) reservations = new java.util.ArrayList<>();
    if (guests == null) guests = new java.util.ArrayList<>();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff Dashboard - Ocean View Resort</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #f5ede0 0%, #e8dcc8 100%);
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }

        /* Navbar */
        .navbar {
            background: linear-gradient(135deg, #d4a574 0%, #c49055 100%);
            color: white;
            padding: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .navbar h1 {
            font-size: 24px;
            font-weight: 600;
        }

        .navbar-right {
            display: flex;
            gap: 20px;
            align-items: center;
        }

        .username {
            font-size: 14px;
            opacity: 0.9;
        }

        .logout-btn {
            background: #b8785f;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
            transition: background 0.3s ease;
        }

        .logout-btn:hover {
            background: #a06c50;
        }

        /* Content */
        .content {
            padding: 30px;
        }

        .tabs {
            display: flex;
            gap: 10px;
            margin-bottom: 30px;
            border-bottom: 2px solid #e8dcc8;
        }

        .tab-button {
            background: none;
            border: none;
            padding: 15px 20px;
            font-size: 16px;
            font-weight: 500;
            color: #c49055;
            cursor: pointer;
            border-bottom: 3px solid transparent;
            transition: all 0.3s ease;
        }

        .tab-button.active {
            color: #d4a574;
            border-bottom-color: #d4a574;
        }

        .tab-button:hover {
            color: #d4a574;
        }

        .tab-content {
            display: none;
        }

        .tab-content.active {
            display: block;
        }

        /* Messages */
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            animation: slideDown 0.5s ease;
        }

        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        /* Forms */
        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            color: #333;
            font-weight: 500;
            font-size: 14px;
        }

        input, select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            font-family: 'Inter', sans-serif;
            transition: border-color 0.3s ease;
        }

        input:focus, select:focus {
            outline: none;
            border-color: #d4a574;
            box-shadow: 0 0 0 3px rgba(212, 165, 116, 0.1);
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .submit-btn {
            background: linear-gradient(135deg, #d4a574 0%, #c49055 100%);
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 600;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .submit-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }

        /* Tables */
        .table-container {
            overflow-x: auto;
            margin-top: 30px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
        }

        thead {
            background: #f5ede0;
        }

        th {
            padding: 15px;
            text-align: left;
            color: #333;
            font-weight: 600;
            border-bottom: 2px solid #ddd;
        }

        td {
            padding: 15px;
            border-bottom: 1px solid #eee;
        }

        tbody tr:hover {
            background: #f9f7f4;
        }

        .delete-btn {
            background: #d32f2f;
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 13px;
            transition: background 0.3s ease;
        }

        .delete-btn:hover {
            background: #b71c1c;
        }

        .empty-message {
            text-align: center;
            padding: 40px;
            color: #999;
            font-style: italic;
        }

        @media (max-width: 768px) {
            .form-row {
                grid-template-columns: 1fr;
            }

            .navbar {
                flex-direction: column;
                gap: 10px;
            }

            .navbar-right {
                width: 100%;
                justify-content: space-between;
            }

            table {
                font-size: 12px;
            }

            th, td {
                padding: 10px;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <!-- Navbar -->
    <div class="navbar">
        <h1>🏨 Ocean View Resort - Staff Dashboard</h1>
        <div class="navbar-right">
            <span class="username">Staff: <%= username %></span>
            <a href="<%= request.getContextPath() %>/logout" class="logout-btn">Logout</a>
        </div>
    </div>

    <!-- Content -->
    <div class="content">
        <!-- Messages -->
        <% if (message != null && !message.isEmpty()) { %>
            <div class="alert alert-success"><%= message %></div>
        <% } %>
        <% if (error != null && !error.isEmpty()) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>

        <!-- Tabs -->
        <div class="tabs">
            <button class="tab-button active" onclick="switchTab(event, 'guests-tab')">
                👥 Manage Guests
            </button>
            <button class="tab-button" onclick="switchTab(event, 'reservations-tab')">
                📋 Manage Reservations
            </button>
            <button class="tab-button" onclick="switchTab(event, 'view-tab')">
                👁️ View All Reservations
            </button>
        </div>

        <!-- Tab 1: Manage Guests -->
        <div id="guests-tab" class="tab-content active">
            <h2 style="color: #333; margin-bottom: 20px;">Add New Guest</h2>
            <form method="post" action="<%= request.getContextPath() %>/staff-dashboard">
                <input type="hidden" name="action" value="add-guest">

                <div class="form-group">
                    <label for="guestName">Guest Name *</label>
                    <input type="text" id="guestName" name="guestName" required minlength="3">
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="contactNumber">Contact Number *</label>
                        <input type="tel" id="contactNumber" name="contactNumber" required>
                    </div>
                    <div class="form-group">
                        <label for="address">Address *</label>
                        <input type="text" id="address" name="address" required minlength="5">
                    </div>
                </div>

                <button type="submit" class="submit-btn">✓ Add Guest</button>
            </form>

            <h2 style="color: #333; margin-top: 40px; margin-bottom: 20px;">All Guests</h2>
            <% if (guests.isEmpty()) { %>
                <div class="empty-message">No guests found. Start adding guests!</div>
            <% } else { %>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Guest ID</th>
                                <th>Guest Name</th>
                                <th>Contact</th>
                                <th>Address</th>
                                <th>Added On</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Guest guest : guests) { %>
                                <tr>
                                    <td><strong><%= guest.getGuestId() %></strong></td>
                                    <td><%= guest.getName() %></td>
                                    <td><%= guest.getContactNumber() %></td>
                                    <td><%= guest.getAddress() %></td>
                                    <td><%= guest.getCreatedAt() %></td>
                                    <td>
                                        <form method="post" action="<%= request.getContextPath() %>/staff-dashboard" style="display: inline;">
                                            <input type="hidden" name="action" value="delete-guest">
                                            <input type="hidden" name="guestId" value="<%= guest.getGuestId() %>">
                                            <button type="submit" class="delete-btn" onclick="return confirm('Are you sure?');">Delete</button>
                                        </form>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        </div>

        <!-- Tab 2: Add Reservation -->
        <div id="reservations-tab" class="tab-content">
            <h2 style="color: #333; margin-bottom: 20px;">Add New Reservation</h2>
            <% if (guests.isEmpty()) { %>
                <div class="alert alert-error">Please add guests first before creating reservations!</div>
            <% } else { %>
                <form method="post" action="<%= request.getContextPath() %>/staff-dashboard">
                    <input type="hidden" name="action" value="add-reservation">

                    <div class="form-group">
                        <label for="guestId">Select Guest *</label>
                        <select id="guestId" name="guestId" required>
                            <option value="">-- Select a Guest --</option>
                            <% for (Guest guest : guests) { %>
                                <option value="<%= guest.getGuestId() %>">
                                    <%= guest.getName() %> (<%= guest.getContactNumber() %>)
                                </option>
                            <% } %>
                        </select>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="roomType">Room Type *</label>
                            <select id="roomType" name="roomType" required>
                                <option value="">-- Select Room Type --</option>
                                <option value="Single">Single</option>
                                <option value="Double">Double</option>
                                <option value="Deluxe">Deluxe</option>
                                <option value="Suite">Suite</option>
                                <option value="Presidential">Presidential</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="checkInDate">Check-In Date *</label>
                            <input type="date" id="checkInDate" name="checkInDate" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="checkOutDate">Check-Out Date *</label>
                            <input type="date" id="checkOutDate" name="checkOutDate" required>
                        </div>
                    </div>

                    <button type="submit" class="submit-btn">✓ Add Reservation</button>
                </form>
            <% } %>
        </div>

        <!-- Tab 3: View All Reservations -->
        <div id="view-tab" class="tab-content">
            <h2 style="color: #333; margin-bottom: 20px;">All Reservations</h2>

            <% if (reservations.isEmpty()) { %>
                <div class="empty-message">No reservations found. Start adding new reservations!</div>
            <% } else { %>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Res. No.</th>
                                <th>Guest Name</th>
                                <th>Contact</th>
                                <th>Room Type</th>
                                <th>Check-In</th>
                                <th>Check-Out</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Reservation res : reservations) {
                                Guest resGuest = guests.stream()
                                    .filter(g -> g.getGuestId() == res.getGuestId())
                                    .findFirst()
                                    .orElse(null);
                            %>
                                <tr>
                                    <td><strong><%= res.getReservationNumber() %></strong></td>
                                    <td><%= resGuest != null ? resGuest.getName() : "N/A" %></td>
                                    <td><%= resGuest != null ? resGuest.getContactNumber() : "N/A" %></td>
                                    <td><%= res.getRoomType() %></td>
                                    <td><%= res.getCheckInDate() %></td>
                                    <td><%= res.getCheckOutDate() %></td>
                                    <td>
                                        <span style="background: #c3e6cb; color: #155724; padding: 5px 10px; border-radius: 3px; font-size: 12px;">
                                            <%= res.getStatus() %>
                                        </span>
                                    </td>
                                    <td>
                                        <form method="post" action="<%= request.getContextPath() %>/staff-dashboard" style="display: inline;">
                                            <input type="hidden" name="action" value="delete-reservation">
                                            <input type="hidden" name="reservationId" value="<%= res.getReservationId() %>">
                                            <button type="submit" class="delete-btn" onclick="return confirm('Are you sure?');">Delete</button>
                                        </form>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        </div>
    </div>
</div>

<script>
    function switchTab(event, tabName) {
        // Hide all tab contents
        const contents = document.querySelectorAll('.tab-content');
        contents.forEach(content => content.classList.remove('active'));

        // Remove active class from all buttons
        const buttons = document.querySelectorAll('.tab-button');
        buttons.forEach(button => button.classList.remove('active'));

        // Show the selected tab
        document.getElementById(tabName).classList.add('active');
        event.target.classList.add('active');
    }

    // Set minimum date to today for check-in (safely check if element exists)
    const checkInInput = document.getElementById('checkInDate');
    if (checkInInput) {
        checkInInput.min = new Date().toISOString().split('T')[0];
        
        // Update check-out date minimum whenever check-in date changes
        checkInInput.addEventListener('change', function() {
            const checkOutInput = document.getElementById('checkOutDate');
            if (checkOutInput) {
                checkOutInput.min = this.value;
                if (checkOutInput.value < this.value) {
                    checkOutInput.value = '';
                }
            }
        });
    }
</script>
</body>
</html>
