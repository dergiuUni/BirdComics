<%@page import="com.birdcomics.GestioneCatalogo.ProductBean"%>
<%@page import="com.birdcomics.GestioneProfili.Cliente.CartBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="com.birdcomics.*, java.util.List, java.util.ArrayList, javax.servlet.ServletOutputStream, java.io.*"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Cart Details</title>
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
<%@ include file="/fragments/header.jsp"%>
<body>



	<br>
	<br>

	<div class="text-center"
		style="color: green; font-size: 24px; font-weight: bold;">Carrello</div>
	<br>
	<br>

	<!-- Start of Product Items List -->
	<div class="container">
		<table class="table table-hover">
			<thead
				style="background-color: black; color: white; font-size: 16px; font-weight: bold;">
				<tr>
					<th>Foto</th>
					<th>Prodotto</th>
					<th>Prezzo</th>
					<th>Quantità</th>
					<th>Prezzo Totale</th>
				</tr>
			</thead>
			<tbody
				style="background-color: white; font-size: 15px; font-weight: bold;">
				<% 
                List<CartBean> cartItems = (List<CartBean>) request.getAttribute("cartItems");
                //List<ProductBean> products = new ProductServiceDAO().getProductDetails(prodId);
                List<ProductBean> products = (List<ProductBean>) request.getAttribute("productItems");

                double totAmount = (double) request.getAttribute("totAmount");

                if (cartItems != null && !cartItems.isEmpty()) {
                	int index = 0;
                    for (CartBean item : cartItems) {
                    	
                        String prodId = item.getProdId();
                        int prodQuantity = item.getQuantity();
                        
                        ProductBean product = products.get(index);
                        double currAmount = product.getProdPrice() * prodQuantity;
                        index++;
                %>
				<tr>
					<td><img src="./ShowImage?pid=<%=prodId%>"
						style="width: 50px; height: 50px;"></td>
					<td><%=product.getProdName()%></td>
					<td><%=product.getProdPrice()%></td>
					<td>
						<form method="post" action="./UpdateToCart">
							<input type="number" name="pqty" value="<%=prodQuantity%>"
								style="max-width: 70px;" min="0"> <input type="hidden"
								name="pid" value="<%=prodId%>"> <input type="submit"
								name="Update" value="Aggiorna" style="max-width: 80px;">
						</form>
					</td>
					<td><%=currAmount%></td>
				</tr>
				<% 
                    }
                } else {
                %>
				<tr>
					<td colspan="5">Nessun articolo nel carrello.</td>
				</tr>
				<% } %>
				<tr style="background-color: grey; color: white;">
					<td colspan="4" style="text-align: center;">Totale da pagare</td>
					<td>${totAmount}</td>
					<!-- Assumendo che totAmount sia disponibile in servlet -->
				</tr>
				<%-- Mostra i pulsanti Cancella e Paga solo se il totale non è zero --%>
				<% if (totAmount != 0) { %>
				<tr style="background-color: grey; color: white;">
					<td colspan="3" style="text-align: center;"></td>
					<td>
						<form method="post">
							<button formaction="EmptyCartServlet"
								style="background-color: black; color: white;">Svuota</button>
						</form>
					</td>
					<td colspan="2" align="center">
						<form method="post" action="payment.jsp">
							<button style="background-color: black; color: white;"
								type="submit">Paga ora</button>
							<input type="hidden" name="amount" value="${totAmount}">
						</form>
					</td>
				</tr>
				<% } %>
			</tbody>
		</table>
	</div>
	<!-- End of Product Items List -->

	<br>
	<br>
<%@ include file="/fragments/footer.html"%>
</body>
</html>
