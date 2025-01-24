<%@page import="com.birdcomics.GestioneOrdine.OrderBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List, com.birdcomics.*"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Shipped Items</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/changes.css">
</head>
<jsp:include page="/fragments/header.jsp" />
<body>
	<%
    List<OrderBean> orders = (List<OrderBean>) request.getAttribute("orders");
    List<String> ArrayUserId = (List<String>) request.getAttribute("ArrayUserId");
    List<String> ArrayUserAddr = (List<String>) request.getAttribute("ArrayUserAddr");
    List<String> ArrayDateTime = (List<String>) request.getAttribute("ArrayDateTime");

    %>



	<form class="search-form" action="ShipmentServlet" method="get">
		<input type="text" name="search" placeholder="Cerca utente per email">
		<label for="startDate">Da:</label> <input type="date" id="startDate"
			name="startDate" placeholder="Da data"> <label for="endDate">A:</label>
		<input type="date" id="endDate" name="endDate" placeholder="A data">
		<input type="submit" value="Submit">
	</form>



	<div class="container-fluid">
		<div class="text-center"
			style="color: green; font-size: 24px; font-weight: bold; margin-top: 15px; margin-bottom: 15px;">
			Orders</div>
		<div class="table-responsive">
			<table class="table table-hover table-sm">
				<thead
					style="background-color: #4CAF50; color: white; font-size: 16px;">
					<tr>
						<th>TransactionId</th>
						<th>ProductId</th>
						<th>User Email Id</th>
						<th>Address</th>
						<th>Date time</th>
						<th>Quantity</th>
						<th>Status</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody style="background-color: white;">

					<% 
                    
       				int index = 0;
                    for (OrderBean order : orders) {
                        String transId = order.getTransactionId();
                        String prodId = order.getProductId();
                        int quantity = order.getQuantity();
                        int shipped = order.getShipped();
                        String userId = ArrayUserId.get(index);
                        String userAddr = ArrayUserAddr.get(index);
                        String dateTime = ArrayDateTime.get(index);
                        index++;
                        if (shipped == 0) {
                    %>

					<tr>
						<td><%=transId%></td>
						<td><%=prodId%></td>
						<td><%=userId%></td>
						<td><%=userAddr%></td>
						<td><%=dateTime%></td>
						<td><%=quantity%></td>
						<td>READY_TO_SHIP</td>
						<td><a
							href="ShipmentServlet?orderid=<%=order.getTransactionId()%>&userid=<%=userId%>&prodid=<%=order.getProductId()%>"
							class="btn btn-danger">SHIP NOW</a></td>
					</tr>

					<% 
                        } else {
                    %>

					<tr>
						<td><%=transId%></td>
						<td><%=prodId%></td>
						<td><%=userId%></td>
						<td><%=userAddr%></td>
						<td><%=dateTime%></td>
						<td><%=quantity%></td>
						<td>SHIPPED</td>
						<td></td>
						<!-- Empty action cell for shipped items -->
					</tr>

					<% 
                        }
                    } 
                    %>

				</tbody>
			</table>
		</div>
	</div>

	<%@ include file="/fragments/footer.html"%>
</body>
</html>