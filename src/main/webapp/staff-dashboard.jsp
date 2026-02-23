<%--@formatter:off--%>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <% String username=(String) session.getAttribute("username"); String role=(String) session.getAttribute("role");
            if (username==null || role==null || !"Staff".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp" ); return; } String message=(String)
            request.getAttribute("message"); String error=(String) request.getAttribute("error"); String
            ctx=request.getContextPath(); %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Staff Dashboard - Ocean View Resort</title>
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
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
                        background: #fff;
                        border-radius: 10px;
                        box-shadow: 0 10px 30px rgba(0, 0, 0, .1);
                        overflow: hidden;
                    }

                    .navbar {
                        background: linear-gradient(135deg, #d4a574 0%, #c49055 100%);
                        color: #fff;
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
                        opacity: .9;
                    }

                    .logout-btn {
                        background: #b8785f;
                        color: #fff;
                        border: none;
                        padding: 10px 20px;
                        border-radius: 5px;
                        cursor: pointer;
                        font-size: 14px;
                        text-decoration: none;
                        transition: background .3s;
                    }

                    .logout-btn:hover {
                        background: #a06c50;
                    }

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
                        transition: all .3s;
                    }

                    .tab-button.active,
                    .tab-button:hover {
                        color: #d4a574;
                        border-bottom-color: #d4a574;
                    }

                    .tab-content {
                        display: none;
                    }

                    .tab-content.active {
                        display: block;
                    }

                    .alert {
                        padding: 15px;
                        margin-bottom: 20px;
                        border-radius: 5px;
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

                    input,
                    select {
                        width: 100%;
                        padding: 12px;
                        border: 1px solid #ddd;
                        border-radius: 5px;
                        font-size: 14px;
                        font-family: 'Inter', sans-serif;
                        transition: border-color .3s;
                    }

                    input:focus,
                    select:focus {
                        outline: none;
                        border-color: #d4a574;
                        box-shadow: 0 0 0 3px rgba(212, 165, 116, .1);
                    }

                    .form-row {
                        display: grid;
                        grid-template-columns: 1fr 1fr;
                        gap: 20px;
                    }

                    .submit-btn {
                        background: linear-gradient(135deg, #d4a574 0%, #c49055 100%);
                        color: #fff;
                        border: none;
                        padding: 12px 30px;
                        border-radius: 5px;
                        cursor: pointer;
                        font-size: 14px;
                        font-weight: 600;
                        transition: transform .2s, box-shadow .2s;
                    }

                    .submit-btn:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 5px 15px rgba(0, 0, 0, .2);
                    }

                    .table-container {
                        overflow-x: auto;
                        margin-top: 30px;
                    }

                    table {
                        width: 100%;
                        border-collapse: collapse;
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
                        color: #fff;
                        border: none;
                        padding: 8px 15px;
                        border-radius: 4px;
                        cursor: pointer;
                        font-size: 13px;
                        transition: background .3s;
                    }

                    .delete-btn:hover {
                        background: #b71c1c;
                    }

                    .download-btn {
                        background: #2196F3;
                        color: #fff;
                        border: none;
                        padding: 8px 15px;
                        border-radius: 4px;
                        cursor: pointer;
                        font-size: 13px;
                        text-decoration: none;
                        display: inline-block;
                        margin-right: 5px;
                        transition: background .3s;
                    }

                    .download-btn:hover {
                        background: #0b7dda;
                    }

                    .edit-btn {
                        background: #ff9800;
                        color: #fff;
                        border: none;
                        padding: 8px 15px;
                        border-radius: 4px;
                        cursor: pointer;
                        font-size: 13px;
                        margin-right: 5px;
                        transition: background .3s;
                    }

                    .edit-btn:hover {
                        background: #e68900;
                    }

                    .action-btns {
                        display: flex;
                        gap: 6px;
                        align-items: center;
                        flex-wrap: wrap;
                    }

                    .empty-message {
                        text-align: center;
                        padding: 40px;
                        color: #999;
                        font-style: italic;
                    }

                    .modal {
                        display: none;
                        position: fixed;
                        z-index: 1000;
                        left: 0;
                        top: 0;
                        width: 100%;
                        height: 100%;
                        background: rgba(0, 0, 0, .4);
                    }

                    .modal-content {
                        background: #fff;
                        margin: 5% auto;
                        padding: 30px;
                        border-radius: 10px;
                        box-shadow: 0 10px 40px rgba(0, 0, 0, .3);
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
                    }

                    .modal-close:hover {
                        color: #333;
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
                        color: #fff;
                        border: none;
                        padding: 10px 25px;
                        border-radius: 5px;
                        cursor: pointer;
                        font-size: 14px;
                    }

                    .cancel-btn:hover {
                        background: #777;
                    }

                    #priceDisplay,
                    #editPriceDisplay {
                        background: #f5ede0;
                        padding: 15px 20px;
                        border-radius: 6px;
                        margin: 15px 0;
                        font-size: 16px;
                        font-weight: 600;
                        color: #c49055;
                        border-left: 4px solid #d4a574;
                        display: none;
                    }

                    @media(max-width:768px) {
                        .form-row {
                            grid-template-columns: 1fr;
                        }

                        .navbar {
                            flex-direction: column;
                            gap: 10px;
                        }

                        table {
                            font-size: 12px;
                        }

                        th,
                        td {
                            padding: 10px;
                        }
                    }
                </style>
            </head>

            <body>
                <div class="container">
                    <div class="navbar">
                        <h1>&#127968; Ocean View Resort - Staff Dashboard</h1>
                        <div class="navbar-right">
                            <span class="username">Staff: <%= username %></span>
                            <a href="<%= ctx %>/logout" class="logout-btn">Logout</a>
                        </div>
                    </div>

                    <div class="content">
                        <% if (message !=null && !message.isEmpty()) { %>
                            <div class="alert alert-success">
                                <%= message %>
                            </div>
                            <% } %>
                                <% if (error !=null && !error.isEmpty()) { %>
                                    <div class="alert alert-error">
                                        <%= error %>
                                    </div>
                                    <% } %>

                                        <div class="tabs">
                                            <button class="tab-button active"
                                                onclick="switchTab(event,'guests-tab')">&#128101; Manage Guests</button>
                                            <button class="tab-button"
                                                onclick="switchTab(event,'reservations-tab')">&#128203; Manage
                                                Reservations</button>
                                            <button class="tab-button" onclick="switchTab(event,'view-tab')">&#128065;
                                                View All Reservations</button>
                                            <button class="tab-button" onclick="switchTab(event,'help-tab')">&#10067;
                                                Help &amp; Instructions</button>
                                        </div>

                                        <!-- Tab 1: Manage Guests -->
                                        <div id="guests-tab" class="tab-content active">
                                            <h2 style="color:#333;margin-bottom:20px;">Add New Guest</h2>
                                            <form method="post" action="<%= ctx %>/staff-dashboard">
                                                <input type="hidden" name="action" value="add-guest">
                                                <div class="form-group">
                                                    <label for="guestName">Guest Name *</label>
                                                    <input type="text" id="guestName" name="guestName" required
                                                        minlength="3">
                                                </div>
                                                <div class="form-row">
                                                    <div class="form-group">
                                                        <label for="contactNumber">Contact Number *</label>
                                                        <input type="tel" id="contactNumber" name="contactNumber"
                                                            required>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="address">Address *</label>
                                                        <input type="text" id="address" name="address" required
                                                            minlength="5">
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="email">Email Address</label>
                                                    <input type="email" id="email" name="email"
                                                        placeholder="guest@example.com">
                                                </div>
                                                <button type="submit" class="submit-btn">&#10003; Add Guest</button>
                                            </form>

                                            <h2 style="color:#333;margin-top:40px;margin-bottom:20px;">All Guests</h2>
                                            <div class="table-container">
                                                <table>
                                                    <thead>
                                                        <tr>
                                                            <th>Guest ID</th>
                                                            <th>Guest Name</th>
                                                            <th>Contact</th>
                                                            <th>Email</th>
                                                            <th>Address</th>
                                                            <th>Added On</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody id="guestsTableBody">
                                                        <tr>
                                                            <td colspan="7" style="text-align:center;color:#999;">
                                                                Loading guests...</td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>

                                        <!-- Tab 2: Add Reservation -->
                                        <div id="reservations-tab" class="tab-content">
                                            <h2 style="color:#333;margin-bottom:20px;">Add New Reservation</h2>
                                            <form method="post" action="<%= ctx %>/staff-dashboard">
                                                <input type="hidden" name="action" value="add-reservation">
                                                <div class="form-group">
                                                    <label for="guestId">Select Guest *</label>
                                                    <select id="guestId" name="guestId" required>
                                                        <option value="">-- Loading guests... --</option>
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
                                                        <label for="roomId">Select Room *</label>
                                                        <select id="roomId" name="roomId" required>
                                                            <option value="">-- Select a Room --</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div id="priceDisplay"><!-- price --></div>
                                                <div class="form-row">
                                                    <div class="form-group">
                                                        <label for="checkInDate">Check-In Date *</label>
                                                        <input type="date" id="checkInDate" name="checkInDate" required>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="checkOutDate">Check-Out Date *</label>
                                                        <input type="date" id="checkOutDate" name="checkOutDate"
                                                            required>
                                                    </div>
                                                </div>
                                                <button type="submit" class="submit-btn">&#10003; Add
                                                    Reservation</button>
                                            </form>
                                        </div>

                                        <!-- Tab 3: View All Reservations -->
                                        <div id="view-tab" class="tab-content">
                                            <h2 style="color:#333;margin-bottom:20px;">All Reservations</h2>
                                            <div class="table-container">
                                                <table>
                                                    <thead>
                                                        <tr>
                                                            <th>Res. No.</th>
                                                            <th>Guest Name</th>
                                                            <th>Contact</th>
                                                            <th>Email</th>
                                                            <th>Room Type</th>
                                                            <th>Check-In</th>
                                                            <th>Check-Out</th>
                                                            <th>Status</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody id="reservationsTableBody">
                                                        <tr>
                                                            <td colspan="8" style="text-align:center;color:#999;">
                                                                Loading reservations...</td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>

                                        <!-- Tab 4: Help -->
                                        <div id="help-tab" class="tab-content">
                                            <h2 style="color:#333;margin-bottom:10px;">&#128214; Help &amp; Instructions
                                            </h2>
                                            <p style="color:#666;margin-bottom:25px;font-size:14px;">Welcome! This guide
                                                will help you efficiently manage guest reservations.</p>

                                            <div
                                                style="margin-bottom:25px;padding:20px;background:#f5ede0;border-left:4px solid #d4a574;border-radius:5px;">
                                                <h3 style="color:#c49055;margin-bottom:12px;">1&#65039;&#8419; Managing
                                                    Guests</h3>
                                                <ul style="color:#333;margin-left:20px;line-height:1.8;">
                                                    <li><strong>Click "Manage Guests" tab</strong> to access the guest
                                                        management section</li>
                                                    <li><strong>Guest Name:</strong> Enter the full name (minimum 3
                                                        characters)</li>
                                                    <li><strong>Contact Number:</strong> Phone number for communication
                                                    </li>
                                                    <li><strong>Address:</strong> Complete residential address (minimum
                                                        5 characters)</li>
                                                    <li><strong>Delete Guest:</strong> Use the Delete button to remove a
                                                        guest</li>
                                                </ul>
                                            </div>

                                            <div
                                                style="margin-bottom:25px;padding:20px;background:#f5ede0;border-left:4px solid #d4a574;border-radius:5px;">
                                                <h3 style="color:#c49055;margin-bottom:12px;">2&#65039;&#8419; Making
                                                    Reservations</h3>
                                                <ol style="color:#333;margin-left:20px;line-height:1.9;">
                                                    <li>Go to <strong>"Manage Reservations"</strong> tab</li>
                                                    <li>Select a guest from the dropdown (add guests first if empty)
                                                    </li>
                                                    <li>Choose room type, check-in and check-out dates</li>
                                                    <li>Available rooms load automatically based on type and dates</li>
                                                    <li>Select a room and click <strong>"&#10003; Add
                                                            Reservation"</strong></li>
                                                </ol>
                                            </div>

                                            <div
                                                style="margin-bottom:25px;padding:20px;background:#f5ede0;border-left:4px solid #d4a574;border-radius:5px;">
                                                <h3 style="color:#c49055;margin-bottom:12px;">3&#65039;&#8419; Viewing
                                                    &amp; Managing Reservations</h3>
                                                <ul style="color:#333;margin-left:20px;line-height:1.8;">
                                                    <li><strong>&#128229; Bill:</strong> Download a PDF bill for any
                                                        reservation</li>
                                                    <li><strong>&#9999;&#65039; Edit:</strong> Modify room, dates, or
                                                        other details</li>
                                                    <li><strong>Delete:</strong> Cancel the reservation completely</li>
                                                </ul>
                                            </div>

                                            <div
                                                style="padding:15px;background:#d4edda;border:1px solid #c3e6cb;border-radius:5px;color:#155724;">
                                                <strong>Need more help?</strong> Contact the management team or refer to
                                                the hotel operations manual.
                                            </div>
                                        </div>
                    </div>
                </div>

                <!-- Edit Reservation Modal -->
                <div id="editModal" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2>Edit Reservation</h2>
                            <button class="modal-close" onclick="closeEditModal()">&times;</button>
                        </div>
                        <div class="modal-body">
                            <form method="post" action="<%= ctx %>/staff-dashboard" id="editForm">
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
                                        <label for="editRoomId">Room (LKR):</label>
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
                                <div id="editPriceDisplay"><!-- price --></div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="cancel-btn" onclick="closeEditModal()">Cancel</button>
                            <button type="submit" form="editForm" class="submit-btn">Save Changes</button>
                        </div>
                    </div>
                </div>

                <!-- Edit Guest Modal -->
                <div id="editGuestModal" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2>Edit Guest</h2>
                            <button class="modal-close" onclick="closeEditGuestModal()">&times;</button>
                        </div>
                        <div class="modal-body">
                            <form method="post" action="<%= ctx %>/staff-dashboard" id="editGuestForm">
                                <input type="hidden" name="action" value="edit-guest">
                                <input type="hidden" id="editGuestIdField" name="guestId">
                                <div class="form-group">
                                    <label for="editGuestNameField">Guest Name:</label>
                                    <input type="text" id="editGuestNameField" name="guestName" required minlength="3">
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="editContactNumberField">Contact Number:</label>
                                        <input type="tel" id="editContactNumberField" name="contactNumber" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="editAddressField">Address:</label>
                                        <input type="text" id="editAddressField" name="address" required minlength="5">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="editEmailField">Email Address:</label>
                                    <input type="email" id="editEmailField" name="email"
                                        placeholder="guest@example.com">
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="cancel-btn" onclick="closeEditGuestModal()">Cancel</button>
                            <button type="submit" form="editGuestForm" class="submit-btn">Save Changes</button>
                        </div>
                    </div>
                </div>

                <script>
                    var CTX = '<%= ctx %>';
                    var currentEditRoomId = null;

                    function escHtml(s) {
                        return String(s).replace(/\\/g, '\\\\').replace(/'/g, "\\'").replace(/"/g, '&quot;');
                    }

                    function switchTab(ev, tabName) {
                        document.querySelectorAll('.tab-content').forEach(function (c) { c.classList.remove('active'); });
                        document.querySelectorAll('.tab-button').forEach(function (b) { b.classList.remove('active'); });
                        document.getElementById(tabName).classList.add('active');
                        ev.target.classList.add('active');
                    }

                    // ─── Guests ───────────────────────────────────────────────────────────
                    function loadGuests() {
                        fetch(CTX + '/api/guests')
                            .then(function (r) { return r.json(); })
                            .then(function (guests) {
                                renderGuestsTable(guests);
                                renderGuestDropdown(guests);
                            })
                            .catch(function (err) {
                                console.error('Failed to load guests:', err);
                                document.getElementById('guestsTableBody').innerHTML =
                                    '<tr><td colspan="7" style="text-align:center;color:red;">Failed to load guests.</td></tr>';
                            });
                    }

                    function renderGuestsTable(guests) {
                        var tbody = document.getElementById('guestsTableBody');
                        if (!guests || guests.length === 0) {
                            tbody.innerHTML = '<tr><td colspan="7" class="empty-message">No guests found. Start adding guests!</td></tr>';
                            return;
                        }
                        var html = '';
                        for (var i = 0; i < guests.length; i++) {
                            var g = guests[i];
                            var ts = g.createdAt ? g.createdAt.replace('T', ' ').substring(0, 19) : '';
                            var email = g.email || '';
                            html += '<tr>' +
                                '<td><strong>' + g.guestId + '</strong></td>' +
                                '<td>' + g.name + '</td>' +
                                '<td>' + g.contactNumber + '</td>' +
                                '<td>' + (email ? '<a href="mailto:' + email + '">' + email + '</a>' : '<span style="color:#bbb;">—</span>') + '</td>' +
                                '<td>' + g.address + '</td>' +
                                '<td>' + ts + '</td>' +
                                '<td><div class="action-btns">' +
                                '<button type="button" class="edit-btn" onclick="openEditGuestModal(' + g.guestId + ',\'' + escHtml(g.name) + '\',\'' + escHtml(g.contactNumber) + '\',\'' + escHtml(g.address) + '\',\'' + escHtml(email) + '\')">&#9999;&#65039; Edit</button>' +
                                '<form method="post" action="' + CTX + '/staff-dashboard" style="display:inline;">' +
                                '<input type="hidden" name="action" value="delete-guest">' +
                                '<input type="hidden" name="guestId" value="' + g.guestId + '">' +
                                '<button type="submit" class="delete-btn" onclick="return confirm(\'Are you sure?\');">Delete</button>' +
                                '</form></div></td></tr>';
                        }
                        tbody.innerHTML = html;
                    }

                    function renderGuestDropdown(guests) {
                        var sel = document.getElementById('guestId');
                        if (!sel) return;
                        var prev = sel.value;
                        sel.innerHTML = '<option value="">-- Select a Guest --</option>';
                        if (!guests || guests.length === 0) {
                            sel.innerHTML = '<option value="">No guests available - add one first</option>';
                            return;
                        }
                        for (var i = 0; i < guests.length; i++) {
                            var g = guests[i];
                            var opt = document.createElement('option');
                            opt.value = g.guestId;
                            opt.textContent = g.name + ' (' + g.contactNumber + ')';
                            if (String(g.guestId) === prev) opt.selected = true;
                            sel.appendChild(opt);
                        }
                    }

                    // ─── Reservations ─────────────────────────────────────────────────────
                    var guestMap = {};

                    function loadReservations() {
                        Promise.all([
                            fetch(CTX + '/api/reservations').then(function (r) { return r.json(); }),
                            fetch(CTX + '/api/guests').then(function (r) { return r.json(); })
                        ]).then(function (results) {
                            var reservations = results[0];
                            var guests = results[1];
                            guestMap = {};
                            for (var i = 0; i < guests.length; i++) { guestMap[guests[i].guestId] = guests[i]; }
                            renderReservationsTable(reservations);
                        }).catch(function (err) {
                            console.error('Failed to load reservations:', err);
                            document.getElementById('reservationsTableBody').innerHTML =
                                '<tr><td colspan="8" style="text-align:center;color:red;">Failed to load reservations.</td></tr>';
                        });
                    }

                    function renderReservationsTable(reservations) {
                        var tbody = document.getElementById('reservationsTableBody');
                        if (!reservations || reservations.length === 0) {
                            tbody.innerHTML = '<tr><td colspan="9" class="empty-message">No reservations found.</td></tr>';
                            return;
                        }
                        var html = '';
                        for (var i = 0; i < reservations.length; i++) {
                            var res = reservations[i];
                            var g = guestMap[res.guestId];
                            var gName = g ? g.name : 'N/A';
                            var gContact = g ? g.contactNumber : 'N/A';
                            var gEmail = g && g.email ? g.email : '';
                            html += '<tr>' +
                                '<td><strong>' + res.reservationNumber + '</strong></td>' +
                                '<td>' + gName + '</td>' +
                                '<td>' + gContact + '</td>' +
                                '<td>' + (gEmail ? '<a href="mailto:' + gEmail + '">' + gEmail + '</a>' : '<span style="color:#bbb;">—</span>') + '</td>' +
                                '<td>' + res.roomType + '</td>' +
                                '<td>' + res.checkInDate + '</td>' +
                                '<td>' + res.checkOutDate + '</td>' +
                                '<td><span style="background:#c3e6cb;color:#155724;padding:5px 10px;border-radius:3px;font-size:12px;">' + res.status + '</span></td>' +
                                '<td>' +
                                '<a href="' + CTX + '/download-bill?reservationId=' + res.reservationId + '" class="download-btn">&#128229; Bill</a>' +
                                '<button type="button" class="edit-btn" onclick="openEditModal(' + res.reservationId + ',' + res.guestId + ',\'' + escHtml(res.roomType) + '\',\'' + res.checkInDate + '\',\'' + res.checkOutDate + '\',' + res.roomId + ')">&#9999;&#65039; Edit</button>' +
                                '<form method="post" action="' + CTX + '/staff-dashboard" style="display:inline;">' +
                                '<input type="hidden" name="action" value="delete-reservation">' +
                                '<input type="hidden" name="reservationId" value="' + res.reservationId + '">' +
                                '<button type="submit" class="delete-btn" onclick="return confirm(\'Are you sure?\');">Delete</button>' +
                                '</form></td></tr>';
                        }
                        tbody.innerHTML = html;
                    }

                    // ─── Available Rooms AJAX ─────────────────────────────────────────────
                    function updateRoomList() {
                        var roomType = document.getElementById('roomType').value;
                        var checkIn = document.getElementById('checkInDate').value;
                        var checkOut = document.getElementById('checkOutDate').value;
                        var roomSel = document.getElementById('roomId');
                        var priceDisp = document.getElementById('priceDisplay');

                        if (!roomType || !checkIn || !checkOut) {
                            roomSel.innerHTML = '<option value="">-- Select a Room --</option>';
                            priceDisp.innerHTML = ''; priceDisp.style.display = 'none';
                            return;
                        }

                        fetch(CTX + '/api/rooms?action=available&roomType=' + encodeURIComponent(roomType) +
                            '&checkInDate=' + checkIn + '&checkOutDate=' + checkOut)
                            .then(function (r) { return r.json(); })
                            .then(function (rooms) {
                                if (rooms.length === 0) {
                                    roomSel.innerHTML = '<option value="">No rooms available for selected dates</option>';
                                    priceDisp.innerHTML = ''; priceDisp.style.display = 'none';
                                    return;
                                }
                                roomSel.innerHTML = '<option value="">-- Select a Room --</option>';
                                for (var i = 0; i < rooms.length; i++) {
                                    var opt = document.createElement('option');
                                    opt.value = rooms[i].roomId;
                                    opt.textContent = 'Room ' + rooms[i].roomNumber + ' - LKR ' + rooms[i].pricePerNight + '/night';
                                    opt.setAttribute('data-price', rooms[i].pricePerNight);
                                    roomSel.appendChild(opt);
                                }
                                priceDisp.innerHTML = '&#128176; Price: LKR ' + rooms[0].pricePerNight + '/night';
                                priceDisp.style.display = 'block';
                            })
                            .catch(function (err) {
                                console.error('Error fetching rooms:', err);
                                roomSel.innerHTML = '<option value="">Error loading rooms</option>';
                            });
                    }

                    function updatePrice() {
                        var sel = document.getElementById('roomId');
                        var pd = document.getElementById('priceDisplay');
                        if (sel.value) {
                            var p = sel.options[sel.selectedIndex].getAttribute('data-price');
                            pd.innerHTML = '&#128176; Price: LKR ' + p + '/night';
                            pd.style.display = 'block';
                        } else { pd.innerHTML = ''; pd.style.display = 'none'; }
                    }

                    // ─── Edit Reservation Modal ───────────────────────────────────────────
                    function openEditModal(resId, guestId, roomType, checkIn, checkOut, roomId) {
                        document.getElementById('editReservationId').value = resId;
                        document.getElementById('editGuestId').value = guestId;
                        document.getElementById('editRoomType').value = roomType;
                        document.getElementById('editCheckInDate').value = checkIn;
                        document.getElementById('editCheckOutDate').value = checkOut;
                        document.getElementById('editRoomId').innerHTML = '<option value="">-- Select a Room --</option>';
                        document.getElementById('editPriceDisplay').innerHTML = '';
                        document.getElementById('editPriceDisplay').style.display = 'none';
                        currentEditRoomId = roomId;
                        updateEditRoomList(resId);
                        document.getElementById('editModal').style.display = 'block';
                    }

                    function closeEditModal() { document.getElementById('editModal').style.display = 'none'; }

                    function updateEditRoomList(excludeId) {
                        if (excludeId && typeof excludeId !== 'number' && typeof excludeId !== 'string') {
                            excludeId = null;
                        }
                        if (!excludeId) excludeId = document.getElementById('editReservationId').value;

                        var roomType = document.getElementById('editRoomType').value;
                        var checkIn = document.getElementById('editCheckInDate').value;
                        var checkOut = document.getElementById('editCheckOutDate').value;
                        var roomSel = document.getElementById('editRoomId');
                        var priceDisp = document.getElementById('editPriceDisplay');

                        if (!roomType || !checkIn || !checkOut) {
                            roomSel.innerHTML = '<option value="">-- Select a Room --</option>';
                            priceDisp.innerHTML = ''; priceDisp.style.display = 'none';
                            return;
                        }

                        fetch(CTX + '/api/rooms?action=available&roomType=' + encodeURIComponent(roomType) +
                            '&checkInDate=' + checkIn + '&checkOutDate=' + checkOut +
                            (excludeId ? '&excludeReservationId=' + excludeId : ''))
                            .then(function (r) { return r.json(); })
                            .then(function (rooms) {
                                if (rooms.length === 0) {
                                    roomSel.innerHTML = '<option value="">No rooms available</option>';
                                    priceDisp.innerHTML = ''; priceDisp.style.display = 'none';
                                    return;
                                }
                                roomSel.innerHTML = '<option value="">-- Select a Room --</option>';
                                for (var i = 0; i < rooms.length; i++) {
                                    var opt = document.createElement('option');
                                    opt.value = rooms[i].roomId;
                                    opt.textContent = 'Room ' + rooms[i].roomNumber + ' - LKR ' + rooms[i].pricePerNight + '/night';
                                    opt.setAttribute('data-price', rooms[i].pricePerNight);
                                    if (currentEditRoomId && String(rooms[i].roomId) === String(currentEditRoomId)) {
                                        opt.selected = true;
                                    }
                                    roomSel.appendChild(opt);
                                }
                                updateEditPrice();
                            })
                            .catch(function (err) { console.error(err); roomSel.innerHTML = '<option value="">Error loading rooms</option>'; });
                    }

                    function updateEditPrice() {
                        var sel = document.getElementById('editRoomId');
                        var pd = document.getElementById('editPriceDisplay');
                        var ci = document.getElementById('editCheckInDate').value;
                        var co = document.getElementById('editCheckOutDate').value;
                        if (!sel.value) { pd.innerHTML = ''; pd.style.display = 'none'; return; }
                        var price = sel.options[sel.selectedIndex].getAttribute('data-price');
                        var nights = Math.ceil((new Date(co) - new Date(ci)) / 86400000);
                        if (nights > 0 && price) {
                            pd.innerHTML = '&#128176; LKR ' + price + '/night x ' + nights + ' nights = <strong>LKR ' + (nights * parseFloat(price)).toFixed(2) + '</strong>';
                            pd.style.display = 'block';
                        } else { pd.innerHTML = ''; pd.style.display = 'none'; }
                    }

                    // ─── Edit Guest Modal ─────────────────────────────────────────────────
                    function openEditGuestModal(id, name, contact, address, email) {
                        document.getElementById('editGuestIdField').value = id;
                        document.getElementById('editGuestNameField').value = name;
                        document.getElementById('editContactNumberField').value = contact;
                        document.getElementById('editAddressField').value = address;
                        document.getElementById('editEmailField').value = email || '';
                        document.getElementById('editGuestModal').style.display = 'block';
                    }

                    function closeEditGuestModal() { document.getElementById('editGuestModal').style.display = 'none'; }

                    window.onclick = function (e) {
                        if (e.target === document.getElementById('editModal')) closeEditModal();
                        if (e.target === document.getElementById('editGuestModal')) closeEditGuestModal();
                    };

                    // ─── Init ─────────────────────────────────────────────────────────────
                    document.addEventListener('DOMContentLoaded', function () {
                        loadGuests();
                        loadReservations();

                        var checkInInput = document.getElementById('checkInDate');
                        if (checkInInput) {
                            var today = new Date().toISOString().split('T')[0];
                            checkInInput.min = today;
                            checkInInput.value = today;

                            var tomorrow = new Date();
                            tomorrow.setDate(tomorrow.getDate() + 1);
                            var checkOutInput = document.getElementById('checkOutDate');
                            var tomorrowStr = tomorrow.toISOString().split('T')[0];
                            checkOutInput.value = tomorrowStr;
                            checkOutInput.min = tomorrowStr;

                            checkInInput.addEventListener('change', function () {
                                var next = new Date(this.value);
                                next.setDate(next.getDate() + 1);
                                checkOutInput.value = next.toISOString().split('T')[0];
                                checkOutInput.min = this.value;
                                updateRoomList();
                            });
                            checkOutInput.addEventListener('change', updateRoomList);
                            document.getElementById('roomType').addEventListener('change', updateRoomList);
                            document.getElementById('roomId').addEventListener('change', updatePrice);

                            setTimeout(updateRoomList, 50);
                        }

                        var editCheckIn = document.getElementById('editCheckInDate');
                        if (editCheckIn) {
                            var editCheckOut = document.getElementById('editCheckOutDate');
                            editCheckOut.min = new Date().toISOString().split('T')[0];
                            editCheckIn.addEventListener('change', function () {
                                var next = new Date(this.value);
                                next.setDate(next.getDate() + 1);
                                editCheckOut.value = next.toISOString().split('T')[0];
                                editCheckOut.min = this.value;
                                updateEditRoomList();
                            });
                            document.getElementById('editCheckOutDate').addEventListener('change', updateEditRoomList);
                            document.getElementById('editRoomType').addEventListener('change', updateEditRoomList);
                            document.getElementById('editRoomId').addEventListener('change', updateEditPrice);
                        }
                    });
                </script>
            </body>

            </html>
            <%--@formatter:on--%>