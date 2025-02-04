<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.birdcomics.*"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/changes.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<body>
    <nav>
        <div class="menu-icon">
            <span class="fas fa-bars" aria-hidden="true"></span>
        </div>
        <div class="logo">BirdComics</div>
        <div class="nav-items">
            <ul class="nav navbar-nav">
                
                <% List<String> userTypes = (List<String>) session.getAttribute("usertype"); %>
                
                <% if (userTypes != null && !userTypes.isEmpty()) { %>

                    <%-- User-specific Menus --%>
                    <%
                    if (userTypes.contains("GestoreGenerale")) {
                    %>
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Gestione Magazzini<span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="">AggiungiMagazzino</a></li>
                                <li><a href="./ListaMagazziniServlet">Lista Magazzini</a></li>
                                <li><a href="">AggiungiDirettoreMagazzino</a></li>
                                <li><a href="./GestoriMagazziniServlet">Lista Direttori Magazzino</a></li>
                            </ul>
                        </li>
                    <%  
                    }
                    if (userTypes.contains("GestoreMagazzino")) {
                    %>
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Gestione Magazzino<span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="">AggiungiScaffale</a></li>
                                <li><a href="">ListaScaffale</a></li>
                                <li><a href="">AggiungiHr</a></li>
                                <li><a href="">ListaHr</a></li>
                            </ul>
                        </li>
                    <%  
                    }
                    if (userTypes.contains("RisorseUmane")) {
                    %>
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Gestione Dipendenti<span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="">Aggiungi Dipendente</a></li>
                                <li><a href="">Lista Dipendente</a></li>
                            </ul>
                        </li>
                    <%  
                    }
                    if (userTypes.contains("GestoreCatalogo")) {
                    %>
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Gestione Catalogo <span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="./GestioneCatalogo">Visualizza Catalogo</a></li>
                                <li><a href="./AddProductSrv">Aggiungi Fumetto</a></li>
                            </ul>
                        </li>
                    <%  
                    }
                    if (userTypes.contains("Magazziniere")) {
                    %>
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Magazziniere <span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="">ModificaPosizioneScaffale</a></li>
                                <li><a href="">Mansioni</a></li>
                                <li><a href="">LiberaScaffale</a></li>
                            </ul>
                        </li>
                    <%  
                    }
                    if (userTypes.contains("Spedizioniere")) {
                    %>
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Spedizioniere <span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="GestioneOrdiniNonSpediti">AggiungiTracking</a></li>
                            </ul>
                        </li>
                    <%  
                    }
                    if (userTypes.contains("Assistenza")) {
                    %>
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Assistenza <span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="adminStock">Cerca per id ordine</a></li>
                            </ul>
                        </li>
                    <%  
                    }
                    if (userTypes.contains("Finanza")) {
                    %>
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Finanza <span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="adminStock">test</a></li>
                            </ul>
                        </li>
                    <%  
                    }
                    if (userTypes.contains("Cliente")) {
                    %>
                        <li><a href="index.jsp">Home</a></li>
                        <li><a href="CartDetailsServlet">Carrello</a></li>
                        <li><a href="OrderDetailsServlet">Ordini</a></li>
                    <%  
                    }
                    %>
                
                <% } else { %>

                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="CartDetailsServlet">Carrello</a></li>
                    <li><a href="login.jsp">Login</a></li>
                    <li><a href="register.jsp">Registrati</a></li>
                    <form class="form-inline" action="ProductListServlet" method="GET">
                        <input type="text" class="search-data form-control" name="search" placeholder="Cerca fumetti..." required>
                        <button type="submit" class="fas fa-search" aria-label="search"></button>
                    </form>
                
                <% } %>

                <%-- Common Links at the end (only show when logged in) --%>
                <% if (userTypes != null && !userTypes.isEmpty()) { %>
                    <li><a href="UserProfileServlet">Profilo</a></li>
                    <li><a href="./LogoutSrv">Logout</a></li>
                <% } %>

            </ul>
        </div>
    </nav>
</body>
</html>
