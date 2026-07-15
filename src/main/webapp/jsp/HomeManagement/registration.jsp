<%--
  Created by IntelliJ IDEA.
  User: giuliapanarello
  Date: 10/10/24
  Time: 10:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String message = (String) request.getAttribute("applicationMessage");
    String email = (String) request.getAttribute("email");
    String nome = (String) request.getAttribute("nome");
    String cognome = (String) request.getAttribute("cognome");
    String telefono = (String) request.getAttribute("telefono");
%>
<html>
<head>
    <title>Registrazione</title>
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

        .registration-container {
            background-color: #E6E6FA;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 500px;
            height: auto;
            text-align: center;
            border: 3px solid #6A5ACD;
            margin-top: 9%;
        }

        h2 {
            font-size: 44px;
            margin-bottom: 20px;
        }

        .registration-container input[type="text"],
        .registration-container input[type="email"],
        .registration-container input[type="number"],
        .registration-container input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border-radius: 5px;
            border: 1.5px solid #6A5ACD;
        }

        .registration-container button {
            width: 100%;
            padding: 10px;
            background-color: #6A5ACD;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 20px;
        }

        .registration-container button:hover {
            background-color: #FFCBFF;
            color: black;
            font-size: 20px;
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

<div class="registration-container">
    <h2>
        <img src="<%= request.getContextPath() %>/images/download.png" alt="Icona Registrazione" style="width: 40px; height: 40px; vertical-align: middle; border-radius: 50%;">Registrazione
    </h2>
    <form action="Dispatcher" method="post">
        <input type="text" name="Nome" placeholder="Inserisci nome" value="<% if(nome != null){ %><%=nome%><% } %>" required />
        <input type="text" name="Cognome" placeholder="Inserisci Cognome" value="<% if(cognome != null){ %><%=cognome%><% } %>" required />
        <input type="email" name="Email" placeholder="Inserisci email" value="<% if(email != null){ %><%=email%><% } %>" required />
        <input type="number" minlength="10" maxlength="10" name="Telefono" placeholder="Inserisci telefono" value="<% if(telefono != null){ %><%=telefono%><% } %>" required />
        <input type="password" name="Password" placeholder="Inserisci password" required />
        <input type="hidden" name="controllerAction" value="HomeManagement.registration" />
        <button type="submit">Conferma</button>
    </form>
    <% if(message != null){ %>
    <div class="message">
        <%= message %>
    </div>
    <% } %>
</div>
</body>
</html>
