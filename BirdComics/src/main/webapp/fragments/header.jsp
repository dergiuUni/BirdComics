<%@page import="com.birdcomics.Bean.RuoloBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.birdcomics.*"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" href="css/changes.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
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
                    if (userTypes.contains(RuoloBean.GestoreGenerale.toString())) {
                    %>
				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#">Gestione Magazzini<span
						class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="">Aggiungi Magazzino</a></li>
						<li><a href="./ListaMagazziniServlet">Lista Magazzini</a></li>
						<li><a href="register.jsp">Aggiungi Direttore Magazzino</a></li>
						<li><a href="./GestioneUtentiServlet">Lista Direttori
								Magazzino</a></li>
					</ul></li>
				<%  
                    }
                    if (userTypes.contains(RuoloBean.GestoreMagazzino.toString())) {
                    %>
				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#">Gestione Magazzino<span
						class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="">Aggiungi Scaffale</a></li>
						<li><a href="">Lista Scaffale</a></li>
						<li><a href="register.jsp">Aggiungi HR</a></li>
						<li><a href="./GestioneUtentiServlet">Lista HR</a></li>
					</ul></li>
				<%  
                    }
                    if (userTypes.contains(RuoloBean.RisorseUmane.toString())) {
                    %>
				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#">Gestione Dipendenti<span
						class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="register.jsp">Aggiungi Dipendente</a></li>
						<li><a href="./GestioneUtentiServlet">Lista Dipendenti</a></li>
					</ul></li>
				<%  
                    }
                    if (userTypes.contains(RuoloBean.GestoreCatalogo.toString())) {
                    %>
				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#">Gestione Catalogo <span
						class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="./ProductListServlet">Visualizza Catalogo</a></li>
						<li><a href="./AddProductSrv">Aggiungi Fumetto</a></li>
					</ul></li>
				<%  
                    }
                    if (userTypes.contains(RuoloBean.Cliente.toString())) {
                    %>
				<li><a href="index.jsp">Home</a></li>
				<li class="dropdown" id="generi-dropdown"><a
					class="dropdown-toggle" data-toggle="dropdown" href="#">Generi
						<span class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<li><a href="ProductListServlet?type=avventura"
							class="genre-link">Avventura</a></li>
						<li><a href="ProductListServlet?type=azione"
							class="genre-link">Azione</a></li>
						<li><a href="ProductListServlet?type=horror"
							class="genre-link">Horror</a></li>
						<li><a href="ProductListServlet?type=thriller"
							class="genre-link">Thriller</a></li>
						<li><a href="ProductListServlet?type=fantasy"
							class="genre-link">Fantasy</a></li>
						<li><a href="ProductListServlet?type=drammatico"
							class="genre-link">Drammatico</a></li>
						<li><a href="ProductListServlet?type=fantascienza"
							class="genre-link">Fantascienza</a></li>
					</ul></li>
				<li><a href="CartDetailsServlet">Carrello</a></li>
				<li><a href="OrderDetailsServlet">Ordini</a></li>
				<%  
                    }
                    %>

				<% } else { %>
				<li><a href="index.jsp">Home</a></li>
				<li class="dropdown" id="generi-dropdown"><a
					class="dropdown-toggle" data-toggle="dropdown" href="#">Generi
						<span class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<li><a href="ProductListServlet?type=avventura"
							class="genre-link">Avventura</a></li>
						<li><a href="ProductListServlet?type=azione"
							class="genre-link">Azione</a></li>
						<li><a href="ProductListServlet?type=horror"
							class="genre-link">Horror</a></li>
						<li><a href="ProductListServlet?type=thriller"
							class="genre-link">Thriller</a></li>
						<li><a href="ProductListServlet?type=fantasy"
							class="genre-link">Fantasy</a></li>
						<li><a href="ProductListServlet?type=drammatico"
							class="genre-link">Drammatico</a></li>
						<li><a href="ProductListServlet?type=fantascienza"
							class="genre-link">Fantascienza</a></li>
					</ul></li>
				<li><a href="CartDetailsServlet">Carrello</a></li>
				<li><a href="login.jsp">Login</a></li>
				<li><a href="register.jsp">Registrati</a></li>
				<% } %>

				<% if (userTypes != null && !userTypes.isEmpty()) { %>
				<li><a href="UserProfileServlet">Profilo</a></li>
				<li><a href="./LogoutSrv">Logout</a></li>
				<% } %>
			</ul>

			<form class="navbar-form navbar-right" action="ProductListServlet"
				method="GET">
				<div class="input-group">
					<input type="text" class="form-control" name="search"
						placeholder="Cerca fumetti..." required> <span
						class="input-group-btn">
						<button type="submit" class="btn btn-outline-secondary">
							<i class="fas fa-search"></i>
						</button>
					</span>
				</div>
			</form>


		</div>
	</nav>
</body>
</html>
