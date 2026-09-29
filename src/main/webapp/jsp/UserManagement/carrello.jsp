<%@ page import="com.glamourglow.glamourglow.model.mo.Carrello" %>
<%--
  Created by IntelliJ IDEA.
  User: giuliapanarello
  Date: 15/10/24
  Time: 13:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.glamourglow.glamourglow.model.mo.Carrello" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>

<%
    List<Carrello> elementi = (List<Carrello>) request.getAttribute("carrello");
    double totaleCarrello = 0.0;
    String messaggio = (String) request.getAttribute("applicationMessage");
%>
<html>
<head>

    <title>Carrello - GlamourGlow</title>
    <link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/images/carrello.png">
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


        .cart-header {
            font-size: 24px;
            color: #6A5ACD;
            position: relative;
        }

        .cart-header::after {
            content: '';
            display: block;
            width: 100%;
            height: 2px;
            background-color: #6A5ACD;
            margin-top: 10px;
        }



        .cart-item:last-child {
            border-bottom: none;
        }

        .product-info {
            display: flex;
            align-items: center;
        }

        .product-info img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            margin-right: 20px;
        }

        .product-info h3 {
            font-size: 18px;
            color: #333;
            margin-left: 14%;
        }
        .product-info p {
            color: #666;
            margin-left: 14%;
            white-space: nowrap;
        }

        .cart-item {
            display: grid;
            grid-template-columns: 1fr auto;
            justify-content: space-between;
            align-items: center;
            padding: 15px;
            border-bottom: 1px solid #ddd;
        }

        .price {
            font-size: 18px;
            font-weight: bold;
            color: #6A5ACD;
            text-align: right;
            width: 100px;
        }


        .empty-cart {
            text-align: center;
            font-size: 24px;
            color: #999;
            margin-top: 50px;
        }


        .quantity-container input {
            width: 50px;
            text-align: center;
            font-size: 16px;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin: 0 5px;
            padding: 2px;
        }

        .checkout-button:hover {
            background-color: #5B4ACD;
        }

        .product-info img {
            margin-right:-3px;
        }

        .product-info button img {
            margin-left: -3px;
        }

        .stock-info {
            font-size: 14px;
            color: #555;
            margin-top: 5px;

        }

        .stock-alert {
            font-size: 14px;
            color: red;
            text-decoration: line-through;
        }


        .quantity-label-container {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }

        .quantity-label {
            margin-right: 22px;
            font-size: 16px;
            color: #6A5ACD;
            margin-left: 14%;
        }

        .quantity-container {
            display: flex;
            align-items: center;
        }

        .quantity-container button {
            background-color: #6A5ACD;
            color: white;
            border: none;
            border-radius: 5px;
            width: 30px;
            height: 23px;
            cursor: pointer;
            font-size: 18px;
            margin-left: -5px;
            margin-right: -5px;
        }


        .quantity-container button:hover {
            background-color: #5B4ACD;
        }

        input[type="number"]::-webkit-inner-spin-button,
        input[type="number"]::-webkit-outer-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }

        input[type="number"] {
            -moz-appearance: textfield;
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

        .cart-container {
            background-color: white;
            border-radius: 10px;
            border: 3px solid #6A5ACD;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 2000);
            width: 55%;
            height: calc(70vh - 100px);
            margin: auto;
            padding: 40px;
            display: flex;
            flex-direction: column;
            position: relative;
            margin-top: 0%;
        }
        .cart-items {
            flex: 1;
            overflow-y: auto;
            max-height: 300px;
            margin-bottom: 20px;
        }

        .checkout-button {
            background-color: #6A5ACD;
            color: white;
            padding: 15px 20px;
            font-size: 18px;
            border-radius: 5px;
            border: none;
            position: fixed;
            bottom: 34px;
            left: 50%;
            transform: translateX(-50%);
            width: 55%;
            z-index: 100;
            cursor: pointer;
            text-align: center;
            text-decoration: none;
        }

        .total-container {
            display: flex;
            justify-content: space-between;
            font-size: 20px;
            font-weight: bold;
            color: #6A5ACD;
            padding: 10px 20px;
            background-color: #E6E6FA;
            border-top: 1px solid #ddd;
            margin-top: auto;
        }

        .messaggio {

            width: 350px;
            font-weight: bold;
            background-color: #F0E68C;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            position: absolute;
            top: 25%;
            right: 28%;
            text-align: center;
            color: #483D8B;
        }


    </style>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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


<div class="cart-container">
    <h2 class="cart-header">
        Il Tuo Carrello  <img src="<%= request.getContextPath() %>/images/cart.png" alt="Carrello" style="width: 40px; height: 40px; vertical-align: middle; margin-right: 10px;">

    </h2>

    <div class="cart-items">
        <%
            if (elementi != null && !elementi.isEmpty()) {
                java.util.Collections.reverse(elementi);
                int i=0;
                for (Carrello item : elementi) {

                    double prezzoTotale;
                    if(item.getProdotto().getInPromo()){
                        prezzoTotale = item.getProdotto().getPrezzoSconto() * item.getQta();
                        totaleCarrello += prezzoTotale;
        %><input type="hidden" id="pu<%=i%>" name="pu" value="<%=item.getProdotto().getPrezzoSconto()%>"> <%
    }
    else {
        prezzoTotale = item.getProdotto().getPrezzo() * item.getQta();
        totaleCarrello += prezzoTotale;
    %><input type="hidden" id="pu<%=i%>" name="pu" value="<%=item.getProdotto().getPrezzo()%>"> <%
        }
    %>
        <div class="cart-item" id="cart-item<%=i%>">
            <div class="product-info">
                <img src="<%= request.getContextPath() %>/images/prodotti/<%= item.getProdotto().getImmagine() %>" alt="<%= item.getProdotto().getNomeProdotto() %>">
                <form method="post" action="Dispatcher" id="prodottocarrello<%=i%>">
                    <input type="hidden" name="controllerAction" value="UserManagement.aggiungicarrello">
                    <input type="hidden" id="idprod<%=i%>" name="idprod" value="<%=item.getProdotto().getIdProdotto()%>">

                    <h3><%= item.getProdotto().getNomeProdotto() %></h3>
                    <p>
                    <div class="quantity-label-container">
                        <span class="quantity-label">Quantità:</span>
                        <div class="quantity-container">
                            <button type="button" onclick="decrementQuantity(<%= i %>)">-</button>
                            <input type="number" min="1" value="<%= item.getQta() %>" id="qta<%= i %>" name="tmp" data-quantita-vecchia="<%= item.getQta() %>" required/>
                            <button type="button" onclick="incrementQuantity(<%= i %>)">+</button>
                        </div>
                    </div>
                    </p>



                    <% if(item.getQta() > item.getProdotto().getQuantitaDispo()) { %>
                    <p class="stock-alert" id="stock<%=i%>">Disponibilità in magazzino: <span id="qtamag<%=i%>"><%= item.getProdotto().getQuantitaDispo() %></span> pezzi</p>
                    <% } else { %>
                    <p class="stock-info" id="stock<%=i%>">Disponibilità in magazzino: <span id="qtamag<%=i%>" ><%= item.getProdotto().getQuantitaDispo() %></span> pezzi</p>
                    <% } %>
                </form>
            </div>
            <div class="price" id="prezzotot<%=i%>">
                <%= String.format("%.2f", prezzoTotale) %> €
            </div>

            <form action="Dispatcher" method="POST" style="margin-left: 20px;">
                <input type="hidden" name="controllerAction" value="UserManagement.aggiungicarrello">
                <input type="hidden" name="idprod" value="<%= item.getProdotto().getIdProdotto() %>">
                <input type="hidden" name="quantita" value="0" required>
                <button type="submit" style="border: none; background: none; cursor: pointer;" >
                    <img src="<%= request.getContextPath() %>/images/cestino.png" alt="Elimina" style="width: 30px; height: 30px; ">
                </button>
            </form>
        </div>
        <%
                i++;
            }
        %>
    </div>


    <div class="total-container">
        <span>Totale Carrello:</span>
        <span id="totcarrello"><%= String.format("%.2f", totaleCarrello) %> €</span>
    </div>
    <%
    } else {
    %>
    <p class="empty-cart">Il tuo carrello è vuoto.</p>
    <%
        }
    %>
</div>
</div>


<%
    if (elementi != null && !elementi.isEmpty()) {
%>
<a href="Dispatcher?controllerAction=UserManagement.viewcheckout" class="checkout-button">Procedi al Checkout</a>
<% if(messaggio != null){
%><div class="messaggio"> <%=messaggio%> </div><%
        }
    }
%>

<script>
    $(document).ready(function () {

        let j = 0;
        <% if (elementi == null) { %>
        j = 0;
        <% } else { %>
        j = <%= elementi.size() %>;
        <% } %>



        let totcarrello = parseFloat($("#totcarrello").text().replace(' €', ''));

        let k = j;

        let variabileausiliaria = [];
        for (let i = 0; i < j; i++) {



            const qtamag = parseInt(document.getElementById("qtamag" + i).innerHTML);
            const qtaInput = $("#qta" + i);
            const stock = document.getElementById("stock" + i);
            const elemento = document.getElementById("cart-item" + i);
            const pu = parseFloat(document.getElementById("pu" + i).value);
            let prezzotot = parseFloat($("#prezzotot" + i).text().replace(' €', ''));

            qtaInput.data('quantita-vecchia', qtaInput.val());
            qtaInput.on('input', function () {
                let nuovoValore = parseInt($(this).val());
                let vecchioValore = parseInt($(this).data('quantita-vecchia'));
                console.log("vecchioVal:"+vecchioValore);
                console.log("nuovoVal:"+nuovoValore);
                let differenzaQuantita = nuovoValore - vecchioValore;
                if(isNaN(vecchioValore))
               {
                   differenzaQuantita = nuovoValore;
               }


                if(isNaN(nuovoValore))
                {
                    variabileausiliaria[i]=vecchioValore;
                    console.log("assegno varausiliaria:"+variabileausiliaria);
                    differenzaQuantita = 0 - vecchioValore;
                }
                console.log("differenzaQta:"+differenzaQuantita);

                if (nuovoValore > qtamag) {
                    stock.classList.add("stock-alert");
                    stock.classList.remove("stock-info");
                } else {
                    stock.classList.remove("stock-alert");
                    stock.classList.add("stock-info");
                }

                if (nuovoValore > 0) {
                    if(isNaN(vecchioValore))
                    {
                        console.log("varausiliaria:"+variabileausiliaria[i]);
                        let tmpval = differenzaQuantita-variabileausiliaria[i];
                        if(tmpval != 0)
                        {
                            console.log("dentro differenzaqta-variabileausiliaria !=0");
                            differenzaQuantita-=variabileausiliaria[i];
                            console.log("diffqta con variabile ausiliaria:"+differenzaQuantita);
                            $.ajax({
                                url: "Dispatcher",
                                type: "POST",
                                data: {
                                    controllerAction: "UserManagement.aggiungicarrello",
                                    idprod: $("#idprod" + i).val(),
                                    quantita: differenzaQuantita
                                },
                                success: function (response) {
                                    console.log("Quantità aggiornata con successo");

                                    let nuovoPrezzoTotale = pu * nuovoValore;
                                    $("#prezzotot" + i).text(nuovoPrezzoTotale.toFixed(2) + ' €');

                                    totcarrello += differenzaQuantita * pu;
                                    $("#totcarrello").text(totcarrello.toFixed(2) + ' €');
                                },
                                error: function (xhr, status, error) {
                                    console.error("Errore nell'aggiornamento della quantità");
                                }
                            });
                        }
                    }
                    else
                    {
                        $.ajax({
                            url: "Dispatcher",
                            type: "POST",
                            data: {
                                controllerAction: "UserManagement.aggiungicarrello",
                                idprod: $("#idprod" + i).val(),
                                quantita: differenzaQuantita
                            },
                            success: function (response) {
                                console.log("Quantità aggiornata con successo");

                                let nuovoPrezzoTotale = pu * nuovoValore;
                                $("#prezzotot" + i).text(nuovoPrezzoTotale.toFixed(2) + ' €');

                                totcarrello += differenzaQuantita * pu;
                                $("#totcarrello").text(totcarrello.toFixed(2) + ' €');
                            },
                            error: function (xhr, status, error) {
                                console.error("Errore nell'aggiornamento della quantità");
                            }
                        });
                    }


                } else if (nuovoValore === 0) {

                    $.ajax({
                        url: "Dispatcher",
                        type: "POST",
                        data: {
                            controllerAction: "UserManagement.aggiungicarrello",
                            idprod: $("#idprod" + i).val(),
                            quantita: "0"
                        },
                        success: function (response) {
                            console.log("Prodotto rimosso con successo");
                            k--;

                            elemento.remove();
                            console.log(k);
                            if(k === 0){

                                location.reload();

                            }

                            totcarrello -= prezzotot;
                            $("#totcarrello").text(totcarrello.toFixed(2) + ' €');
                        },
                        error: function (xhr, status, error) {
                            console.error("Errore nella rimozione del prodotto");
                        }
                    });
                }


                $(this).data('quantita-vecchia', nuovoValore);
            });


        }
    });

    function incrementQuantity(index) {
        const quantityInput = document.getElementById("qta" + index);
        let currentValue = parseInt(quantityInput.value);
        quantityInput.value = currentValue + 1;
        quantityInput.dispatchEvent(new Event('input'));
    }

    function decrementQuantity(index) {
        const quantityInput = document.getElementById("qta" + index);
        let currentValue = parseInt(quantityInput.value);

        if (currentValue > 1) {
            quantityInput.value = currentValue - 1;
            quantityInput.dispatchEvent(new Event('input'));
        }
    }

</script>


</body>
</html>

