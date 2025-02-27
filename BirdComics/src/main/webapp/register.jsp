<%@page import="com.birdcomics.Bean.RuoloBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Registrazione Utente</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" href="css/changes.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<script src="scripts/registerValidation.js"></script>
</head>
<%@ include file="./fragments/header.jsp"%>
<body>
	<div class="container" style="margin-top: 35px; margin-bottom: 40px;">
		<div class="row"
			style="margin-top: 5px; margin-left: 2px; margin-right: 2px;">
			<form action="./RegisterSrv" method="post"
				class="col-md-6 col-md-offset-3">
				<div style="font-weight: bold;" class="text-center">
					<h2>Modulo di Registrazione</h2>
					<c:if test="${not empty param.message}">
						<p style="color: blue;">${param.message}</p>
					</c:if>
				</div>

				<div class="row">
					<div class="col-md-6 form-group">
						<label for="nome">Nome</label> <input type="text" name="nome"
							class="form-control" id="nome" required>
					</div>
					<div class="col-md-6 form-group">
						<label for="cognome">Cognome</label> <input type="text"
							name="cognome" class="form-control" id="cognome" required>
					</div>
				</div>

				<div class="form-group">
					<label for="email">Email</label> <input type="email" name="email"
						class="form-control" id="email" required>
				</div>

				<div class="form-group">
					<label for="telefono">Telefono</label> <input type="text"
						name="telefono" class="form-control" id="telefono" required>
				</div>

				<div class="row">
					<div class="col-md-6 form-group">
						<label for="nomeCitta">Città</label> <input type="text"
							name="nomeCitta" class="form-control" id="nomeCitta" required>
					</div>
					<div class="col-md-6 form-group">
						<label for="via">Via</label> <input type="text" name="via"
							class="form-control" id="via" required>
					</div>
				</div>

				<div class="row">
					<div class="col-md-6 form-group">
						<label for="numeroCivico">Numero Civico</label> <input type="text"
							name="numeroCivico" class="form-control" id="numeroCivico"
							required>
					</div>
					<div class="col-md-6 form-group">
						<label for="cvc">CAP</label> <input type="text" name="cap"
							class="form-control" id="cap" required>
					</div>
				</div>

				<div class="form-group">
					<label for="dataNascita">Data di Nascita</label> <input type="date"
						name="dataNascita" class="form-control" id="dataNascita">
				</div>

				<div class="row">
					<div class="col-md-6 form-group">
						<label for="password">Password</label> <input type="password"
							name="password" class="form-control" id="password" required>
					</div>
					<div class="col-md-6 form-group">
						<label for="confirmPassword">Conferma Password</label> <input
							type="password" name="confirmPassword" class="form-control"
							id="confirmPassword" required>
					</div>
				</div>




				<% List<String> userTypesS = (List<String>) session.getAttribute("usertype");
	        		if (userTypesS != null && !userTypesS.isEmpty() && userTypesS.contains(RuoloBean.RisorseUmane.toString())) {
	        			
				%>

				<div class="row">
					<div class="col-md-6 form-group">
						<label for="RuoloDipendente">Ruoli</label>
						<div class="form-group">
							<div class="checkbox">
								<label><input type="checkbox" name="ruoli"
									value="Assistenza"> Assistenza</label>
							</div>
							<div class="checkbox">
								<label><input type="checkbox" name="ruoli"
									value="Finanza"> Finanza</label>
							</div>
							<div class="checkbox">
								<label><input type="checkbox" name="ruoli"
									value="GestoreCatalogo"> Gestore Catalogo</label>
							</div>
							<div class="checkbox">
								<label><input type="checkbox" name="ruoli"
									value="Magazziniere"> Magazziniere</label>
							</div>
							<div class="checkbox">
								<label><input type="checkbox" name="ruoli"
									value="Spedizioniere"> Spedizioniere</label>
							</div>
						</div>

					</div>

				</div>
				
				<div class="row text-center">
					<div class="col-md-6" style="margin-bottom: 2px;">
						<button type="reset" class="btn btn-danger">Reset</button>
					</div>
					<div class="col-md-6">
						<button type="submit" class="btn btn-success">Registra Account</button>
					</div>
				</div>
			</form>

				<% 
	        		}
	        		else{
	        			
	        		
				%>


				<div class="row text-center">
					<div class="col-md-6" style="margin-bottom: 2px;">
						<button type="reset" class="btn btn-danger">Reset</button>
					</div>
					<div class="col-md-6">
						<button type="submit" class="btn btn-success">Registrati</button>
					</div>
				</div>
			</form>
	
				<% 
	        		
	        		}
	        			
	        		
				%>
			



		</div>
	</div>
	<%@ include file="fragments/footer.html"%>
</body>
</html>
