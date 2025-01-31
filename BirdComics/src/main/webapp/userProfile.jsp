<%@page import="com.birdcomics.GestioneProfili.UserBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="com.birdcomics.*,java.util.*,javax.servlet.ServletOutputStream,java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Profile Details</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" href="css/changes.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<jsp:include page="fragments/header.jsp" />
<body>

	<%
	UserBean user = (UserBean) request.getAttribute("user");
	%>

	<div class="container bg-secondary" style="margin-top: 50px;">


		<div class="row">
			<div class="col-lg-4">
				<div class="card mb-4">
					<div class="card-body text-center">
						<img
							src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"
							class="rounded-circle img-fluid" style="width: 150px;">
						<h5 class="my-3">
							Hello
							<%=user.getNome()%>

						</h5>
						<!-- <p class="text-muted mb-1">Full Stack Developer</p>
						<p class="text-muted mb-4">Bay Area, San Francisco, CA</p> -->
					</div>
				</div>
				<div class="card mb-4 mb-lg-0">
					<div class="card-body p-0">
						<ul class="list-group list-group-flush rounded-3">

							<li
								class="text-center list-group-item d-flex justify-content-between align-items-center p-3">
								<h3>Il mio profilo</h3>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="col-lg-8">
				<div class="card mb-4">
					<div class="card-body">
						<div class="row">
							<div class="col-sm-3">
								<p class="mb-0">Nome</p>
							</div>
							<div class="col-sm-9">
								<p class="text-muted mb-0"><%=user.getNome()%></p>
							</div>
						</div>
						<hr>
							<div class="row">
							<div class="col-sm-3">
								<p class="mb-0">Cognome</p>
							</div>
							<div class="col-sm-9">
								<p class="text-muted mb-0"><%=user.getCognome()%></p>
							</div>
						</div>
						<hr>
						<div class="row">
							<div class="col-sm-3">
								<p class="mb-0">Email</p>
							</div>
							<div class="col-sm-9">
								<p class="text-muted mb-0"><%=user.getEmail()%>
								</p>
							</div>
						</div>
						<hr>
						<div class="row">
							<div class="col-sm-3">
								<p class="mb-0">Telefono</p>
							</div>
							<div class="col-sm-9">
								<p class="text-muted mb-0"><%=user.getNumeroTelefono()%>
								</p>
							</div>
						</div>
						<hr>
							<div class="row">
							<div class="col-sm-3">
								<p class="mb-0">Data di Nascita</p>
							</div>
							<div class="col-sm-9">
								<p class="text-muted mb-0"><%=user.getDataNascita()%>
								</p>
							</div>
						</div>
						<hr>
						<div class="row">
							<div class="col-sm-3">
								<p class="mb-0">Città</p>
							</div>
							<div class="col-sm-9">
								<p class="text-muted mb-0"><%=user.getIndirizzo().getNomeCitta()%>
								</p>
							</div>
						</div>
						<hr>
							<div class="row">
							<div class="col-sm-3">
								<p class="mb-0">Via</p>
							</div>
							<div class="col-sm-9">
								<p class="text-muted mb-0"><%=user.getIndirizzo().getVia()%>
								</p>
							</div>
						</div>
						<hr>
							<div class="row">
							<div class="col-sm-3">
								<p class="mb-0">Numero Civico</p>
							</div>
							<div class="col-sm-9">
								<p class="text-muted mb-0"><%=user.getIndirizzo().getNumeroCivico()%>
								</p>
							</div>
						</div>
						<hr>
						<div class="row">
							<div class="col-sm-3">
								<p class="mb-0">CAP</p>
							</div>
							<div class="col-sm-9">
								<p class="text-muted mb-0"><%=user.getIndirizzo().getCap()%>
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		  <div class="text-center">
            <!-- Aggiungi il tasto "Update Profile" qui -->
            <br>
            <br>
         
        </div>
	</div>

	<br>
	<br>
	<br>
</body>
<%@ include file="fragments/footer.html"%>
</html>
