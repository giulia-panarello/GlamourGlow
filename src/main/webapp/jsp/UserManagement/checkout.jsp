<%@ page import="com.glamourglow.glamourglow.model.mo.Carrello" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: giuliapanarello
  Date: 22/10/24
  Time: 11:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.glamourglow.glamourglow.model.mo.Carrello" %>
<%@ page import="java.util.List" %>
<%@ page import="com.glamourglow.glamourglow.model.mo.Utente" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<%
    List<Carrello> elementi = (List<Carrello>) request.getAttribute("carrello");
    String[] coupon = (String[]) request.getAttribute("coupon");
    String[] sconto = (String[]) request.getAttribute("sconto");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    Boolean loggedOn = (Boolean) request.getAttribute("loggedOn");

    String messaggio = (String) request.getAttribute("applicationMessage");
    double prezzoTotaleFinale = 0;
%>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>GlamourGlow - Checkout</title>
    <link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/images/logo.png">
    <style>
        body {
            margin: 0;
            padding: 0;
            height: 100vh;
            display: flex;
            flex-direction: column;
            overflow: hidden;
            align-items: center;
            background-color: pink;
            font-family: Arial, sans-serif;
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
        .container {
            display: flex;
            justify-content: space-between;
            padding: 20px;
            width: 90%;
            max-width: 1333px;
            margin: 20px auto;
        }


        .prodotti h1 {
            color: #6A5ACD;
            font-size: 24px;
            margin-bottom: 10px;
        }


        .prodotti-header h1::after {
            content: '';
            display: block;
            width: 100%;
            height: 2px;
            background-color: #6A5ACD;
            margin-top: 10px;
        }

        .prodotti-header {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 2000);
            padding: 20px;
            width: 543px;
            position: relative; /* Imposta la posizione relativa per il contenitore */
            text-align: center;
            margin-top: 3.5%;
            height: 442px;
            border: 3px solid #6A5ACD;
            display: flex;
            flex-direction: column;
            margin-left: 11%;
        }

        .verifica {
            background-color: #6A5ACD;
            color: white;
            border: none;
            padding: 10px 20px;
            font-size: 18px;
            border-radius: 5px;
            cursor: pointer;
            width: 80%;
            text-align: center;
            position: absolute;
            bottom: 0;
            left: 50%;
            margin-bottom: 1%;
            transform: translateX(-50%);
        }

        .items-container {
            overflow-y: auto;
            flex-grow: 1;
            padding-right: 10px;
            margin-top: 10px;
            height: 100%;
            padding-bottom: 60px; /* Margine inferiore per evitare sovrapposizioni */
        }


        h1 {
            text-align: center;
            color: #6A5ACD;
        }
        .item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #D3D3D3;
            padding: 15px 0;
            width: 100%;
            position: relative;
        }
        .item:last-child {
            border-bottom: none;
        }
        .product-name {
            margin-right: 10px;
            white-space: normal; /* Consente al testo di andare a capo */
            overflow-wrap: break-word; /* Permette il wrapping del testo */
            word-break: break-word; /* Alternativa per spezzare le parole lunghe */
            max-width: 150px; /* Puoi modificare questo valore a seconda delle tue necessità */
            text-align: left; /* Allinea il testo a sinistra */
            font-size: 13px;
        }


        .total {
            font-weight: bold;
            text-align: center;
            margin-top: 10px;
            color: #483D8B;
            font-size: 28px;
        }
        .coupon-input {
            width: 153px;
            padding: 4px;
            border: 2px solid #6A5ACD;
            margin-right: 0;
            font-size: 14px;
            transition: border-color 0.3s, box-shadow 0.3s;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            margin-left: 30%;
            position: absolute;
            border-radius: 5px;
            bottom: 27%;
        }
        .coupon-input:focus {
            border-color: #5A4EB8;
            outline: none;
            box-shadow: 0 0 8px rgba(106, 90, 205, 0.5);
        }

        .verifica:hover {
            background-color: #836FFF;
        }
        .cart-icon img {
            width: 40px;
            height: 40px;
            margin-right: 60px;
        }
        .totale {
            flex: 0 0 300px;
            background-color: #E6E6FA;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            margin-right: 1.65%;
            height: 90%;
        }
        .quantity {
            margin-left: 7%;
            font-weight: bold;
            color: #6A5ACD;
            white-space: nowrap;
            position: absolute;
            bottom: 15%;


        }
        .price {
            margin-left: 45px;
            color: #6A5ACD;
            font-weight: bold;
            white-space: nowrap;
            position: absolute;
            left: 80%;
            bottom: 32%;
        }
        .coupon-container {
            display: flex;
            align-items: center;
            margin-top: 10px;
        }


        .alert-message-container {
            width: 89%;
            max-width: 31%;
            margin: 20px auto;
            padding: 10px;
            background-color: #F0E68C;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            text-align: center;
            position: absolute;
            top: 15%;
            font-weight: bold;
            right: 3%;
        }

        .alert-message {
            font-size: 16px;
            color: #483D8B;
        }


        .address-input {
            width: 100%;
            padding: 10px;
            margin: 5px 0;
            border: 2px solid #6A5ACD;
            border-radius: 5px;
            font-size: 16px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            transition: border-color 0.3s, box-shadow 0.3s;
        }
        .address-input:focus {
            border-color: #5A4EB8;
            outline: none;
            box-shadow: 0 0 8px rgba(106, 90, 205, 0.5);
        }
        .checkout {
            background-color: #6A5ACD;
            color: white;
            border: none;
            padding: 10px 20px;
            font-size: 18px;
            border-radius: 5px;
            width: 100%;
            cursor: pointer;
            transition: background-color 0.3s;


        }

        .checkout:hover {
            background-color: #45a049;
        }

        .totale-riquadro {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 2000);
            text-align: center;
            margin-top: 20px;
            width: 63%;
            border: 2px solid #6A5ACD;

        }

        .totale-riquadro h2{
            color: #6A5ACD;
        }
        .totale h2 {
            color: #6A5ACD;
            font-size: 20px;
        }

        .messaggio{
            z-index: 2000;
            width: 350px;
            background-color: #F0E68C;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            position: absolute;
            top: -17%;
            right: 15%;
            text-align: center;
            font-weight: bold;
            color: #483D8B;
        }




    </style>
