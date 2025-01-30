<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.birdcomics.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
				<c:choose>
					<c:when test="${'admin' eq sessionScope.usertype}">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Gestione libri <span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="adminStock">Elenco libri</a></li>
								<li><a href="addProduct.jsp">Aggiungi libro</a></li>
								<li><a href="removeProduct.jsp">Rimuovi libro</a></li>
								<li><a href="updateProductById.jsp">Aggiorna libro</a></li>
							</ul></li>
						<li><a href="ShipmentServlet">Gestione Ordini</a></li>
						<li><a href="./LogoutSrv">Logout</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="index.jsp">Home</a></li>
						<li class="dropdown" id="generi-dropdown"><a
							class="dropdown-toggle" data-toggle="dropdown" href="#">Generi
								<span class="caret"></span>
						</a>
							<ul class="dropdown-menu">
								<li><a href="ProductListServlet?type=avventura" class="genre-link">Avventura</a></li>
								<li><a href="ProductListServlet?type=azione" class="genre-link">Azione</a></li>
								<li><a href="ProductListServlet?type=horror" class="genre-link">Horror</a></li>
								<li><a href="ProductListServlet?type=thriller" class="genre-link">Thriller</a></li>
								<li><a href="ProductListServlet?type=fantasy" class="genre-link">Fantasy</a></li>
								<li><a href="ProductListServlet?type=drammatico" class="genre-link">Drammatico</a></li>
								<li><a href="ProductListServlet?type=fantascienza" class="genre-link">Fantascienza</a></li>

							</ul></li>
							
						<c:choose>
							<c:when test="${not empty sessionScope.usertype}">
								<li><a href="CartDetailsServlet">Carrello</a></li>
								<li><a href="OrderDetailsServlet">Ordini</a></li>
								<li><a href="UserProfileServlet">Profilo</a></li>
								<li><a href="./LogoutSrv">Logout</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="CartDetailsServlet">Carrello</a></li>
								<li><a href="login.jsp">Login</a></li>
								<li><a href="register.jsp">Registrati</a></li>
							</c:otherwise>
						</c:choose>
						<div class="search-icon">
							<span class="fas fa-search" aria-hidden="true"></span>
						</div>
						<div class="cancel-icon">
							<span class="fas fa-times" aria-hidden="true"></span>
						</div>
						<form class="form-inline">
							<input type="text" class="search-data form-control" name="search"
								placeholder="Cerca libri..." required>
							<button type="submit" class="fas fa-search" aria-label="search"></button>
						</form>

					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</nav>
</body>
</html>