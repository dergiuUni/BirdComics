<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page
    import="com.birdcomics.*,java.util.*,javax.servlet.ServletOutputStream,java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Payments</title>
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
<jsp:include page="/fragments/header.jsp" />
<body>
    <div class="container">
        <div class="row"
            style="margin-top: 35px; margin-left: 2px; margin-right: 2px; margin-bottom: 35px">
            <form method="post" action="confermaIndirizzo.jsp">
				<button style="background-color: black; color: white;"
					type="submit">Conferma Ordine</button>
				<input type="hidden" name="amount" value="${totAmount}">
			</form>
			
			<form method="post" action="index.jsp">
				<button style="background-color: black; color: white;"
					type="submit">Annulla Ordine</button>
				<input type="hidden" name="amount" value="${totAmount}">
			</form>
        </div>
    </div>
<%@ include file="fragments/footer.html"%>
</body>
</html>
