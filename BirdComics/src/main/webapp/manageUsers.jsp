<%@page import="java.util.*"%>
<%@page import="com.birdcomics.GestioneProfili.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Gestione Magazzini</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" href="css/changes.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>

<jsp:include page="./fragments/header.jsp" />
<body>
	<div class="text-center"
		style="color: green; font-size: 24px; font-weight: bold; margin-top: 15px; margin-bottom: 15px;">
		Gestione Magazzini</div>

	<form class="search-form" action="GestioneMagazzino" method="get">
		<input type="text" name="search" placeholder="Cerca un magazzino...">
		<input type="submit" value="Cerca">
	</form>

	<h3 style="text-align: center;"><%= request.getAttribute("message") %></h3>

	<div class="container-fluid">
		<div class="table-responsive">
			<table class="table table-hover table-sm">
				<thead
					style="background-color: #4CAF50; color: white; font-size: 18px;">
					<tr>
						<th>Email</th>
						<th>Nome</th>
						<th>Numero di telefono</th>
						<th>Indirizzo</th>
						<th>Azioni</th>
						<th></th>
					</tr>
				</thead>
				<tbody style="background-color: white; font-size: 16px;">
					<% 
        ArrayList<UserBean> listaGestoriMagazzino = (ArrayList<UserBean>) request.getAttribute("gestoriMagazzino");
        if (listaGestoriMagazzino != null) {
            for (UserBean user : listaGestoriMagazzino) { 
    %>
					<tr>
						<td><%= user.getEmail() %></td>
						<td><%= user.getNome() %></td>
						<td><%= user.getNumeroTelefono() %></td>
						<td><%= user.getIndirizzo() %></td>
						<td style="text-align: center">
							<form action="CancellaMagazzino" method="post">
								<input type="hidden" name="warehouseId"
									value="<%= user.getNome() %>">
								<button type="submit" class="btn btn-danger">Cancella</button>
							</form>
						</td>
					</tr>
					<% 
            }
        } else { 
    %>
					<tr>
						<td colspan="6" style="text-align: center;">Nessun magazzino
							disponibile.</td>
					</tr>
					<% } %>
				</tbody>
			</table>
		</div>
	</div>

	<%@ include file="/fragments/footer.html"%>
</body>
</html>
