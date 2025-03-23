<%@page import="com.birdcomics.Model.Bean.UserBean"%>
<%@page import="java.util.*"%>
<%@page import="com.birdcomics.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Gestione Utenti</title>
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
		Gestione Utenti</div>

	<form class="search-form" action="Gestione Utenti" method="get">
		<input type="text" name="search" placeholder="Cerca utente...">
		<input type="submit" value="Cerca">
	</form>

	<h3 style="text-align: center;"><%=request.getAttribute("message") != null ? request.getAttribute("message") : ""%></h3>


	<div class="container-fluid">
		<div class="table-responsive">
			<table class="table table-hover table-sm">
				<thead
					style="background-color: #4CAF50; color: white; font-size: 18px;">
					<tr>
						<th>Email</th>
						<th>Nome</th>
						<th>Cognome</th>
						<th>Numero di telefono</th>
						<th>Citt�</th>
						<th>Via</th>
						<th>NumeroCivico</th>
						<th>CAP</th>
						<th colspan="2" style="text-align: center;">Azioni</th>
				</thead>
				<tbody style="background-color: white; font-size: 16px;">
					<%
					ArrayList<UserBean> utenti = (ArrayList<UserBean>) request.getAttribute("utenti");
					if (utenti != null) {
						for (UserBean user : utenti) {
					%>
					<tr>
						<td><%=user.getEmail()%></td>
						<td><%=user.getNome()%></td>
						<td><%=user.getCognome()%></td>
						<td><%=user.getNumeroTelefono()%></td>
						<td><%=user.getIndirizzo().getNomeCitta()%></td>
						<td><%=user.getIndirizzo().getVia()%></td>
						<td><%=user.getIndirizzo().getNumeroCivico()%></td>
						<td><%=user.getIndirizzo().getCap()%></td>
						<td style="text-align: center">
				
							<form action="" method="post">
								<input type="hidden" name="email"
									value="<%=user.getEmail()%>">
								<button type="submit" class="btn btn-primary">Modifica</button>
							</form>
						</td>
						<td style="text-align: center">
							
							<form action="RemoveUserServlet" method="post">
								<input type="hidden" name="email"
									value="<%=user.getEmail()%>">
								<button type="submit" class="btn btn-danger">Cancella</button>
							</form>
						</td>
						<%
						}
						} else {
						%>
					
					
					<tr>
						<td colspan="10" style="text-align: center;">Nessun utente.</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>
		</div>
	</div>

	<%@ include file="/fragments/footer.html"%>
</body>
</html>
