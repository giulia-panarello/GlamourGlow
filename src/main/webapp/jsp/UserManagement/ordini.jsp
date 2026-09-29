<%@ page import="com.glamourglow.glamourglow.model.mo.Ordine" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: giuliapanarello
  Date: 24/10/24
  Time: 12:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.glamourglow.glamourglow.model.mo.Ordine" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Ordine> ordini = (List<Ordine>) request.getAttribute("ordini");
%>
<html>
<head>

    <meta charset="UTF-8">
    <title>Storico Ordini - GlamourGlow</title>
    <link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/images/storico.png">
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
            overflow: hidden;
            height: 100vh;
            display: flex;
            flex-direction: column;
        }

        .orders-container {
            background-color: white;
            border-radius: 10px;
            border: 3px solid #6A5ACD;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            width: 55%;
            height: calc(70vh - 100px);
            margin: auto;
            padding: 40px;
            display: flex;
            flex-direction: column;
            position: relative;
            overflow-y: auto;
            overflow-x: hidden;
            margin-top: 11%;
        }



        .order-header::after {
            content: '';
            display: block;
            width: 100%;
            height: 2px;
            background-color: #6A5ACD;
            margin-top: 10px;
        }


        .orders-container {
            width: 60%;
            background-color: white;
            border-radius: 10px;
            border: 3px solid #6A5ACD;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            padding: 20px;
            display: flex;
            flex-direction: column;
            height: 70vh;
        }

        .order-header {
            text-align: center;
            font-size: 24px;
            color: #6A5ACD;
            margin-bottom: 20px;
        }

        .order-header img {
            width: 40px;
            height: 40px;
            vertical-align: middle;
            margin-left: 10px;
        }

        .order-items {
            flex: 1;
            overflow-y: auto;
        }

        .order-management-item {
            background-color: #E6E6FA;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .order-details {
            display: grid;
            grid-template-columns: 2fr 1fr 1fr 1fr;
            margin-top: 10px;
        }

        .order-details span {
            padding: 5px 0;
            border-bottom: 1px solid #ddd;
        }

        .total-price {
            text-align: right;
            font-weight: bold;
            color: #6A5ACD;
            margin-top: 30px;
        }

        .empty-orders {
            text-align: center;
            font-size: 18px;
            color: #666;
        }

        .button-container {
            text-align: center;
            margin-top: 40px;
        }

        .button-container button {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            background-color: #6A5ACD;
            color: white;
            cursor: pointer;
        }

        .button-container button:hover {
            background-color: #836FFF;
        }

        .order-status {
            color: #6A5ACD;
            margin-top: 1%;
            font-weight: bold;
        }

        .date {
            margin-top: 1%;
            color: #6A5ACD;
            font-weight: bold;
        }

        .order-user {
            font-size: 16px;
            color: #6A5ACD;
            font-weight: bold;
            margin-top: 1%;
            margin-bottom: 10px;
        }

        .order-status input[type="text"] {
            padding: 5px;
            margin-top: 5px;
            border: 1px solid #6A5ACD;
            border-radius: 5px;
            box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
        }

        .order-status input[type="text"]:focus {
            border-color: #4b0082;
        }



    </style>
</head>
<body>

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

<div class="orders-container">
    <h2 class="order-header">
        Storico Ordini
        <img src="<%= request.getContextPath() %>/images/storico.png" alt="Icona Storico">
    </h2>
    <div class="order-items">
        <% if (ordini != null && !ordini.isEmpty()) {
            for (int i = ordini.size() - 1; i >= 0; i--) {
                Ordine ordine = ordini.get(i);
                double totale = 0;
        %>
        <div class="order-management-item">
            <form method="post" action="Dispatcher">

                <div style="color: green; font-weight: bold; font-size: 18px;">
                    Ordine #<%=i+1 %>
                </div>


                <div class="order-status">Stato: <%= ordine.getStatoOrdine() %> </div>


                <div class="date">Data ordine: <%= ordine.getData() %></div>
                <div class="order-details">
                    <span><strong>Nome Prodotto</strong></span>
                    <span><strong>Quantità</strong></span>
                    <span><strong>Prezzo Unitario</strong></span>
                    <span><strong>Importo Totale</strong></span>
                    <% for (int j = 0; j < ordine.getDettagliOrdine().size(); j++) {
                        double prezzoUnitario = ordine.getDettagliOrdine().get(j).getPrezzoUnitario();
                        int quantita = ordine.getDettagliOrdine().get(j).getQuantita();
                        double importo = prezzoUnitario * quantita;

                        totale += importo;
                    %>
                    <span><%= ordine.getDettagliOrdine().get(j).getProdotto().getNomeProdotto() %></span>
                    <span><%= quantita %></span>
                    <span><%= String.format("%.2f", prezzoUnitario) %> €</span>
                    <span><%= String.format("%.2f", importo) %> €</span>
                    <% } %>
                </div>
                <div class="total-price">Totale pagato: <%= String.format("%.2f", totale) %> €</div>

            </form>
        </div>
        <% }
        } else { %>
        <div class="empty-orders">Nessun ordine effettuato.</div>
        <% } %>
    </div>
</div>
</body>
</html>
