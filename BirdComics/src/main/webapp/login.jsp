<!DOCTYPE html>
<html lang="en">
<head>
<title>Login</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" href="css/changes.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<%@ include file="./fragments/header.jsp"%>
<body>


	<div class="container">
		<h2>Login Page</h2>

		<form action="LoginSrv" method="post">
			<!-- Mostra il messaggio di errore se presente -->
			<c:if test="${not empty param.message}">
				<p style="color: red;">${param.message}</p>
			</c:if>

			<div class="row">
				<div class="col-md-12 form-group">
					<label for="email">Email</label> <input type="email"
						class="form-control" id="username" name="username"
						placeholder="Enter Email" required>
				</div>
			</div>

			<div class="row">
				<div class="col-md-12 form-group">
					<label for="password">Password</label> <input type="password"
						class="form-control" id="password" name="password"
						placeholder="Enter Password" required>
				</div>
			</div>

			<div class="row">
				<div class="col-md-12 text-center">
					<button type="submit" class="btn btn-success">Login</button>
				</div>
			</div>
		</form>

	</div>
<%@ include file="./fragments/footer.html"%>
</body>
</html>
