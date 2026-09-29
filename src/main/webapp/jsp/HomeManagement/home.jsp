<%@ page import="com.glamourglow.glamourglow.model.mo.Prodotto" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: giuliapanarello
  Date: 10/10/24
  Time: 11:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.glamourglow.glamourglow.model.mo.Prodotto" %>
<%@ page import="java.util.List" %>
<%@ page import="com.glamourglow.glamourglow.model.mo.Marchio" %>
<%@ page import="com.glamourglow.glamourglow.model.mo.Categoria" %>
<%@ page import="com.glamourglow.glamourglow.model.mo.Utente" %>
<%
    String categoria = (String) request.getAttribute("Categoria");
    if(categoria==null){categoria="";}
    List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("Prodotti");
    List<Marchio> marchi = (List<Marchio>) request.getAttribute("marchi");
    Boolean promo = (Boolean) request.getAttribute("promo");
    String marchio = (String) request.getAttribute("Marchio");
    if(marchio==null){marchio="";}
    String messaggio = (String) request.getAttribute("applicationMessage");
    Boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    String message = (String) request.getAttribute("Message");

%>
<html>
<head>
    <title>GlamourGlow</title>
    <link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/images/logo.png">
    <style>

        .welcome-message {
            font-weight: bold;
            color: #6A5ACD;
            font-size: 20px;
        }
        .container-marchio {
            display: none;
            position: absolute;
            background-color: white;
            z-index: 100;
            padding: 10px;
            border-radius: 5px;
        }

        .marchio:hover .container-marchio {
            display: block;
        }

        body {
            margin: 0;
            padding: 0;
            height: 100vh;
            overflow: hidden;
            display: flex;
            flex-direction: column;
            align-items: center;
            background-color: pink;
            font-family: Arial, sans-serif;
        }
        .search-container {
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
        .search-bar {
            display: flex;
            align-items: center;
            width: 100%;
            max-width: 500px;
            margin-right: 20px;
            position: sticky;

        }
        .search-bar input[type="text"] {
            flex-grow: 1;
            padding: 10px 15px;
            border-radius: 25px 0 0 25px;
            border: 1.5px solid #6A5ACD;
            box-sizing: border-box;
            font-size: 16px;
            max-width: 400px;
        }
        .search-bar button {
            background-color: white;
            border: none;
            cursor: pointer;
            border-radius: 0 25px 25px 0;
            padding: 10px;
            border: 1.5px solid #6A5ACD;
        }
        .search-bar button img {
            width: 35px;
            height: 35px;
        }
        .icon-container {
            display: flex;
            align-items: center;
            margin-left: 30px;
            margin-right: 20px;
        }
        .icon-container img {
            width: 35px;
            height: 35px;
            margin-left: 15px;
            cursor: pointer;
        }
        .category-bar {
            display: flex;
            justify-content: center;
            margin-top: 10px;
            background-color: #E6E6FA;
            padding: 10px;
            border-radius: 10px;
            width: 100%;
            z-index: 100;
        }
        .category-bar a {
            margin: 0 15px;
            text-decoration: none;
            color: #6A5ACD;
            font-size: 18px;
        }
        .category-bar a:hover {
            text-decoration: underline;
        }


       .promo-products-container {
           margin-top: 5px;
           width: 100%;
           max-width: 800px;
           position: relative;
           overflow-x: hidden;
           height: 70vh;

       }

        .promo-products-container h2 {
            color: darkred;
        }

        .products-container h3 {
            margin: 0; /
            font-size: 18px;
        }
        .products-container p {
            margin: 2px 0;
        }


        .messaggio {
        position: absolute;
        margin-top: 6%;
        width: 20%;
        right: 1%;
        background-color: white;
        padding: 12px;
        border-radius: 7px;
        text-align: center;
        border: 1.5px solid #6A5ACD;
        color: #6A5ACD;
    }

        .wallet:hover .saldo {
            display: block;
            position: absolute;
            text-align: center;
            background-color: white;
            margin-left: auto;
            padding: 9px;
            width: 10%;
            right: 4%;
            border-radius: 7px;
            text-align: center;
            border: 1.5px solid #6A5ACD;
            color: #6A5ACD;
        }



        .saldo {
            display: none;
            font-weight: bold;
            font-size: 20px;
        }
        .mess{
            font-weight: normal;
            font-size: 14px
        }
        .cont-admin{
            display: none;
            position: absolute;
            background-color: white;
            padding: 7px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            border: 1.5px solid #6A5ACD;
            z-index: 2000;
        }
        .admin:hover .cont-admin{
            display: block;
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

        .message-box {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 400px;
            height: auto;
            max-width: 90%;
            background-color: white;
            border: 3px solid #6A5ACD;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            padding: 20px;
            text-align: center;
            z-index: 1000;
            font-size: 23px;
            color: #6A5ACD;
            font-weight: bold;
        }



        .close-button {
            position: absolute;
            top: 5px;
            right: 10px;
            color: #6A5ACD;
            font-weight: bold;
            font-size: 18px;
            cursor: pointer;
        }



        .products-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            overflow-x: hidden;
            gap: 15px;
            width: 98%;
            height: auto;
            object-fit: contain;
            padding: 10px;
            grid-gap: 15px;
            transition: transform 0.3s ease;
            will-change: transform;
            overflow-y: auto;
            scrollbar-width: thin;
        }


        .products-container::-webkit-scrollbar {
            width: 8px;
        }

        .products-container::-webkit-scrollbar-thumb {
            background: #6A5ACD;
            border-radius: 4px;
        }

        .product {
            background-color: #E6E6FA;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        .products-container img {
            display: block;
            max-width: 100%;
            max-height: 100%;
            margin: auto;
            object-fit: contain;
        }



    </style>
</head>
<body>

<div class="search-container">
    <div class="logo-container">
        <img src="<%= request.getContextPath() %>/images/logo.png" alt="Icona">
        <% if (loggedUser != null && loggedUser.getRuolo()){
            %> <div class="admin"><h1>GLAMOURGLOW</h1>
        <div class="cont-admin">
        <a href="Dispatcher?controllerAction=AdminManagement.gestioneutente">Gestione Utenti</a>
        <a href="Dispatcher?controllerAction=AdminManagement.gestioneordini">Gestione Ordini</a>
            <a href="Dispatcher?controllerAction=AdminManagement.gestionecoupon">Gestione Coupon</a>
            <a href="Dispatcher?controllerAction=AdminManagement.gestioneprodotti">Gestione Prodotti</a>
        </div>
    </div><%
        }
        else{
           %><h1>GLAMOURGLOW</h1><%
        }
    %>
    </div>
    <div class="search-bar">
        <form action="Dispatcher" method="post" style="display: flex; width: 100%;">
            <!-- <input type="text" name="Nomeprodotto" placeholder="Cerca"> -->
            <%
                System.out.println("Categ:"+categoria+"\nMarc:"+marchio+"\nPromo:"+promo);
                if(categoria.equals("") && marchio.equals("") && (promo == null || !promo)){
            %>  <input type="text" name="Nomeprodotto" placeholder="Cerca"> <%
            }
            else{

                if((!categoria.equals("")))
                {
                 %>  <input type="text" name="Nomeprodotto" placeholder="Cerca in <%=categoria%>"> <%
                }

            if((!marchio.equals("")))
            {
        %>  <input type="text" name="Nomeprodotto" placeholder="Cerca in <%=marchio%>"> <%
                }

            if((promo!=null && promo))
            {
        %>  <input type="text" name="Nomeprodotto" placeholder="Cerca in promo"> <%
                }

            }
        %>

            <input type="hidden" name="Promo" value="<%= promo %>">
            <input type="hidden" name="Categoria" value="<%= categoria %>">
            <input type="hidden" name="Marchioprodotto" value="<%= marchio %>">
            <input type="hidden" name="controllerAction" value="HomeManagement.cerca">
            <button type="submit">
                <img src="<%= request.getContextPath() %>/images/search.jpg" alt="Cerca">
            </button>
        </form>
    </div>

    <div class="icon-container">
        <%
            if (loggedOn) {
        %>
        <span class="welcome-message">Ciao, <%= loggedUser.getNome() %>!</span>
        <a href="Dispatcher?controllerAction=HomeManagement.logout">
            <img src="<%= request.getContextPath() %>/images/logout.png" alt="Logout">
        </a>
        <div class="wallet">
        <a href="Dispatcher?controllerAction=UserManagement.ricaricasaldo" >
            <img src="<%= request.getContextPath() %>/images/wallet.png" alt="Portafoglio" title="Portafoglio">
        </a>

            <div class="saldo">
               <div class="mess">Saldo disponibile:</div> <%= String.format("%.2f", loggedUser.getWallet()) %>€
            </div>
        </div>


            <a href="Dispatcher?controllerAction=UserManagement.viewcarrello">
                <img src="<%= request.getContextPath() %>/images/carrello.png" alt="Carrello">
            </a>
            <a href="Dispatcher?controllerAction=UserManagement.viewordini">
                <img src="<%= request.getContextPath() %>/images/sped.png" alt="Spedizione" title="Spedizione">
            </a>


        <%
        } else {
        %>
        <a href="Dispatcher?controllerAction=HomeManagement.viewlogin">
            <img src="<%= request.getContextPath() %>/images/login.png" alt="Login">
        </a>


        <a href="Dispatcher?controllerAction=UserManagement.viewcarrello">
            <img src="<%= request.getContextPath() %>/images/carrello.png" alt="Carrello">
        </a>
        <%
            }
        %>




        <% if (messaggio != null) { %>

            <a class="messaggio" href="Dispatcher?controllerAction=HomeManagement.viewlogin"> <%= messaggio %> </a>

        <% } %>

    </div>

</div>


<div class="category-bar">

    <a href="Dispatcher?controllerAction=HomeManagement.cerca&Categoria=Cura del corpo">Cura del corpo
        <img src="<%= request.getContextPath() %>/images/corpo.png" alt="Capelli" style="width: 20px; height: 20px; margin-left: 5px;">
    </a>
    <a href="Dispatcher?controllerAction=HomeManagement.cerca&Categoria=Capelli">Capelli
        <img src="<%= request.getContextPath() %>/images/capelli.png" alt="Capelli" style="width: 20px; height: 20px; margin-left: 5px;">
    </a>
    <a href="Dispatcher?controllerAction=HomeManagement.cerca&Categoria=Profumi">Profumi
        <img src="<%= request.getContextPath() %>/images/profumi.png" alt="Profumi" style="width: 20px; height: 20px; margin-left: 5px;">
    </a>
    <a href="Dispatcher?controllerAction=HomeManagement.cerca&Categoria=Makeup">Makeup
        <img src="<%= request.getContextPath() %>/images/makeup.png" alt="Trucchi" style="width: 20px; height: 20px; margin-left: 5px;">
    </a>
    <a href="Dispatcher?controllerAction=HomeManagement.cerca&Categoria=Trattamenti anti-invecchiamento">Anti-invecchiamento
        <img src="<%= request.getContextPath() %>/images/vecchia.png" alt="Anti" style="width: 20px; height: 20px; margin-left: 5px;">
    </a>
    <a href="Dispatcher?controllerAction=HomeManagement.cerca&Categoria=Cura del viso">Cura del viso
        <img src="<%= request.getContextPath() %>/images/viso.png" alt="Viso" style="width: 20px; height: 20px; margin-left: 5px;">
    </a>
    <a href="Dispatcher?controllerAction=HomeManagement.cerca&Categoria=">Tutti
        <img src="<%= request.getContextPath() %>/images/tutto.png" alt="Tutti" style="width: 20px; height: 20px; margin-left: 5px;">
    </a>
    <a href="Dispatcher?controllerAction=HomeManagement.viewhome&Categoria=">Promo
        <img src="<%= request.getContextPath() %>/images/pro.png" alt="Promo" style="width: 20px; height: 20px; margin-left: 5px;">
    </a>
    <div class="marchio">
        <a href="#" style="color: #6A5ACD; font-size: 18px; text-decoration: none;">Marchi
            <img src="<%= request.getContextPath() %>/images/marchi.png" alt="Marchi" style="width: 20px; height: 20px; margin-left: 5px;">
        </a>
        <div class="container-marchio">
            <% for(int i=0; i<marchi.size(); i++){ %>
            <a class="righe-marchi"
               href="Dispatcher?controllerAction=HomeManagement.cerca&Marchioprodotto=<%= marchi.get(i).getNomeMarchio() %>"
               style="color: #6A5ACD; display: block; margin-bottom: 5px; font-size: 15px">
                <%= marchi.get(i).getNomeMarchio() %>
            </a>
            <% } %>
        </div>
    </div>
</div>


<% if (message != null) { %>
<div class="message-box" id="messageBox">
    <span class="close-button" onclick="closeMessageBox()">×</span>
    <p><%= message %> <img src="<%= request.getContextPath() %>/images/conferma.png" alt="Conferma" style="width: 40px; height: 40px; vertical-align: middle; margin-right: 10px;"></p>
</div>
<% } %>



<div class="promo-products-container">
    <% if(promo != null && promo){
    %><h2>Prodotti in Promozione</h2> <%
    } else{
        if(categoria.isEmpty()){
            if(marchio.isEmpty()) {
            %>
                 <h2>Tutti</h2>
            <%
                }
            else{
              %>  <h2><%= marchio%></h2> <%
                }
        }
        else{
            %>
              <h2><%= categoria %></h2>
    <%
        }

    }
    %>

    <div class="products-scroller">

        <div class="products-container">
            <% if (prodotti != null && !prodotti.isEmpty()) {
                for (Prodotto prodotto : prodotti) {%>
            <div class="product" style="display: flex; flex-direction: column; align-items: center;">
                <div style="width:96%; height: 78%;"> <img src="images/prodotti/<%= prodotto.getImmagine() %>" alt="<%= prodotto.getNomeProdotto() %>"
                            style="width: 250px; height: 250px; object-fit: contain; margin-top: -7%;">
                </div>


                <h3 style="text-align: center; margin-top: 10px; font-size: 15px;">
                    <a href="Dispatcher?controllerAction=ProductManagement.viewprodotto&id=<%= prodotto.getIdProdotto() %>">
                        <%= prodotto.getNomeProdotto() %>
                    </a>
                </h3>


                    <%
                        if (prodotto.getInPromo()) {


                    %>

                <div style="display:flex; align-items:center; margin-top: 8%; width: 69%; ">
                <div class="original-price" style="text-decoration: line-through; text-align: center; font-size: 20px; margin-right: 15px; white-space: nowrap;"><%= String.format("%.2f", prodotto.getPrezzo()) %> € </div>



                <div style="color: #6A5ACD; font-weight: bold; font-size: 30px"><%= String.format("%.2f", prodotto.getPrezzoSconto()) %>€</div>
                </div>

                    <%
                        }
                        else{
                        %> <p class="price" style="color: #6A5ACD; font-weight: bold; text-align: center; font-size: 30px; margin-top: 8%">  <%= String.format("%.2f", prodotto.getPrezzo()) %>€ </p> <%
                        }
                    %>


            </div>
            <% } } else { %>
            <p>Nessun prodotto disponibile.</p>
            <% } %>
        </div>
    </div>
</div>

<script>
    function closeMessageBox() {
        document.getElementById("messageBox").style.display = "none";
    }
</script>

</body>
</html>
