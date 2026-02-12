<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.ocean_view_resort.model.Staff" %>
<%@ page import="com.example.ocean_view_resort.model.Room" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Admin Dashboard - Ocean View Resort</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
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
        
        input, select, textarea {
            padding: 12px;
            border: 1.5px solid #e5ddd0;
            border-radius: 10px;
            font-size: 14px;
            font-family: 'Inter', sans-serif;
            background: #faf8f5;
            transition: all 0.3s;
        }
        
        input:focus, select:focus, textarea:focus {
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
    </style>
</head>
<body>
    <div class="navbar">
    <h2>🏖️ Ocean View Admin</h2>
    <div class="user-info">
        <span>Welcome, <strong><%= session.getAttribute("username") %></strong></span>
        <a href="<%= request.getContextPath() %>/logout" class="logout-btn" style="text-decoration: none;">Logout</a>
    </div>
</div>

<div class="container">
    <%
        String message = (String) request.getAttribute("message");
        if (message != null && !message.isEmpty()) {
    %>
        <div class="message"><%= message %></div>
    <%
        }
    %>

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
                <div style="padding: 20px; background: #f5ede0; border-radius: 12px; text-align: center;">
                    <div style="font-size: 32px; font-weight: bold; color: #d4a574;">
                        <%= request.getAttribute("staffList") != null ? ((List<?>) request.getAttribute("staffList")).size() : 0 %>
                    </div>
                    <div style="color: #8b8b8b; margin-top: 5px;">Total Staff</div>
                </div>
                <div style="padding: 20px; background: #f5ede0; border-radius: 12px; text-align: center;">
                    <div style="font-size: 32px; font-weight: bold; color: #d4a574;">
                        <%= request.getAttribute("roomList") != null ? ((List<?>) request.getAttribute("roomList")).size() : 0 %>
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
                        <input type="text" name="name" placeholder="John Doe" required>
                    </div>
                    <div class="form-group">
                        <label>Username</label>
                        <input type="text" name="username" placeholder="john_doe" required>
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="password" placeholder="Enter password" required>
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
            <%
                List<Staff> staffList = (List<Staff>) request.getAttribute("staffList");
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
                                <td><%= staff.getStaffId() %></td>
                                <td><%= staff.getName() %></td>
                                <td><%= staff.getUsername() %></td>
                                <td><%= staff.getContactNumber() %></td>
                                <td><%= staff.getRole() %></td>
                                <td>
                                    <div class="action-btns">
                                        <form method="post" action="<%= request.getContextPath() %>/admin-dashboard" style="display:inline;">
                                            <input type="hidden" name="action" value="delete-staff">
                                            <input type="hidden" name="staffId" value="<%= staff.getStaffId() %>">
                                            <button class="btn btn-small btn-danger" type="submit" onclick="return confirm('Delete staff?')">Delete</button>
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
                        <label>Price Per Night ($)</label>
                        <input type="number" name="pricePerNight" placeholder="150" step="0.01" min="0.01" required>
                    </div>
                    <div class="form-group">
                        <label>Capacity (Guests)</label>
                        <input type="number" name="capacity" placeholder="2" min="1" required>
                    </div>
                </div>
                <button class="btn" type="submit">Add Room</button>
            </form>
        </div>

        <div class="card">
            <h3>Room List</h3>
            <%
                List<Room> roomList = (List<Room>) request.getAttribute("roomList");
                if (roomList != null && !roomList.isEmpty()) {
            %>
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
                    <tbody>
                        <% for (Room room : roomList) { %>
                            <tr>
                                <td><%= room.getRoomId() %></td>
                                <td><%= room.getRoomNumber() %></td>
                                <td><%= room.getRoomType() %></td>
                                <td>$<%= room.getPricePerNight() %></td>
                                <td><%= room.getCapacity() %></td>
                                <td><span style="background: #e8f5e9; padding: 4px 8px; border-radius: 4px; font-size: 12px;"><%= room.getStatus() %></span></td>
                                <td>
                                    <div class="action-btns">
                                        <form method="post" action="<%= request.getContextPath() %>/admin-dashboard" style="display:inline;">
                                            <input type="hidden" name="action" value="delete-room">
                                            <input type="hidden" name="roomId" value="<%= room.getRoomId() %>">
                                            <button class="btn btn-small btn-danger" type="submit" onclick="return confirm('Delete room?')">Delete</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p style="color: #8b8b8b;">No rooms found.</p>
            <% } %>
        </div>
    </div>
</div>

<script>
    function switchTab(tabName) {
        // Hide all tabs
        var tabs = document.querySelectorAll('.tab-content');
        tabs.forEach(function(tab) {
            tab.classList.remove('active');
        });
        
        // Remove active from all buttons
        var buttons = document.querySelectorAll('.tab-btn');
        buttons.forEach(function(btn) {
            btn.classList.remove('active');
        });
        
        // Show selected tab
        document.getElementById(tabName).classList.add('active');
        
        // Add active to clicked button
        event.target.classList.add('active');
    }
</script>
</body>
</html>