</head>
<body>

<div class="header">
    <div class="logo-container">
        <img src="<%= request.getContextPath() %>/images/logo.png" alt="Logo">
        <h1>GLAMOURGLOW</h1>
    </div>
    <div class="cart-icon">
        <a href="Dispatcher?controllerAction=UserManagement.viewcarrello">
            <img src="<%= request.getContextPath() %>/images/dietro.png" alt="Carrello" />
        </a>
    </div>
</div>

<div class="container">
    <div class="prodotti-header">
        <h1>Riepilogo Prodotti</h1>

        <div class="items-container">
            <% if (elementi != null) { %>
            <form method="post" action="Dispatcher">

                <%
                    for (int i = 0; i < elementi.size(); i++) {
                %>
                <div class="item">
                    <div style="display: flex; align-items: center;">
                        <img src="<%= request.getContextPath() %>/images/prodotti/<%= elementi.get(i).getProdotto().getImmagine() %>" alt="<%= elementi.get(i).getProdotto().getNomeProdotto() %>" style="width: 70px; height: 70px; object-fit: contain;" />
                        <div style="display: flex; flex-direction: column; margin-left: 10px;">

                            <div class="product-name"><%= elementi.get(i).getProdotto().getNomeProdotto() %></div>
                            <span class="quantity"> x <%= elementi.get(i).getQta() %></span>

                            <div class="coupon-container" style="margin-top: 5px; display: flex; align-items: center;">
                                <%
                                    if(coupon != null) {
                                        if(coupon[i] != null) {
                                %> <input type="text" name="coupon" value="<%= coupon[i] %>" placeholder="Inserisci coupon" class="coupon-input" /><%
                            } else {
                            %> <input type="text" name="coupon" placeholder="Inserisci coupon" class="coupon-input" /><%
                                }
                            } else {
                            %> <input type="text" name="coupon" placeholder="Inserisci coupon" class="coupon-input" /><%
                                }
                            %>
                            </div>
                        </div>
                    </div>

                    <span class="price">
                    <%
                        double prezzoProdotto;
                        if (elementi.get(i).getProdotto().getInPromo()) {
                            prezzoProdotto = elementi.get(i).getProdotto().getPrezzoSconto();
                        } else {
                            prezzoProdotto = elementi.get(i).getProdotto().getPrezzo();
                        }
                        int quantita = elementi.get(i).getQta();
                        double prezzoTotale = prezzoProdotto * quantita;
                        double scontoApplicato = 0;

                        // Calcolo dello sconto se presente
                        if (coupon != null && coupon[i] != null && sconto[i] != null) {
                            scontoApplicato = prezzoTotale * (Float.parseFloat(sconto[i]) / 100);
                            prezzoTotale -= scontoApplicato;
                        }

                        prezzoTotaleFinale += prezzoTotale;

                        // Visualizzazione della percentuale di sconto
                        if (scontoApplicato > 0) {

                            out.print("<span style='color: green; position: absolute; left: -84%; bottom:-3%; '>-" + String.format("%.0f", Float.parseFloat(sconto[i])) + "% </span>");
                        }

                        // Visualizzazione del prezzo finale
                        out.print( String.format("%.2f", prezzoTotale) + " &euro; ");
                    %>
                </span>

                </div>
                <%
                    }
                %>
                <input type="hidden" name="controllerAction" value="UserManagement.verificacoupon">
               <button type="submit" class="verifica">Verifica coupon</button>

                <% if(messaggio != null) {
                %> <div class="messaggio"> <%=messaggio%> </div><%
                } %>
            </form>
            <% } else { %>
            <p>Il carrello è vuoto.</p>
            <% } %>
        </div>

    </div>

        <form class="totale" action="Dispatcher" method="post">
            <div class="totale-riquadro"> <h2>Totale:  <img src="<%= request.getContextPath() %>/images/scontrino.png" alt="Scontrino" style="width: 40px; height: 40px; margin-right: 8px; vertical-align: middle;"></h2>
                <span class="total">
            <%
                out.print(String.format("%.2f", prezzoTotaleFinale) + " &euro; ");
            %>
        </span>
            </div>
            <h2>Indirizzo di Spedizione  <img src="<%= request.getContextPath() %>/images/spe.png" alt="Spedizione" style="width: 40px; height: 40px; margin-right: 8px; vertical-align: middle;"></h2>
            <input type="text" name="Indirizzo" placeholder="Inserisci indirizzo di consegna" required class="address-input" />
            <input type="text" name="Stato" placeholder="Inserisci Stato" required class="address-input" />
            <input type="text" name="Citta" placeholder="Inserisci Città" required class="address-input" />
            <% if (coupon != null) {
                for (String c : coupon) { %>
            <input type="hidden" name="coupon" value="<%= c %>" />
            <% } } %>
            <button type="submit" class="checkout">Checkout</button>
            <input type="hidden" name="controllerAction" value="UserManagement.acquisto" />

        </form>


    </div>
<% if (loggedUser.getWallet() < prezzoTotaleFinale) { %>
<div class="alert-message-container">
    <div class="alert-message">Non hai abbastanza credito! (Credito disponibile: <%= String.format("%.2f", loggedUser.getWallet()) %> €) Ricaricalo
        <a href="Dispatcher?controllerAction=UserManagement.ricaricasaldo">Qui</a>
    </div>
</div>
<% } %>

</body>
</html>