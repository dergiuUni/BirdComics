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
					<c:when test="${usertype != null and usertype.contains('GestoreGenerale')}">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Gestione Magazzini<span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="">AggiungiMagazzino</a></li>
								<li><a href="">listaMagazzino</a></li>
								<li><a href="">AggiungiDirettoreMagazzino</a></li>
								<li><a href="">ListaDirettoreMagazzino</a></li>
							</ul>
						</li>
						<li><a href="UserProfileServlet">Profilo</a></li>		
						<li><a href="./LogoutSrv">Logout</a></li>
					</c:when>
					
					<c:when test="${usertype != null and usertype.contains('GestoreMagazzino')}">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Gestione Magazzino<span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="">AggiungiScaffale</a></li>
								<li><a href="">ListaScaffale</a></li>
								<li><a href="">AggiungiHr</a></li>
								<li><a href="">ListaHr</a></li>
							</ul>
						</li>
						<li><a href="UserProfileServlet">Profilo</a></li>		
						<li><a href="./LogoutSrv">Logout</a></li>
						
					</c:when>
					
					<c:when test="${usertype != null and usertype.contains('RisorseUmane')}">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Gestione Dipendenti<span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="">Aggiungi Dipendente</a></li>
								<li><a href="">Lista Dipendente</a></li>
							</ul>
						</li>
						<li><a href="UserProfileServlet">Profilo</a></li>		
						<li><a href="./LogoutSrv">Logout</a></li>
					</c:when>
					
					<c:when test="${usertype != null and usertype.contains('GestoreCatalogo')}">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Gestione Catalogo <span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="./GestioneCatalogo">Visualizza Catalogo</a></li>
								<li><a href="addProduct.jsp">Aggiungi Fumetto</a></li>
								<li><a href="">Rimuovi Fumetto</a></li>
								<li><a href="">Aggiorna Fumetto</a></li>
								
							</ul>
						</li>
						<li><a href="UserProfileServlet">Profilo</a></li>		
						<li><a href="./LogoutSrv">Logout</a></li>
					</c:when>
					
					<c:when test="${usertype != null and usertype.contains('Magazziniere')}">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Magazziniere <span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="">ModificaPosizioneScaffale</a></li>
								<li><a href="">Mansioni</a></li>
								<li><a href="">LiberaScaffale</a></li>
							</ul>
						</li>
						<li><a href="UserProfileServlet">Profilo</a></li>		
						<li><a href="./LogoutSrv">Logout</a></li>
					</c:when>
					
					<c:when test="${usertype != null and usertype.contains('Spedizioniere')}">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Spedizioniere <span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="GestioneOrdiniNonSpediti">AggiungiTracking</a></li>
							</ul>
						</li>
						<li><a href="UserProfileServlet">Profilo</a></li>		
						<li><a href="./LogoutSrv">Logout</a></li>
					</c:when>
					
					<c:when test="${usertype != null and usertype.contains('Assistenza')}">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Assistenza <span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="adminStock">Cerca per id ordine</a></li>
							</ul>
						</li>
						<li><a href="UserProfileServlet">Profilo</a></li>		
						<li><a href="./LogoutSrv">Logout</a></li>
					</c:when>
					
					<c:when test="${usertype != null and usertype.contains('Finanza')}">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Finanza <span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="adminStock">test</a></li>
							</ul></li>
							<li><a href="UserProfileServlet">Profilo</a></li>		
							<li><a href="./LogoutSrv">Logout</a></li>
					</c:when>
					
					<c:when test="${usertype != null and usertype.contains('Cliente')}">
						<li><a href="index.jsp">Home</a></li>
							<li><a href="CartDetailsServlet">Carrello</a></li>
							<li><a href="OrderDetailsServlet">Ordini</a></li>
							<li><a href="UserProfileServlet">Profilo</a></li>			
							<li><a href="./LogoutSrv">Logout</a></li>
				</c:when>
				
							<c:when test="${usertype != null}">
							<li><a href="testServlet">TEST</a></li>			
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
							<li><a href="CartDetailsServlet">Carrello</a></li>
								<li><a href="login.jsp">Login</a></li>
								<li><a href="register.jsp">Registrati</a></li>
								
								<form class="form-inline" action="ProductListServlet" method="GET">
   							 <input type="text" class="search-data form-control" name="search"
       							 placeholder="Cerca fumetti..." required>
    						<button type="submit" class="fas fa-search" aria-label="search"></button>
							</form>
								</ul>

							
							</c:otherwise>
							
							</c:choose>	
		</div>
	</nav>
</body>
</html>