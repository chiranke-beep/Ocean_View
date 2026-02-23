<%--@formatter:off--%>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ page import="java.util.List" %>
            <%@ page import="com.example.ocean_view_resort.model.Staff" %>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <title>Admin Dashboard - Ocean View Resort</title>
                    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap"
                        rel="stylesheet">
                    <style>
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }

                        body {
                            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
                            background: #f8f6f3;
                            color: #2c2c2c;
                        }

                        .navbar {
                            background: linear-gradient(135deg, #d4a574 0%, #c49055 100%);
                            color: white;
                            padding: 15px 30px;
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            box-shadow: 0 4px 12px rgba(139, 100, 70, 0.15);
                        }

                        .navbar h2 {
                            font-size: 24px;
                            font-weight: 600;
                        }

                        .navbar .user-info {
                            display: flex;
                            gap: 15px;
                            align-items: center;
                        }

                        .navbar .user-info span {
                            font-size: 14px;
                        }

                        .logout-btn {
                            background: rgba(255, 255, 255, 0.2);
                            color: white;
                            border: 1px solid rgba(255, 255, 255, 0.3);
                            padding: 8px 16px;
                            border-radius: 8px;
                            cursor: pointer;
                            font-size: 13px;
                            transition: all 0.3s;
                        }

                        .logout-btn:hover {
                            background: rgba(255, 255, 255, 0.3);
                        }

                        .container {
                            max-width: 1400px;
                            margin: 0 auto;
                            padding: 30px 20px;
                        }

                        .message {
                            background: #e8f5e9;
                            border: 1px solid #4caf50;
                            color: #2e7d32;
                            padding: 15px;
                            border-radius: 10px;
                            margin-bottom: 20px;
                        }

                        .tabs {
                            display: flex;
                            gap: 10px;
                            margin-bottom: 30px;
                            border-bottom: 2px solid #e5ddd0;
                        }

                        .tab-btn {
                            padding: 12px 20px;
                            background: none;
                            border: none;
                            border-bottom: 3px solid transparent;
                            cursor: pointer;
                            font-size: 15px;
                            font-weight: 500;
                            color: #8b8b8b;
                            transition: all 0.3s;
                        }

                        .tab-btn.active {
                            color: #d4a574;
                            border-bottom-color: #d4a574;
                        }

                        .tab-content {
                            display: none;
                        }

                        .tab-content.active {
                            display: block;
                        }

                        .card {
                            background: white;
                            border-radius: 16px;
                            padding: 30px;
                            box-shadow: 0 4px 12px rgba(139, 100, 70, 0.08);
                            margin-bottom: 30px;
                        }

                        .card h3 {
                            font-size: 22px;
                            margin-bottom: 20px;
                        }

                        .form-grid {
                            display: grid;
                            grid-template-columns: 1fr 1fr;
                            gap: 15px;
                            margin-bottom: 20px;
                        }

                        .form-grid.full {
                            grid-template-columns: 1fr;
                        }

                        .form-group {
                            display: flex;
                            flex-direction: column;
                        }

                        label {
                            font-size: 13px;
                            font-weight: 500;
                            color: #3a3a3a;
                            margin-bottom: 8px;
                            text-transform: uppercase;
                            letter-spacing: 0.8px;
                        }

                        input,
                        select,
                        textarea {
                            padding: 12px;
                            border: 1.5px solid #e5ddd0;
                            border-radius: 10px;
                            font-size: 14px;
                            font-family: 'Inter', sans-serif;
                            background: #faf8f5;
                            transition: all 0.3s;
                        }

                        input:focus,
                        select:focus,
                        textarea:focus {
                            outline: none;
                            border-color: #d4a574;
                            background: white;
                            box-shadow: 0 0 0 4px rgba(212, 165, 116, 0.1);
                        }

                        .btn {
                            padding: 12px 24px;
                            background: linear-gradient(135deg, #d4a574 0%, #c49055 100%);
                            color: white;
                            border: none;
                            border-radius: 10px;
                            cursor: pointer;
                            font-size: 14px;
                            font-weight: 600;
                            transition: all 0.3s;
                        }

                        .btn:hover {
                            transform: translateY(-2px);
                            box-shadow: 0 8px 20px rgba(212, 165, 116, 0.3);
                        }

                        .btn-danger {
                            background: linear-gradient(135deg, #ef5350 0%, #e53935 100%);
                        }

                        table {
                            width: 100%;
                            border-collapse: collapse;
                        }

                        th {
                            background: #f5ede0;
                            padding: 15px;
                            text-align: left;
                            font-weight: 600;
                            color: #3a3a3a;
                            border-bottom: 2px solid #e5ddd0;
                        }

                        td {
                            padding: 15px;
                            border-bottom: 1px solid #e5ddd0;
                        }

                        tr:hover {
                            background: #faf8f5;
                        }

                        .action-btns {
                            display: flex;
                            gap: 8px;
                        }

                        .btn-small {
                            padding: 8px 12px;
                            font-size: 12px;
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
                            background-color: rgba(0, 0, 0, 0.5);
                        }

                        .modal-content {
                            background-color: white;
                            margin: 10% auto;
                            padding: 30px;
                            border-radius: 16px;
                            width: 90%;
                            max-width: 500px;
                            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
                            animation: slideDown 0.3s ease-out;
                        }

                        @keyframes slideDown {
                            from {
                                opacity: 0;
                                transform: translateY(-50px);
                            }

                            to {
                                opacity: 1;
                                transform: translateY(0);
                            }
                        }

                        .modal-header {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            margin-bottom: 20px;
                            border-bottom: 2px solid #e5ddd0;
                            padding-bottom: 15px;
                        }

                        .modal-header h2 {
                            font-size: 20px;
                            color: #2c2c2c;
                        }

                        .modal-close {
                            background: none;
                            border: none;
                            font-size: 28px;
                            cursor: pointer;
                            color: #8b8b8b;
                            transition: color 0.3s;
                        }

                        .modal-close:hover {
                            color: #d4a574;
                        }

                        .modal-body {
                            margin: 20px 0;
                        }

                        .modal-footer {
                            display: flex;
                            gap: 10px;
                            justify-content: flex-end;
                            margin-top: 25px;
                            padding-top: 15px;
                            border-top: 1px solid #e5ddd0;
                        }

                        .cancel-btn {
                            padding: 10px 20px;
                            background: #e5ddd0;
                            color: #2c2c2c;
                            border: none;
                            border-radius: 8px;
                            cursor: pointer;
                            font-weight: 500;
                            transition: all 0.3s;
                        }

                        .cancel-btn:hover {
                            background: #d9cfc0;
                        }

                        .submit-btn {
                            padding: 10px 24px;
                            background: linear-gradient(135deg, #d4a574 0%, #c49055 100%);
                            color: white;
                            border: none;
                            border-radius: 8px;
                            cursor: pointer;
                            font-weight: 500;
                            transition: all 0.3s;
                        }

                        .submit-btn:hover {
                            transform: translateY(-2px);
                            box-shadow: 0 6px 16px rgba(212, 165, 116, 0.3);
                        }

                        .edit-btn {
                            padding: 8px 12px;
                            background: #d4a574;
                            color: white;
                            border: none;
                            border-radius: 6px;
                            cursor: pointer;
                            font-size: 12px;
                            transition: all 0.3s;
                        }

                        .edit-btn:hover {
                            background: #c49055;
                        }
                    </style>
                </head>

                <body>
                    <div class="navbar">
                        <h2>🏖️ Ocean View Admin</h2>
                        <div class="user-info">
                            <span>Welcome, <strong>
                                    <%= session.getAttribute("username") %>
                                </strong></span>
                            <a href="<%= request.getContextPath() %>/logout" class="logout-btn"
                                style="text-decoration: none;">Logout</a>
                        </div>
                    </div>

                    <div class="container">
                        <% String message=(String) request.getAttribute("message"); if (message !=null &&
                            !message.isEmpty()) { %>
                            <div class="message">
                                <%= message %>
                            </div>
                            <% } %>

                                <div class="tabs">
                                    <button class="tab-btn active" onclick="switchTab('home')">📊 Dashboard</button>
                                    <button class="tab-btn" onclick="switchTab('staff')">👥 Staff Management</button>
                                    <button class="tab-btn" onclick="switchTab('rooms')">🏠 Room Management</button>
                                </div>

                                <!-- Dashboard Tab -->
                                <div id="home" class="tab-content active">
                                    <div class="card">
                                        <h3>Dashboard Overview</h3>
                                        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                                            <div
                                                style="padding: 20px; background: #f5ede0; border-radius: 12px; text-align: center;">
                                                <div style="font-size: 32px; font-weight: bold; color: #d4a574;">
                                                    <%= request.getAttribute("staffList") !=null ? ((List<?>)
                                                        request.getAttribute("staffList")).size() : 0 %>
                                                </div>
                                                <div style="color: #8b8b8b; margin-top: 5px;">Total Staff</div>
                                            </div>
                                            <div
                                                style="padding: 20px; background: #f5ede0; border-radius: 12px; text-align: center;">
                                                <div style="font-size: 32px; font-weight: bold; color: #d4a574;">
                                                    <span id="roomCountDisplay">...</span>
                                                </div>
                                                <div style="color: #8b8b8b; margin-top: 5px;">Total Rooms</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Staff Management Tab -->
                                <div id="staff" class="tab-content">
                                    <div class="card">
                                        <h3>Add New Staff</h3>
                                        <form method="post" action="<%= request.getContextPath() %>/admin-dashboard">
                                            <input type="hidden" name="action" value="add-staff">
                                            <div class="form-grid">
                                                <div class="form-group">
                                                    <label>Full Name</label>
                                                    <input type="text" name="name" placeholder="Enter full name"
                                                        required minlength="3" maxlength="100">
                                                </div>
                                                <div class="form-group">
                                                    <label>Username</label>
                                                    <input type="text" name="username" placeholder="Enter username"
                                                        required minlength="4" maxlength="50">
                                                </div>
                                                <div class="form-group">
                                                    <label>Password</label>
                                                    <input type="password" name="password" placeholder="Enter password"
                                                        required minlength="6" maxlength="100">
                                                </div>
                                                <div class="form-group">
                                                    <label>Contact Number</label>
                                                    <input type="tel" name="contactNumber" placeholder="+1234567890">
                                                </div>
                                            </div>
                                            <input type="hidden" name="role" value="Staff">
                                            <button class="btn" type="submit">Add Staff</button>
                                        </form>
                                    </div>

                                    <div class="card">
                                        <h3>Staff List</h3>
                                        <% List<Staff> staffList = (List<Staff>) request.getAttribute("staffList");
                                                if (staffList != null && !staffList.isEmpty()) {
                                                %>
                                                <table>
                                                    <thead>
                                                        <tr>
                                                            <th>ID</th>
                                                            <th>Name</th>
                                                            <th>Username</th>
                                                            <th>Contact</th>
                                                            <th>Role</th>
                                                            <th>Actions</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <% for (Staff staff : staffList) { %>
                                                            <tr>
                                                                <td>
                                                                    <%= staff.getStaffId() %>
                                                                </td>
                                                                <td>
                                                                    <%= staff.getName() %>
                                                                </td>
                                                                <td>
                                                                    <%= staff.getUsername() %>
                                                                </td>
                                                                <td>
                                                                    <%= staff.getContactNumber() %>
                                                                </td>
                                                                <td>
                                                                    <%= staff.getRole() %>
                                                                </td>
                                                                <td>
                                                                    <div class="action-btns">
                                                                        <button type="button" class="edit-btn"
                                                                            onclick="openEditStaffModal(<%= staff.getStaffId() %>, '<%= staff.getName() %>', '<%= staff.getUsername() %>', '<%= staff.getContactNumber() %>')"
                                                                            title="Edit Staff">✏️ Edit</button>
                                                                        <form method="post"
                                                                            action="<%= request.getContextPath() %>/admin-dashboard"
                                                                            style="display:inline;">
                                                                            <input type="hidden" name="action"
                                                                                value="delete-staff">
                                                                            <input type="hidden" name="staffId"
                                                                                value="<%= staff.getStaffId() %>">
                                                                            <button class="btn btn-small btn-danger"
                                                                                type="submit"
                                                                                onclick="return confirm('Delete staff?')">Delete</button>
                                                                        </form>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <% } %>
                                                    </tbody>
                                                </table>
                                                <% } else { %>
                                                    <p style="color: #8b8b8b;">No staff members found.</p>
                                                    <% } %>
                                    </div>
                                </div>

                                <!-- Room Management Tab -->
                                <div id="rooms" class="tab-content">
                                    <div class="card">
                                        <h3>Add New Room</h3>
                                        <form method="post" action="<%= request.getContextPath() %>/admin-dashboard">
                                            <input type="hidden" name="action" value="add-room">
                                            <div class="form-grid">
                                                <div class="form-group">
                                                    <label>Room Number</label>
                                                    <input type="text" name="roomNumber" placeholder="101" required>
                                                </div>
                                                <div class="form-group">
                                                    <label>Room Type</label>
                                                    <select name="roomType" required>
                                                        <option value="">-- Select Room Type --</option>
                                                        <option value="Single">Single</option>
                                                        <option value="Double">Double</option>
                                                        <option value="Deluxe">Deluxe</option>
                                                        <option value="Suite">Suite</option>
                                                        <option value="Presidential">Presidential</option>
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label>Price Per Night (LKR)</label>
                                                    <input type="number" name="pricePerNight" placeholder="150"
                                                        step="0.01" min="0.01" required>
                                                </div>
                                                <div class="form-group">
                                                    <label>Capacity (Guests)</label>
                                                    <input type="number" name="capacity" placeholder="2" min="1"
                                                        required>
                                                </div>
                                            </div>
                                            <button class="btn" type="submit">Add Room</button>
                                        </form>
                                    </div>

                                    <div class="card">
                                        <h3>Room List</h3>
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Room Number</th>
                                                    <th>Type</th>
                                                    <th>Price/Night</th>
                                                    <th>Capacity</th>
                                                    <th>Status</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody id="roomsTableBody">
                                                <tr>
                                                    <td colspan="7" style="text-align:center;color:#8b8b8b;">Loading
                                                        rooms...</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                    </div>

                    <script>
                        var CTX = '<%= request.getContextPath() %>';

                        // ─── Fetch all rooms from the API and populate the table ─────────────
                        function loadRooms() {
                            fetch(CTX + '/api/rooms')
                                .then(function (r) { return r.json(); })
                                .then(function (rooms) {
                                    var countEl = document.getElementById('roomCountDisplay');
                                    if (countEl) countEl.textContent = rooms.length;

                                    var tbody = document.getElementById('roomsTableBody');
                                    if (!rooms || rooms.length === 0) {
                                        tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;color:#8b8b8b;">No rooms found.</td></tr>';
                                        return;
                                    }
                                    var html = '';
                                    for (var i = 0; i < rooms.length; i++) {
                                        var r = rooms[i];
                                        html += '<tr>' +
                                            '<td>' + r.roomId + '</td>' +
                                            '<td>' + r.roomNumber + '</td>' +
                                            '<td>' + r.roomType + '</td>' +
                                            '<td>LKR ' + r.pricePerNight + '</td>' +
                                            '<td>' + r.capacity + '</td>' +
                                            '<td><span style="background:#e8f5e9;padding:4px 8px;border-radius:4px;font-size:12px;">' + r.status + '</span></td>' +
                                            '<td>' +
                                            '<form method="post" action="' + CTX + '/admin-dashboard" style="display:inline;">' +
                                            '<input type="hidden" name="action" value="delete-room">' +
                                            '<input type="hidden" name="roomId" value="' + r.roomId + '">' +
                                            '<button class="btn btn-small btn-danger" type="submit" onclick="return confirm(\"Delete room?\");">Delete</button>' +
                                            '</form></td></tr>';
                                    }
                                    tbody.innerHTML = html;
                                })
                                .catch(function (err) {
                                    console.error('Failed to load rooms:', err);
                                    document.getElementById('roomsTableBody').innerHTML =
                                        '<tr><td colspan="7" style="text-align:center;color:red;">Failed to load rooms.</td></tr>';
                                });
                        }

                        function switchTab(tabName) {
                            document.querySelectorAll('.tab-content').forEach(function (tab) { tab.classList.remove('active'); });
                            document.querySelectorAll('.tab-btn').forEach(function (btn) { btn.classList.remove('active'); });
                            document.getElementById(tabName).classList.add('active');
                            event.target.classList.add('active');
                        }

                        function openEditStaffModal(staffId, name, username, contactNumber) {
                            document.getElementById('editStaffIdField').value = staffId;
                            document.getElementById('editStaffNameField').value = name;
                            document.getElementById('editStaffUsernameField').value = username;
                            document.getElementById('editStaffContactField').value = contactNumber;
                            document.getElementById('editStaffModal').style.display = 'block';
                        }

                        function closeEditStaffModal() {
                            document.getElementById('editStaffModal').style.display = 'none';
                        }

                        window.onclick = function (event) {
                            var modal = document.getElementById('editStaffModal');
                            if (event.target === modal) modal.style.display = 'none';
                        };

                        document.addEventListener('DOMContentLoaded', loadRooms);
                    </script>

                    <%--@formatter:on--%>

                        <!-- Edit Staff Modal -->
                        <div id="editStaffModal" class="modal">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h2>Edit Staff</h2>
                                    <button class="modal-close" onclick="closeEditStaffModal()">&times;</button>
                                </div>
                                <div class="modal-body">
                                    <form method="post" action="<%= request.getContextPath() %>/admin-dashboard"
                                        id="editStaffForm">
                                        <input type="hidden" name="action" value="edit-staff">
                                        <input type="hidden" id="editStaffIdField" name="staffId">
                                        <input type="hidden" name="role" value="Staff">

                                        <div class="form-group">
                                            <label for="editStaffNameField">Full Name:</label>
                                            <input type="text" id="editStaffNameField" name="name" required
                                                minlength="3">
                                        </div>

                                        <div class="form-group">
                                            <label for="editStaffUsernameField">Username:</label>
                                            <input type="text" id="editStaffUsernameField" name="username" required
                                                minlength="4" readonly>
                                        </div>

                                        <div class="form-group">
                                            <label for="editStaffContactField">Contact Number:</label>
                                            <input type="tel" id="editStaffContactField" name="contactNumber">
                                        </div>
                                    </form>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="cancel-btn"
                                        onclick="closeEditStaffModal()">Cancel</button>
                                    <button type="submit" form="editStaffForm" class="submit-btn">Save Changes</button>
                                </div>
                            </div>
                        </div>

                </body>

                </html>