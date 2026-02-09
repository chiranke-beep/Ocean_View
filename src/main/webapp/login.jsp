<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login - Ocean View Resort</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
            background: linear-gradient(135deg, #f5ede0 0%, #e8dcc8 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .container {
            width: 100%;
            max-width: 420px;
            padding: 20px;
        }
        
        .card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(139, 100, 70, 0.15);
            padding: 50px;
            animation: slideUp 0.6s ease-out;
            border: 1px solid rgba(210, 180, 140, 0.2);
        }
        
        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(40px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .header {
            text-align: center;
            margin-bottom: 35px;
        }
        
        .logo {
            width: 60px;
            height: 60px;
            background: linear-gradient(135deg, #d4a574 0%, #c49055 100%);
            border-radius: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: 600;
            font-size: 28px;
            margin: 0 auto 20px;
        }
        
        h1 {
            font-size: 32px;
            color: #2c2c2c;
            margin-bottom: 8px;
            font-weight: 600;
            letter-spacing: -0.5px;
        }
        
        .subtitle {
            color: #8b8b8b;
            font-size: 15px;
            font-weight: 400;
        }
        
        .form-group {
            margin-bottom: 22px;
        }
        
        label {
            display: block;
            font-size: 13px;
            font-weight: 500;
            color: #3a3a3a;
            margin-bottom: 10px;
            text-transform: uppercase;
            letter-spacing: 0.8px;
        }
        
        input, select {
            width: 100%;
            padding: 14px 16px;
            border: 1.5px solid #e5ddd0;
            border-radius: 12px;
            font-size: 15px;
            transition: all 0.3s ease;
            font-family: 'Inter', sans-serif;
            background: #faf8f5;
        }
        
        input:focus, select:focus {
            outline: none;
            border-color: #d4a574;
            background: white;
            box-shadow: 0 0 0 4px rgba(212, 165, 116, 0.1);
        }
        
        input::placeholder {
            color: #b0a89f;
        }
        
        .error-message {
            background: #fdeef0;
            border: 1.5px solid #e8b4ba;
            color: #c53030;
            padding: 14px;
            border-radius: 12px;
            margin-bottom: 24px;
            font-size: 14px;
            font-weight: 500;
            animation: shake 0.5s ease-in-out;
        }
        
        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-5px); }
            75% { transform: translateX(5px); }
        }
        
        .sign-in-btn {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #d4a574 0%, #c49055 100%);
            color: white;
            border: none;
            border-radius: 12px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            margin-top: 28px;
            transition: transform 0.2s, box-shadow 0.2s;
            letter-spacing: 0.5px;
            font-family: 'Inter', sans-serif;
        }
        
        .sign-in-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 35px rgba(212, 165, 116, 0.3);
        }
        
        .sign-in-btn:active {
            transform: translateY(-1px);
        }
    </style>
</head>
<body>
<div class="container">
    <div class="card">
        <div class="header">
            <div class="logo">🏖️</div>
            <h1>Ocean View</h1>
            <p class="subtitle">Welcome to our luxury resort</p>
        </div>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null && !error.isEmpty()) {
        %>
            <div class="error-message"><%= error %></div>
        <%
            }
        %>

        <form method="post" action="<%= request.getContextPath() %>/login" onsubmit="return validateForm();">
            <div class="form-group">
                <label for="username">Username</label>
                <input 
                    id="username" 
                    name="username" 
                    type="text" 
                    placeholder="Enter your username" 
                    autocomplete="off"
                />
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input 
                    id="password" 
                    name="password" 
                    type="password" 
                    placeholder="Enter your password" 
                    autocomplete="off"
                />
            </div>

            <div class="form-group">
                <label for="role">Login As</label>
                <select id="role" name="role">
                    <option value="Admin">Administrator</option>
                    <option value="Staff">Staff</option>
                </select>
            </div>

            <button class="sign-in-btn" type="submit">Sign In</button>
        </form>
    </div>
</div>

<script>
    function validateForm() {
        var username = document.getElementById('username').value.trim();
        var password = document.getElementById('password').value.trim();
        
        if (username === '' || password === '') {
            alert('Please enter both username and password');
            return false;
        }
        
        if (username.length < 3) {
            alert('Username must be at least 3 characters');
            return false;
        }
        
        if (password.length < 5) {
            alert('Password must be at least 5 characters');
            return false;
        }
        
        return true;
    }
</script>
</body>
</html>
