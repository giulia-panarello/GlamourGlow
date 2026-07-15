<%--
  Created by IntelliJ IDEA.
  User: giuliapanarello
  Date: 07/10/24
  Time: 11:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String message = (String) request.getAttribute("applicationMessage");
    String email = (String) request.getAttribute("email");
%>
<html>
<head>
    <title>Login</title>
    <link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/images/download.png">
    <style>

        .home-icon {
            margin-right: 80px;
        }

        .home-icon img {
            width: 40px;
            height: 40px;
        }

        .header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin: 20px 0;
            padding: 20px;
            background-color: #E6E6FA;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            width: 100%;
            position: absolute;
            top: 0;
        }

        .logo-container {
            display: flex;
            align-items: center;
            margin-left: 20px;
        }

        .logo-container img {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            margin-right: 10px;
        }

        .logo-container h1 {
            font-size: 36px;
            color: #6A5ACD;
            margin: 0;
            font-style: italic;
        }

        body {
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
            background-color: pink;
            height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
        }

        .login-container {
            background-color: #E6E6FA;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 500px;
            text-align: center;
            border: 3px solid #6A5ACD;
        }

        h2 {
            font-size: 44px;
            margin-bottom: 20px;
        }

        .login-container input[type="email"],
        .login-container input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border-radius: 5px;
            border: 1.5px solid #6A5ACD;
        }
        .login-container button {
            width: 100%;
            padding: 10px;
            background-color: #6A5ACD;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 20px;
        }
        .login-container button:hover {
            background-color: #4b0082;
            color: white;
        }
        .message {
            color: red;
            margin-top: 10px;
        }
    </style>
</head>
<body>

<!-- Header con logo e icona Home -->
<div class="header">
    <div class="logo-container">
        <img src="<%= request.getContextPath() %>/images/logo.png" alt="Logo">
        <h1>GLAMOURGLOW</h1>
    </div>
    <div class="home-icon">
        <a href="Dispatcher?controllerAction=HomeManagement.viewhome">
            <img src="<%= request.getContextPath() %>/images/cas.png" alt="Home">
        </a>
    </div>
</div>

<!-- Contenuto del login -->
<div class="login-container">
    <h2>
        <img src="<%= request.getContextPath() %>/images/download.png" alt="Icona Login" style="width: 40px; height: 40px; vertical-align: middle; border-radius: 50%;"> Login
    </h2>
    <form action="Dispatcher" method="post">
        <input type="email" name="Email" placeholder="Inserisci email" value="<% if(email != null){ %><%=email%><% } %>" required/>
        <input type="password" name="Password" placeholder="Inserisci password" required/>
        <input type="hidden" name="controllerAction" value="HomeManagement.login">
        <button type="submit">Login</button>
    </form>
    Non hai un account? <a href="Dispatcher?controllerAction=HomeManagement.viewregistration"> Registrati </a>
    <% if(message != null){ %>
    <div class="message">
        <%= message %>
    </div>
    <% } %>
</div>

</body>
</html>
