<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>
<%@ include file="/fragments/header.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>Birdcomics</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" href="css/changes.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<script src="scripts/ajax.js"></script>
</head>
<body>
	<br>
	<br>

	<!-- Placeholder for Product Items List -->
	<div id="product-list" class="container">
		<!-- Products will be loaded dynamically here -->
	</div>
<%@ include file="/fragments/footer.html"%>
</body>
</html>
