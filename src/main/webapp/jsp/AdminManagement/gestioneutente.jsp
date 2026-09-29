<%@ page import="com.glamourglow.glamourglow.model.mo.Utente" %>
<%@ page import="java.util.List" %>
<%@ page import="com.glamourglow.glamourglow.model.mo.Ordine" %><%--
  Created by IntelliJ IDEA.
  User: giuliapanarello
  Date: 25/10/24
  Time: 10:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.glamourglow.glamourglow.model.mo.Utente" %>
<%@ page import="java.util.List" %>
<%@ page import="com.glamourglow.glamourglow.model.mo.Ordine" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Utente> utenteList = (List<Utente>) request.getAttribute("utenteList");
    List<List<Ordine>> ordini = (List<List<Ordine>>) request.getAttribute("ordini");
    String messaggio = (String) request.getAttribute("applicationMessage");
%>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione Utenti - GlamourGlow </title>
    <link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/images/utente.png">
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
            overflow: hidden;
            height: 100vh;
            display: flex;
            flex-direction: column;
        }

        h1 {
            text-align: center;
            color: #6A5ACD;
        }

        .message {
            color: #ff0000;
            text-align: center;
            margin-bottom: 20px;
        }


        .utente-form {
            display: flex;
            flex-direction: column;
            margin-bottom: 15px;
        }

        .field-container {
            display: flex;
            justify-content: flex-start;
            align-items: center;
            margin-bottom: 10px;
        }

        .field-container label {
            margin-right: 10px;
            font-weight: bold;
        }




        .utente-form select {
            width: 38%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #6A5ACD;
            border-radius: 5px;
            box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
            background-color: #f9f9f9;
            appearance: none;
        }



        .utente-container {
            background-color: white;
            border-radius: 10px;
            border: 3px solid #6A5ACD;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            width: 55%;
            height: auto;
            margin: auto;
            padding: 45px;
            display: flex;
            flex-direction: column;
            overflow-y: auto;
            margin-top: 0;
            margin-bottom: 0;
        }

        .utente-header {
            font-size: 24px;
            color: #6A5ACD;
            text-align: center;
            position: relative;
        }

        .utente-header::after {
            content: '';
            display: block;
            width: 100%;
            height: 2px;
            background-color: #6A5ACD;
            margin-top: 10px;
        }

        .utente-items {
            flex: 1;
            overflow-y: auto;
            max-height: 300px;
            margin-bottom: 20px;
        }

        .user-management-item {
            padding: 15px;
            border: 1px solid #eaeaea;
            border-radius: 5px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            position: relative;
            background-color: #E6E6FA;
            margin-bottom: 5%;
        }

        .utente-form input[type="email"],
        .utente-form input[type="text"],
        .utente-form input[type="hidden"],
        .utente-form select {
            width: 50%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #6A5ACD;
            border-radius: 5px;
            box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
        }

        .utente-form button {
            width: 100%;
            background-color: #6A5ACD;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            margin-top: 15px;
            text-align: center;
        }


        .utente-form button:hover {
            background-color: #836FFF;
        }

        .user-icon {
            width: 40px;
            height: 40px;
            margin-right: 10px;
            vertical-align: middle;
        }

        .cont-admin a {
            color: #6A5ACD;
            font-size: 15px;
            display: block;
            margin: 5px 0;
            text-decoration: none;
        }

        .cont-admin a:hover {
            text-decoration: underline;
        }


        .cont-admin {
            background-color: white;
            padding: 10px;
            position: absolute;
            top: 92px;
            left: 10%;
            border: 1.5px solid #6A5ACD;
            border-radius: 5%;
            display: none;
            opacity: 0;
            transition: opacity 0.3s ease;
        }

        .logo-container:hover .cont-admin {
            display: block;
            opacity: 1;
        }

    </style>
</head>
<body>


<div class="header">
    <div class="logo-container">
        <img src="<%= request.getContextPath() %>/images/logo.png" alt="Logo">
        <h1>GLAMOURGLOW</h1>
        <div class="cont-admin">
            <a href="Dispatcher?controllerAction=AdminManagement.gestioneutente">Gestione Utenti</a>
            <a href="Dispatcher?controllerAction=AdminManagement.gestioneordini">Gestione Ordini</a>
            <a href="Dispatcher?controllerAction=AdminManagement.gestionecoupon">Gestione Coupon</a>
            <a href="Dispatcher?controllerAction=AdminManagement.gestioneprodotti">Gestione Prodotti</a>
        </div>
    </div>
    <div class="home-icon">
        <a href="Dispatcher?controllerAction=HomeManagement.viewhome">
            <img src="<%= request.getContextPath() %>/images/cas.png" alt="Home">
        </a>
    </div>
</div>

<div class="utente-container">
    <h2 class="utente-header">
        Gestione Utenti
        <img src="<%= request.getContextPath() %>/images/utente.png" alt="Icona Utente" class="user-icon">
    </h2>

    <div class="utente-items">
        <% if(messaggio != null){ %>
        <div class="message"><%= messaggio %></div>
        <% } %>

        <% for(int i = 0; i < utenteList.size(); i++) { %>
        <div class="user-management-item">
            <form method="post" action="Dispatcher" class="utente-form">


                <div class="field-container">
                    <label>Nome:</label>
                    <span><%= utenteList.get(i).getNome() %></span>
                </div>

                <div class="field-container">
                    <label>Cognome:</label>
                    <span><%= utenteList.get(i).getCognome() %></span>
                </div>

                <div class="field-container">
                    <label>Email:</label>
                    <input type="email" value="<%= utenteList.get(i).getEmail() %>" name="email" required />
                </div>

                <div class="field-container">
                    <label>Telefono:</label>
                    <span><%= utenteList.get(i).getTelefono() %></span>
                </div>

                <div class="field-container">
                    <label>Ruolo:</label>
                    <select name="ruolo" required>
                        <option value="true" <%= utenteList.get(i).isRuolo() ? "selected" : "" %>>Amministratore</option>
                        <option value="false" <%= !utenteList.get(i).isRuolo() ? "selected" : "" %>>Registrato</option>
                    </select>
                </div>

                <div class="field-container">
                    <label>Stato:</label>
                    <select name="stato" required>
                        <option value="bloccato" <%= utenteList.get(i).getStatoAccount().equals("bloccato") ? "selected" : "" %>>Bloccato</option>
                        <option value="attivo" <%= utenteList.get(i).getStatoAccount().equals("attivo") ? "selected" : "" %>>Attivo</option>
                    </select>
                </div>

                <input type="hidden" name="controllerAction" value="AdminManagement.modificautente" />
                <input type="hidden" name="id" value="<%= utenteList.get(i).getIdNome() %>" />

                <div class="field-container">
                    <label>Numero Ordini:</label>
                    <span><%= ordini.get(i).size() %></span>
                </div>

                    <button type="submit">Modifica</button>

            </form>
        </div>
        <% } %>
    </div>
</div>
</body>
</html>