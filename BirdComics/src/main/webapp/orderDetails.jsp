<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Order Details</title>
<!-- Includi i tuoi stili e script -->
</head>
<%@ include file="/fragments/header.jsp"%>
<body>


	<br>
	<br>
	<div class="text-center"
		style="color: green; font-size: 24px; font-weight: bold;">Order
		Details</div>
	<br>
	<br>
	<div class="container">
		<div class="table-responsive">
			<table class="table table-hover table-sm">
				<thead
					style="background-color: black; color: white; font-size: 14px; font-weight: bold;">
					<tr>
						<th>Picture</th>
						<th>ProductName</th>
						<th>OrderId</th>
						<th>Quantity</th>
						<th>Price</th>
						<th>Time</th>
						<th>Status</th>
					</tr>
				</thead>
				<tbody
					style="background-color: white; font-size: 15px; font-weight: bold;">
					<c:forEach var="order" items="${orders}">
						<tr>
							<td><img src="./ShowImage?pid=${order.productId}"
								style="width: 50px; height: 50px;"></td>
							<td>${order.prodName}</td>
							<td>${order.orderId}</td>
							<td>${order.qty}</td>
							<td>${order.amount}</td>
							<td>${order.time}</td>
							<td class="text-success">${order.shipped == 0 ? "ORDER_PLACED" : "ORDER_SHIPPED"}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<br>
	<br>
<%@ include file="/fragments/footer.html"%>
</body>
</html>
