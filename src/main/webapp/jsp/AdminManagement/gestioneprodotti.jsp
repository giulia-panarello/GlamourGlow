<%@ page import="com.glamourglow.glamourglow.model.mo.Prodotto" %>
<%--
  Created by IntelliJ IDEA.
  User: giuliapanarello
  Date: 03/12/24
  Time: 12:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.glamourglow.glamourglow.model.mo.Prodotto" %>
<%@ page import="com.glamourglow.glamourglow.model.mo.Marchio" %>
<%@ page import="com.glamourglow.glamourglow.model.mo.Categoria" %>
<%

    List<Prodotto> prodottiList = (List<Prodotto>) request.getAttribute("prodotti");
    List<Marchio> marchioList = (List<Marchio>) request.getAttribute("marchi");
    List<Categoria> categoriaList = (List<Categoria>) request.getAttribute("categorie");
    String messaggio = (String) request.getAttribute("applicationMessage");
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestione Prodotti - GlamourGlow</title>
    <link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/images/prod.png">
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
            overflow: hidden;
            height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            overflow-x: hidden;
        }



        h1 {
            font-size: 24px;
            color: #333;
            text-align: center;
        }


        .product-container {
            display: flex;
            justify-content: space-between;
            background-color: white;
            border-radius: 10px;
            border: 3px solid #6A5ACD;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            width: 50%;
            margin: 50px auto;
            padding: 20px;
            position: absolute;
            flex-wrap: wrap;
            top: 15%;
            height: 70vh;
            left: 5%;
            overflow-x: hidden;
        }

        .product-items {
            flex: 1;
            overflow-y: auto;
            margin-right: 20px;
            max-height: 50vh;
            overflow-x: hidden;
        }

        .add-product {
            height: 40%;
            width: 400px;
            background-color: #E6E6FA;
            border: 3px solid #6A5ACD;
            color: black;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            position: absolute;
            z-index: 100;
            right: 5%;
            top: 23%;
            overflow-y: auto;
            overflow-x: hidden;
        }

        .field-container label {
            font-weight: bold;
        }


        .product-management-item {
            padding: 15px;
            border: 1px solid #eaeaea;
            border-radius: 5px;
            background-color: #E6E6FA;
            margin-bottom: 15px;
            width: auto;
            overflow-x: hidden;
        }

        .product-header {
            font-size: 24px;
            color: #6A5ACD;
            text-align: center;
            position: relative;
            width: 100%;
            overflow-x: hidden;
        }

        .product-header::after {
            content: '';
            display: block;
            width: 100%;
            height: 2px;
            background-color: #6A5ACD;
            margin-top: 10px;
        }



         input[type="number"] {
            width: 46%;
            padding: 8px;
            margin: 10px 0;
            border: 1px solid #6A5ACD;
            border-radius: 5px;
            font-size: 16px;
        }

        button {
            width: 100%;
            padding: 10px;
            background-color: #6A5ACD;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        button:hover {
            background-color: #4b0082;
        }

        .messaggio {
            text-align: center;
            padding: 10px;
            background-color: #F0E68C;
            border: 2px solid #FFD700;
            border-radius: 5px;
            position: absolute;
            top: 84%;
            left: 66%;
            font-weight: bold;
            color: #483D8B;
        }



        .add-product button {
            position: fixed;
            bottom: 23%;
            right: 71px;
            width: 31.5%;
            padding: 15px 30px;
            background-color: #6A5ACD;
            color: white;
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            cursor: pointer;
            z-index: 1000;
        }

        .add-product button:hover {
            background-color: #4b0082;
        }



        h3 {
            position: relative;
            right: -25%;
            top: -5%;
            color: #6A5ACD;
        }

        .product-image {
            max-width: 100px;
            height: auto;
            border-radius: 5px;
            position: relative;
            transform: translateX(50%);
            left: 33%;
        }

        select {
            width: 50%;
            padding: 10px;
            margin: 8px 0;
            border: 2px solid #6A5ACD;
            border-radius: 5px;
            background-color: #f9f9f9;
            font-size: 16px;
            color: #333;
            box-sizing: border-box;
        }

        select:focus {
            outline: none;
            border-color: #4b0082;
            background-color: #f0f8ff;
        }

        option {
            padding: 10px;
            background-color: #fff;
            color: #333;
        }


        select:hover {
            border-color: #4b0082;
        }

        input[type="text"] {
            width: 50%;
            padding: 8px;
            margin: 10px 0;
            border: 1px solid #6A5ACD;
            border-radius: 5px;
            font-size: 16px;
        }



        .field-container input[type="checkbox"]:checked {
            background-color: #6A5ACD;
            border-color: #4b0082;
        }

        .field-container input[type="checkbox"]:focus {
            outline: none;
            border-color: #4b0082;
        }

        .field-container input[type="checkbox"]:hover {
            background-color: #eee;
        }

        input[type="checkbox"] {
            width: 20px;
            height: 20px;
            margin-right: 10px;
            cursor: pointer;
        }
        .product-icon {
            width: 40px;
            height: 40px;
            margin-right: 10px;
            vertical-align: middle;

        }



        .field-containe {
            display: flex;
            flex-direction: column;
            margin-bottom: 15px;
            width: 100%;
        }

        .field-containe label {
            font-weight: bold;
            margin-bottom: 5px;
        }

        .field-containe input,
        .field-containe select,
        .field-containe textarea {
            padding: 10px;
            border-radius: 5px;
            border: 1px solid #6A5ACD;
            width: 100%;
            font-size: 16px;
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
            top: 70px;
            left: 10%;
            border: 1.5px solid #6A5ACD;
            border-radius: 5%;
            display: none;
            opacity: 0;
            transition: opacity 0.3s ease;
            z-index: 2000;
        }

        .logo-container:hover .cont-admin {
            display: block;
            opacity: 1;
        }


        .field-containe {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }

        .field-containe label {
            margin-left: 10px;
        }

        .field-containe input[type="checkbox"] {
            width: 20px;
            height: 20px;
            margin-right: -6px;
            cursor: pointer;
            position: relative;
        }


    </style>
