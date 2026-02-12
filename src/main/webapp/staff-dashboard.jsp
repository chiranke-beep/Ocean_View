<%@ page import="java.util.List" %>
<%@ page import="com.example.ocean_view_resort.model.Reservation" %>
<%@ page import="com.example.ocean_view_resort.model.Guest" %>
<%@ page import="com.example.ocean_view_resort.model.Room" %>
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
    List<Room> rooms = (List<Room>) request.getAttribute("rooms");
    String message = (String) request.getAttribute("message");
    String error = (String) request.getAttribute("error");

    if (reservations == null) reservations = new java.util.ArrayList<>();
    if (guests == null) guests = new java.util.ArrayList<>();
    if (rooms == null) rooms = new java.util.ArrayList<>();
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

        .download-btn {
            background: #2196F3;
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 13px;
            text-decoration: none;
            display: inline-block;
            transition: background 0.3s ease;
            margin-right: 5px;
        }

        .download-btn:hover {
            background: #0b7dda;
        }

        .edit-btn {
            background: #ff9800;
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 13px;
            transition: background 0.3s ease;
            margin-right: 5px;
        }

        .edit-btn:hover {
            background: #e68900;
        }

        /* Modal Styles */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.4);
        }

        .modal-content {
            background-color: white;
            margin: 5% auto;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
            width: 90%;
            max-width: 600px;
            max-height: 80vh;
            overflow-y: auto;
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            border-bottom: 2px solid #f5ede0;
            padding-bottom: 15px;
        }

        .modal-header h2 {
            font-size: 20px;
            color: #333;
        }

        .modal-close {
            background: none;
            border: none;
            font-size: 28px;
            cursor: pointer;
            color: #999;
            transition: color 0.3s ease;
        }

        .modal-close:hover {
            color: #333;
        }

        .modal-body {
            margin-top: 20px;
        }

        .modal-footer {
            display: flex;
            gap: 10px;
            justify-content: flex-end;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }

        .cancel-btn {
            background: #999;
            color: white;
            border: none;
            padding: 10px 25px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            transition: background 0.3s ease;
        }

        .cancel-btn:hover {
            background: #777;
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
                            <select id="roomType" name="roomType" required onchange="updateRoomList()">
                                <option value="">-- Select Room Type --</option>
                                <option value="Single">Single</option>
                                <option value="Double">Double</option>
                                <option value="Deluxe">Deluxe</option>
                                <option value="Suite">Suite</option>
                                <option value="Presidential">Presidential</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="roomId">Select Room *</label>
                            <select id="roomId" name="roomId" required onchange="updatePrice()">
                                <option value="">-- Select a Room --</option>
                            </select>
                        </div>
                    </div>

                    <div id="priceDisplay" style="background: #f5ede0; padding: 15px 20px; border-radius: 6px; margin: 15px 0; font-size: 16px; font-weight: 600; color: #c49055; width: 100%; text-align: left; border-left: 4px solid #d4a574; display: none;"></div>

                    <div class="form-group">
                        <label for="checkInDate">Check-In Date *</label>
                        <input type="date" id="checkInDate" name="checkInDate" required onchange="updateRoomList()">
                    </div>

                    <div class="form-group">
                        <label for="checkOutDate">Check-Out Date *</label>
                        <input type="date" id="checkOutDate" name="checkOutDate" required onchange="updateRoomList()">
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
                                        <a href="<%= request.getContextPath() %>/download-bill?reservationId=<%= res.getReservationId() %>" class="download-btn" title="Download Bill">📥 Bill</a>
                                        <button type="button" class="edit-btn" onclick="openEditModal(<%= res.getReservationId() %>, <%= res.getGuestId() %>, '<%= res.getRoomType() %>', '<%= res.getCheckInDate() %>', '<%= res.getCheckOutDate() %>')" title="Edit Reservation">✏️ Edit</button>
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
    // Room data array
    const roomsData = [
        <% for (Room room : rooms) { %>
            {
                roomId: <%= room.getRoomId() %>,
                roomNumber: '<%= room.getRoomNumber() %>',
                roomType: '<%= room.getRoomType() %>',
                status: '<%= room.getStatus() %>',
                pricePerNight: <%= room.getPricePerNight() %>
            },
        <% } %>
    ];
    
    console.log('Total rooms loaded:', roomsData.length);
    console.log('Rooms Data:', roomsData);

    function updateRoomList() {
        const roomTypeSelect = document.getElementById('roomType');
        const roomIdSelect = document.getElementById('roomId');
        const priceDisplay = document.getElementById('priceDisplay');

        const selectedRoomType = roomTypeSelect.value;
        console.log('Selected Room Type:', selectedRoomType);
        console.log('Available room types:', [...new Set(roomsData.map(r => r.roomType))]);

        // Store currently selected room ID before clearing anything
        const previousRoomId = roomIdSelect.value;

        if (!selectedRoomType || roomsData.length === 0) {
            roomIdSelect.innerHTML = '<option value="">-- Select a Room --</option>';
            priceDisplay.innerHTML = '';
            priceDisplay.style.display = 'none';
            if (roomsData.length === 0) {
                roomIdSelect.innerHTML = '<option value="">No rooms exist in system</option>';
            }
            return;
        }

        // Filter rooms by type
        const availableRooms = roomsData.filter(room => room.roomType === selectedRoomType);
        console.log('Filtered rooms for type', selectedRoomType, ':', availableRooms);

        if (availableRooms.length === 0) {
            roomIdSelect.innerHTML = '<option value="">No rooms of this type available</option>';
            priceDisplay.innerHTML = '';
            priceDisplay.style.display = 'none';
            return;
        }

        // Check if previously selected room is still in available list
        const previousRoomStillAvailable = availableRooms.some(room => room.roomId == previousRoomId);
        let selectedRoomId = previousRoomStillAvailable ? previousRoomId : availableRooms[0].roomId;

        // Create options for all available rooms
        roomIdSelect.innerHTML = '<option value="">-- Select a Room --</option>';
        availableRooms.forEach(room => {
            const option = document.createElement('option');
            option.value = room.roomId;
            option.textContent = 'Room ' + room.roomNumber + ' - $' + room.pricePerNight + '/night (' + room.status + ')';
            option.setAttribute('data-price', room.pricePerNight);
            if (room.roomId == selectedRoomId) {
                option.selected = true;
            }
            roomIdSelect.appendChild(option);
        });

        // Show price for the selected room or first room
        if (selectedRoomId) {
            const selectedRoom = availableRooms.find(room => room.roomId == selectedRoomId);
            if (selectedRoom) {
                if (priceDisplay) {
                    priceDisplay.innerHTML = '💰 Price: $' + selectedRoom.pricePerNight + '/night';
                    priceDisplay.style.display = 'block';
                }
            }
        }
    }

    function updatePrice() {
        const roomIdSelect = document.getElementById('roomId');
        const priceDisplay = document.getElementById('priceDisplay');
        
        console.log('updatePrice called. roomId:', roomIdSelect.value);
        if (roomIdSelect.value) {
            const selectedOption = roomIdSelect.options[roomIdSelect.selectedIndex];
            const price = selectedOption.getAttribute('data-price');
            console.log('Selected option:', selectedOption.textContent);
            console.log('Price attribute:', price);
            if (priceDisplay && price) {
                const priceHTML = '💰 Price: $' + price + '/night';
                console.log('Setting price HTML to:', priceHTML);
                priceDisplay.innerHTML = priceHTML;
                priceDisplay.style.display = 'block';
            }
        } else {
            // Hide price display when no room selected
            if (priceDisplay) {
                priceDisplay.innerHTML = '';
                priceDisplay.style.display = 'none';
            }
        }
    }

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

    // Set minimum date to today for check-in
    const checkInInput = document.getElementById('checkInDate');
    if (checkInInput) {
        const today = new Date().toISOString().split('T')[0];
        checkInInput.min = today;
        
        // Set check-in to today initially
        checkInInput.value = today;
        
        // Auto-set checkout to tomorrow
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        document.getElementById('checkOutDate').value = tomorrow.toISOString().split('T')[0];
        
        // Update check-out date when check-in changes
        checkInInput.addEventListener('change', function() {
            const checkOutInput = document.getElementById('checkOutDate');
            // Set checkout to one day after checkin
            const nextDay = new Date(this.value);
            nextDay.setDate(nextDay.getDate() + 1);
            checkOutInput.value = nextDay.toISOString().split('T')[0];
            checkOutInput.min = this.value;
            updateRoomList();
        });
    }

    // Update room list when check-out date changes
    const checkOutInput = document.getElementById('checkOutDate');
    if (checkOutInput) {
        checkOutInput.addEventListener('change', updateRoomList);
    }

    // Update room list when room type changes
    const roomTypeSelect = document.getElementById('roomType');
    if (roomTypeSelect) {
        roomTypeSelect.addEventListener('change', updateRoomList);
    }

    // Update price when room is selected
    const roomIdSelect = document.getElementById('roomId');
    if (roomIdSelect) {
        roomIdSelect.addEventListener('change', updatePrice);
    }

    // Edit Modal Functions
    function openEditModal(reservationId, guestId, roomType, checkIn, checkOut) {
        const modal = document.getElementById('editModal');
        document.getElementById('editReservationId').value = reservationId;
        document.getElementById('editGuestId').value = guestId;
        document.getElementById('editRoomType').value = roomType;
        document.getElementById('editCheckInDate').value = checkIn;
        document.getElementById('editCheckOutDate').value = checkOut;
        
        // Reset room selection and price display
        document.getElementById('editRoomId').innerHTML = '<option value="">-- Select a Room --</option>';
        document.getElementById('editPriceDisplay').innerHTML = '';
        document.getElementById('editPriceDisplay').style.display = 'none';
        
        // Load rooms for the selected room type
        updateEditRoomList();
        
        modal.style.display = 'block';
    }

    function closeEditModal() {
        const modal = document.getElementById('editModal');
        modal.style.display = 'none';
    }

    function updateEditRoomList() {
        const roomTypeSelect = document.getElementById('editRoomType');
        const checkInInput = document.getElementById('editCheckInDate');
        const checkOutInput = document.getElementById('editCheckOutDate');
        const roomIdSelect = document.getElementById('editRoomId');
        const priceDisplay = document.getElementById('editPriceDisplay');

        const selectedRoomType = roomTypeSelect.value;
        const checkInDate = checkInInput.value;
        const checkOutDate = checkOutInput.value;

        if (!selectedRoomType || !checkInDate || !checkOutDate || roomsData.length === 0) {
            roomIdSelect.innerHTML = '<option value="">-- Select a Room --</option>';
            priceDisplay.innerHTML = '';
            priceDisplay.style.display = 'none';
            return;
        }

        // Filter rooms by type
        const availableRooms = roomsData.filter(room => room.roomType === selectedRoomType);

        if (availableRooms.length === 0) {
            roomIdSelect.innerHTML = '<option value="">No rooms of this type available</option>';
            priceDisplay.innerHTML = '';
            priceDisplay.style.display = 'none';
            return;
        }

        // Create options for all available rooms
        roomIdSelect.innerHTML = '<option value="">-- Select a Room --</option>';
        availableRooms.forEach(room => {
            const option = document.createElement('option');
            option.value = room.roomId;
            option.textContent = 'Room ' + room.roomNumber + ' - $' + room.pricePerNight + '/night (' + room.status + ')';
            option.setAttribute('data-price', room.pricePerNight);
            roomIdSelect.appendChild(option);
        });
    }

    function updateEditPrice() {
        const roomIdSelect = document.getElementById('editRoomId');
        const checkInInput = document.getElementById('editCheckInDate');
        const checkOutInput = document.getElementById('editCheckOutDate');
        const priceDisplay = document.getElementById('editPriceDisplay');
        const selectedOption = roomIdSelect.options[roomIdSelect.selectedIndex];

        if (!selectedOption.value) {
            priceDisplay.innerHTML = '';
            priceDisplay.style.display = 'none';
            return;
        }

        const pricePerNight = selectedOption.getAttribute('data-price');
        const checkIn = new Date(checkInInput.value);
        const checkOut = new Date(checkOutInput.value);
        const nights = Math.ceil((checkOut - checkIn) / (1000 * 60 * 60 * 24));

        if (nights > 0 && pricePerNight) {
            const totalPrice = (nights * parseFloat(pricePerNight)).toFixed(2);
            priceDisplay.innerHTML = '💰 Price: $' + pricePerNight + '/night × ' + nights + ' nights = <strong>$' + totalPrice + '</strong>';
            priceDisplay.style.display = 'block';
        } else {
            priceDisplay.innerHTML = '';
            priceDisplay.style.display = 'none';
        }
    }

    // Close modal when clicking outside
    window.onclick = function(event) {
        const editModal = document.getElementById('editModal');
        if (event.target === editModal) {
            editModal.style.display = 'none';
        }
    };

    // Add event listeners to edit modal inputs
    document.addEventListener('DOMContentLoaded', function() {
        const editCheckInInput = document.getElementById('editCheckInDate');
        if (editCheckInInput) {
            editCheckInInput.addEventListener('change', function() {
                const editCheckOutInput = document.getElementById('editCheckOutDate');
                const nextDay = new Date(this.value);
                nextDay.setDate(nextDay.getDate() + 1);
                editCheckOutInput.value = nextDay.toISOString().split('T')[0];
                editCheckOutInput.min = this.value;
                updateEditRoomList();
            });
        }

        const editCheckOutInput = document.getElementById('editCheckOutDate');
        if (editCheckOutInput) {
            editCheckOutInput.addEventListener('change', updateEditRoomList);
        }

        const editRoomTypeSelect = document.getElementById('editRoomType');
        if (editRoomTypeSelect) {
            editRoomTypeSelect.addEventListener('change', updateEditRoomList);
        }

        const editRoomIdSelect = document.getElementById('editRoomId');
        if (editRoomIdSelect) {
            editRoomIdSelect.addEventListener('change', updateEditPrice);
        }
    });
</script>

<!-- Edit Reservation Modal -->
<div id="editModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Edit Reservation</h2>
            <button class="modal-close" onclick="closeEditModal()">&times;</button>
        </div>
        <div class="modal-body">
            <form method="post" action="<%= request.getContextPath() %>/staff-dashboard" id="editForm">
                <input type="hidden" name="action" value="edit-reservation">
                <input type="hidden" id="editReservationId" name="reservationId">
                <input type="hidden" id="editGuestId" name="guestId">

                <div class="form-row">
                    <div class="form-group">
                        <label for="editRoomType">Room Type:</label>
                        <select id="editRoomType" name="roomType" required>
                            <option value="">-- Select Room Type --</option>
                            <option value="Single">Single</option>
                            <option value="Double">Double</option>
                            <option value="Deluxe">Deluxe</option>
                            <option value="Suite">Suite</option>
                            <option value="Presidential">Presidential</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="editRoomId">Room:</label>
                        <select id="editRoomId" name="roomId" required>
                            <option value="">-- Select a Room --</option>
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="editCheckInDate">Check-In Date:</label>
                        <input type="date" id="editCheckInDate" name="checkInDate" required>
                    </div>
                    <div class="form-group">
                        <label for="editCheckOutDate">Check-Out Date:</label>
                        <input type="date" id="editCheckOutDate" name="checkOutDate" required>
                    </div>
                </div>

                <div id="editPriceDisplay" style="padding: 15px; background: #f5ede0; border-radius: 5px; margin: 20px 0; border-left: 4px solid #c49055; display: none;"></div>
            </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="cancel-btn" onclick="closeEditModal()">Cancel</button>
            <button type="submit" form="editForm" class="submit-btn">Save Changes</button>
        </div>
    </div>
</div>

</body>
</html>
