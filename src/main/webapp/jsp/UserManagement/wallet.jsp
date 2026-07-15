
<%--
  Pagina di ricarica del portafoglio con header fisso
  Created by IntelliJ IDEA.
  User: giuliapanarello
  Date: 23/10/24
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String messaggio = (String) request.getAttribute("applicationMessage");
%>
<html>
<head>
    <title>Ricarica Portafoglio - GlamourGlow</title>
    <link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/images/wallet.png">
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


        .container {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 2000);
            padding: 40px;
            width: 400px;
            position: relative; /* Imposta il contenitore come riferimento per il bottone assoluto */
            text-align: center;
            margin-top: 120px; /* Spazio per l'header fisso */
            height: 400px; /* Altezza fissa per il contenitore */
            border: 3px solid #6A5ACD;
        }

        .container h1 {
            color: #6A5ACD;
            font-size: 24px;
            margin-bottom: 10px;
        }


        .container h1::after {
            content: '';
            display: block;
            width: 100%;
            height: 2px;
            background-color: #6A5ACD;
            margin-top: 10px;
        }

        .input-group {
            display: flex;
            flex-direction: column;
            margin-bottom: 20px;
        }

        .input-group input::placeholder{
            font-size: 16px;
        }

        .input-group input[type="number"] {
            padding: 20px;
            border-radius: 5px;
            border: 1.5px solid #6A5ACD;
            font-size: 20px;
            position: absolute;
            bottom: 41%;
            text-align: center;
            left: 50%;
            transform: translateX(-50%);
            width: 57%;
        }

        button {
            background-color: #6A5ACD;
            color: white;
            border: none;
            padding: 10px 20px;
            font-size: 18px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
            position: absolute;
            bottom: 99px;
            left: 50%;
            transform: translateX(-50%);
        }

        button:hover {
            background-color: #836FFF;
        }



        .input-group img {
            width: 70px;
            position: absolute;
            height: 70px;
            transform: translateX(-50%);
            left: 50%;
            top: 27%;
        }

        .message-container {
            width: 34%;
            max-width: 900px;
            padding: 10px;
            background-color: #F0E68C;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            text-align: center;
            transform: translateX(-50%);
            left: 50%;
            position: absolute;
            bottom: 17px;
        }

        .message {
            font-size: 18px;
            color: #483D8B;
            font-weight: bold;
            text-align: center;
        }
        .message-content {
            display: inline-block;
        }

    </style>
</head>
<body>

<!-- Header fisso con logo e icona Home -->
<div class="header">
    <div class="logo-container">
        <img src="<%= request.getContextPath() %>/images/logo.png" alt="Logo">
        <h1>GLAMOURGLOW</h1> <!-- Questo titolo non avrà la linea sotto -->
    </div>
    <div class="home-icon">
        <a href="Dispatcher?controllerAction=HomeManagement.viewhome">
            <img src="<%= request.getContextPath() %>/images/cas.png" alt="Home">
        </a>
    </div>
</div>

<div class="container">
    <h1>Ricarica il tuo portafoglio</h1> <!-- Questo titolo avrà la linea sotto -->

    <form action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="UserManagement.ricarica"/>

        <div class="input-group">
            <img src="<%= request.getContextPath() %>/images/portaf.png" alt="Euro">
                <input type="number" name="saldo" id="saldo" min="0" step="0.01" required placeholder="Inserisci importo da ricaricare" />

        </div>

        <!-- Bottone fissato in basso -->
        <button type="submit">Ricarica</button>
    </form>

    <div>
        <% if (messaggio != null) { %>
        <div class="message-container">
            <div class="message">
                <span class="message-content"><%= messaggio %></span>
            </div>
        </div>
        <% } %>
    </div>
</div>

</body>
</html>