</head>
<body>

<!-- Header -->
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


<div class="product-container">
    <h2 class="product-header">
        Gestione Prodotti
        <img src="<%= request.getContextPath() %>/images/prod.png" alt="Icona prodotti" class="product-icon">

    </h2>

    <div class="product-items">


        <% for(int i = 0; i < prodottiList.size(); i++) {


        %>
        <div class="product-management-item">
            <form method="post" action="Dispatcher" class="coupon-form">


                <div class="field-container">

                    <img src="<%= request.getContextPath() %>/images/prodotti/<%= prodottiList.get(i).getImmagine() %>" alt="Immagine Prodotto" class="product-image"/>
                </div>

                <div class="field-container">
                    <label for="idprodotto_<%= i %>">ID Prodotto:</label>
                    <input type="hidden" name="idprodotto" id="idprodotto_<%= i %>" value="<%= prodottiList.get(i).getIdProdotto() %>" required readonly />
                    <label style="color: #6A5ACD;"> <%= prodottiList.get(i).getIdProdotto() %> </label>
                </div>

                <div class="field-container">
                    <label for="nomeprodotto_<%= i %>">Nome Prodotto:</label>
                    <input type="text" name="nomeprodotto" id="nomeprodotto_<%= i %>" value="<%= prodottiList.get(i).getNomeProdotto() %>" required />
                </div>

                <div class="field-container">
                    <label for="prezzo_<%= i %>">Prezzo (€):</label>
                    <input type="number" step="0.01" name="prezzo" id="prezzo_<%= i %>" value="<%=prodottiList.get(i).getPrezzo() %>" required />
                </div>

                <div class="field-container">
                    <label for="quantita_<%= i %>">Quantità Disponibile:</label>
                    <input type="number" name="quantita" id="quantita_<%= i %>" value="<%= prodottiList.get(i).getQuantitaDispo() %>" required />
                </div>

                <div class="field-container">

                    <label for="promozione_<%= i %>">In Promozione:</label>
                    <input type="checkbox" value="true" name="promozione" id="promozione_<%= i %>" <%= prodottiList.get(i).getInPromo() ? "checked" : "" %> />
                </div>


                <div class="field-container">
                    <label for="prezzo_scontato_<%= i %>">Prezzo Scontato (€):</label>
                    <input type="number" name="prezzo_scontato" id="prezzo_scontato_<%= i %>" value="<%=prodottiList.get(i).getPrezzoSconto() %>" />
                </div>

                <div class="field-container">
                    <label for="stato_<%= i %>">Stato del Prodotto:</label>
                    <select name="stato" id="stato_<%= i %>" required>
                        <% if(prodottiList.get(i).isStatoprodotto()) {
                            %>
                        <option value="false" >Attivo</option>
                        <option value="true" selected >Bloccato</option>
                        <%
                        }
                        else{ %>
                        <option value="false" selected >Attivo</option>
                        <option value="true" >Bloccato</option>
                        <%

                        }
                        %>

                    </select>
                </div>


                <input type="hidden" name="controllerAction" value="AdminManagement.modificaprodotto" />
                <button type="submit">Modifica</button>

            </form>
        </div>
        <% } %>
    </div>



