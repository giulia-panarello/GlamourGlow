<%--
  Created by IntelliJ IDEA.
  User: giuliapanarello
  Date: 28/10/24
  Time: 17:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.glamourglow.glamourglow.model.mo.Coupon" %>
<%

    List<Coupon> couponList = (List<Coupon>) request.getAttribute("coupon");
    String messaggio = (String) request.getAttribute("applicationMessage");
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestione Coupon - GlamourGlow </title>
    <link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/images/coupon.png">
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


        .coupon-container {
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

        .coupon-items {
            flex: 1;
            overflow-y: auto;
            margin-right: 20px;
            max-height: 50vh;
            overflow-x: hidden;
        }

        .add-coupon {
            width: 350px;
            background-color: #f9f9f9;
            border: 3px solid #6A5ACD;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            position: absolute;
            top: 30%;
            z-index: 100;
            right: 5%;
            top: 50%;
        }

        .add-coupon h1 {
            font-size: 24px;
            color: #333;
            width: 100%;
            text-align: right;
            margin-right: 50px;
        }

        .add-coupon button {
            width: 100%;
            background-color: #6A5ACD;
            color: white;
            padding: 8px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            display: block;
            position: absolute;
            margin-top: 15%;
            right: 0;
        }

        h3 {
            position: relative;
            right: -30%;
            top: -5%;
            color: #6A5ACD;
        }

        .coupon-management-item {
            padding: 15px;
            border: 1px solid #eaeaea;
            border-radius: 5px;
            background-color: #E6E6FA;
            margin-bottom: 15px;
            width: auto;
            overflow-x: hidden;
        }

        .coupon-header {
            font-size: 24px;
            color: #6A5ACD;
            text-align: center;
            position: relative;
            width: 100%;
            overflow-x: hidden;
        }

        .coupon-header::after {
            content: '';
            display: block;
            width: 100%;
            height: 2px;
            background-color: #6A5ACD;
            margin-top: 10px;
        }

        .coupon-icon {
            width: 40px;
            height: 40px;
            margin-right: 10px;
            vertical-align: middle;
        }


        input[type="text"] {
            width: 50%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #6A5ACD;
            border-radius: 5px;
            font-size: 16px;
            box-sizing: border-box;
            background-color: #f9f9f9;

        }


        button[type="submit"] {
            width: 80%;
            padding: 10px;
            background-color: #6A5ACD;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            box-sizing: border-box;
            margin-top: 15px;
            position: relative;
            transform: translateX(50%);
            left: -31%;

        }

        button[type="submit"]:hover {
            background-color: #4b0082;
        }


        .field-container label {
            font-weight: bold;
        }

        .messaggio{
            z-index: 2000;
            width: 350px;
            background-color: #F0E68C;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            position: absolute;
            top: 30%;
            right: 5%;
            text-align: center;
            font-weight: bold;
            color: #483D8B;
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


<div class="coupon-container">
    <h2 class="coupon-header">
        Gestione Coupon
        <img src="<%= request.getContextPath() %>/images/coupon.png" alt="Icona Coupon" class="coupon-icon">
    </h2>

    <div class="coupon-items">


        <% for(int i = 0; i < couponList.size(); i++) { %>
        <div class="coupon-management-item">
            <form method="post" action="Dispatcher" class="coupon-form">
                <div class="field-container">
                    <label for="sconto_<%= i %>">Sconto:</label>
                    <%=  couponList.get(i).getSconto() %> %
                    <input type="hidden" name="sconto" min="5" max="80" id="sconto_<%= i %>" value="<%=  couponList.get(i).getSconto() %>" required />
                </div>

                <div class="field-container">
                    <label for="codice_<%= i %>">Codice:</label>
                    <input type="hidden" name="vecchiocodice" value="<%= couponList.get(i).getCodice() %>" />
                    <input type="text" name="codice" id="codice_<%= i %>" value="<%= couponList.get(i).getCodice() %>" required />
                </div>


                <input type="hidden" name="controllerAction" value="AdminManagement.modificacoupon" />


                <button type="submit">Modifica</button>
            </form>
        </div>
        <% } %>
    </div>



</div>


<% if(messaggio != null) {
    %> <div class="messaggio"> <%=messaggio%> </div><%
} %>
<h3>Aggiungi Nuovo Coupon</h3>
<form method="post" action="Dispatcher?controllerAction=AdminManagement.addCoupon" class="add-coupon">
    <input type="text" name="codice" placeholder="Codice coupon" required/>
    <input type="text" name="sconto" placeholder="Sconto (%)" required/>
    <button type="submit">Aggiungi</button>
</form>
</body>
</html>
