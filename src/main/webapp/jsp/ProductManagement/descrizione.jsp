<%@ page import="com.glamourglow.glamourglow.model.mo.Prodotto" %><%--
  Created by IntelliJ IDEA.
  User: giuliapanarello
  Date: 11/10/24
  Time: 11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.glamourglow.glamourglow.model.mo.Prodotto" %>
<%
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    String messaggio = (String) request.getAttribute("applicationMessage");
%>
<html>
<head>
    <title><%= prodotto.getNomeProdotto() %> - Dettagli Prodotto</title>
    <link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/images/tutto.png">
    <style>
        body {
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: pink;
            font-family: Arial, sans-serif;
            position: relative;
        }

        .home-icon {
            margin-right: 10px;
        }

        .home-icon img {
            width: 40px;
            height: 40px;
        }

        .header {
            position: absolute;
            top: 20px;
            left: 20px;
            display: flex;
            align-items: center;
        }

        .product-container {
            max-width: 900px;
            width: 100%;
            margin: 20px;
            padding: 20px;
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: row;
            position: relative;
        }

        .product-container img {
            width: 90%;
            max-width: 350px;
            height: auto;
            border-radius: 10px;
            margin-right: 20px;
            object-fit: cover;
        }

        .product-info {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            flex-grow: 1;
            position: relative;
        }

        .product-name {
            font-size: 28px;
            font-weight: bold;
            color: #6A5ACD;
            margin: 0;
            margin-bottom: 10px;
        }

        .product-description {
            margin: 10px 0;
            font-size: 18px;
            color: #555;
            flex-grow: 1;
        }

        .product-price {
            font-size: 24px;
            color: #333;
            display: flex;
            align-items: center;
            margin-top: 10px;
            margin-bottom: 20px;
        }

        .product-price .discount {
            text-decoration: line-through;
            margin-right: 10px;
        }

        .product-price .discounted-price {
            font-size: 28px;
            color: #6A5ACD;
            margin-right: 10px;
        }

        .availability {
            font-size: 15px;
            color: #8b8c8b;
            margin-top: 6%;
            margin-left: 10px;
        }

        .promo-label {
            font-size: 20px;
            color: #6A5ACD;
            margin-left: 5px;
            font-style: italic;
            display: flex;
            align-items: center;
        }

        .add-to-cart-button {
            background-color: #6A5ACD;
            color: white;
            border: none;
            border-radius: 5px;
            padding: 10px 15px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s;
            display: flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
        }

        .add-to-cart-button img {
            width: 30px;
            height: 30px;
            margin-left: 10px;
        }

        .add-to-cart-button:hover {
            background-color: #483D8B;
        }



        .quantity-select {
            margin-top: 10px;
            margin-bottom: 5px;
            font-size: 16px;
            padding: 5px;
            width: 80px;
            border: 1px solid #ccc;
            border-radius: 5px;
            appearance: none;
            margin-left: 10px;
        }

        .message-container {
            width: 100%;
            max-width: 900px;
            margin: 20px auto;
            padding: 10px;
            background-color: #F0E68C;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            text-align: center;
        }

        .message {
            font-size: 18px;
            text-align: center;
            font-weight: bold;
            color: #483D8B;
        }

        .message-content {
            display: inline-block;
            margin-left: 10px;
        }

        .price-box {
            background-color: #E6E6FA;
            border: 2px solid #6A5ACD;
            border-radius: 8px;
            padding: 15px;
            width: 100%;
            max-width: 400px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 10%;
            margin-top:10%;
        }


    </style>
</head>
<body>

<div class="header">

    <div class="home-icon">
        <a href="Dispatcher?controllerAction=HomeManagement.viewhome">
            <img src="<%= request.getContextPath() %>/images/casa.png" alt="Home">
        </a>
    </div>

    <% if(loggedOn) { %>
    <a href="Dispatcher?controllerAction=UserManagement.viewcarrello" class="add-to-cart-button">
        Vai al Carrello
    </a>
    <% } %>
</div>

<div>
    <% if (messaggio != null) { %>
    <div class="message-container">
        <div class="message">
            <span class="message-content"><%= messaggio %></span>
        </div>
    </div>
    <% } %>
</div>

<div class="product-container">
    <img src="<%= request.getContextPath() %>/images/prodotti/<%= prodotto.getImmagine() %>" alt="<%= prodotto.getNomeProdotto() %>">
    <div class="product-info">
        <div class="product-name"><%= prodotto.getNomeProdotto() %></div>
        <div class="product-description"><%= prodotto.getDescrizione() %></div>

        <div class="product-price">
            <div class="price-box">
                <% if (prodotto.getInPromo()) { %>
                <span class="discount"><%= String.format("%.2f", prodotto.getPrezzo()) %>€</span>
                <span class="discounted-price"><%= String.format("%.2f", prodotto.getPrezzoSconto()) %>€</span>
                <span class="promo-label">
                <em>In promo</em>
                </span>
                <% } else { %>
                <span><%= String.format("%.2f", prodotto.getPrezzo()) %>€</span>
                <% } %>
            </div>
        </div>



        <form action="Dispatcher" method="post" style="display: flex; align-items: center; gap: 20px;">
            <input type="hidden" name="controllerAction" value="UserManagement.aggiungicarrello"/>

            <% if(loggedOn) { %>
            <button type="submit" class="add-to-cart-button">
                Aggiungi al carrello
                <img src="<%= request.getContextPath() %>/images/carr.png" alt="Aggiungi al carrello"/>
            </button>
            <% } else { %>
            <a href="Dispatcher?controllerAction=HomeManagement.viewlogin" class="add-to-cart-button">
                Login per aggiungere al carrello
            </a>
            <% } %>




            <div style="display: flex; flex-direction: row; align-items: center;">
                <div style="display: flex; flex-direction: column; align-items: flex-start; gap: 5px;">
                    <label for="quantita" style="margin-bottom: -17%; margin-left: 10px">Quantità:</label>

                    <select name="quantita" id="quantita" class="quantity-select" style="height: auto; max-height: 100px; overflow-y: auto;">
                        <% for (int i = 1; i <= prodotto.getQuantitaDispo(); i++) { %>
                        <option value="<%= i %>"><%= i %></option>
                        <% } %>
                    </select>
                </div>
                <div class="availability" style="margin-left: 20px;">Disponibilità: <%= prodotto.getQuantitaDispo() %></div>
            </div>


            <input type="hidden" name="idprod" value="<%= prodotto.getIdProdotto() %>"/>
        </form>
    </div>
</div>
</body>
</html>