</div>


<% if(messaggio != null) {
%> <div class="messaggio"> <%=messaggio%> </div><%
    } %>

<form method="post" action="Dispatcher" class="add-product">
    <h3>Aggiungi nuovo prodotto</h3>

    <div class="field-containe">
        <label for="nomeprodotto">Nome Prodotto:</label>
        <input type="text" name="nomeprodotto" id="nomeprodotto" placeholder="Nome del prodotto" required />
    </div>


    <div class="field-containe">
        <label for="descrizione">Descrizione Prodotto:</label>
        <textarea name="descrizione" id="descrizione" placeholder="Descrizione del prodotto" required></textarea>
    </div>


    <div class="field-containe">
        <label for="prezzo">Prezzo (€):</label>
        <input type="number" name="prezzo" id="prezzo" step="0.01" placeholder="Prezzo del prodotto" required />
    </div>


    <div class="field-containe">
        <label for="quantita">Quantità Disponibile:</label>
        <input type="number" name="quantita" id="quantita" placeholder="Quantità disponibile" required />
    </div>


    <div class="field-containe">
        <label for="immagine">Immagine Prodotto:</label>
        <input type="text" name="immagine" id="immagine" placeholder="Immagine" required  />
    </div>


    <div class="field-containe">
        <label for="categoria">Categoria:</label>
        <select name="categoria" id="categoria" required>
            <% for(int i=0; i<categoriaList.size(); i++) {
            %><option value="<%=categoriaList.get(i).getIdCategoria()%>"> <%=categoriaList.get(i).getNomeCategoria()%></option><%
            }%>
        </select>
    </div>


    <div class="field-containe">

        <label for="promozione">In Promozione:</label>
        <input type="checkbox" value="true" name="promozione" id="promozione" />

    </div>


    <div class="field-containe">
        <label for="prezzo_scontato">Prezzo Scontato (€):</label>
        <input type="number" value="0.00" name="prezzo_scontato" id="prezzo_scontato" step="0.01" placeholder="Prezzo scontato" />
    </div>


    <div class="field-containe">
        <label for="stato">Stato del Prodotto:</label>
        <select name="stato" id="stato" required>
            <option value="false">Attivo</option>
            <option value="true">Bloccato</option>
        </select>
    </div>

    <div class="field-containe">
        <label for="marchio">Marchio:</label>
        <select name="marchio" id="marchio" required>
            <% for(int i=0; i<marchioList.size(); i++) {
                %><option value="<%=marchioList.get(i).getIdMarchio()%>"> <%=marchioList.get(i).getNomeMarchio()%></option><%
            }%>

        </select>

    </div>


    <div class="field-containe">
        <input type="hidden" name="controllerAction" value="AdminManagement.aggiungiprodotti" />
        <button type="submit">Aggiungi Prodotto</button>
    </div>


</form>


</body>
</html>
