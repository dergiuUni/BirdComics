<%@page import="com.birdcomics.Model.Bean.OrderBean"%>
<%@page import="com.birdcomics.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page
	import="com.birdcomics.*,java.util.*,javax.servlet.ServletOutputStream,java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Product Stocks</title>
<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/changes.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<jsp:include page="./fragments/header.jsp" />
<body>

  <%   List<String> userRoles = (List<String>) session.getAttribute("usertype");

  if (userRoles == null || !userRoles.contains("Spedizioniere")) {
      RequestDispatcher dispatcher = request.getRequestDispatcher("./LoginSrv");
      dispatcher.forward(request, response);
      return;
  }

    
%>


<div class="text-center"
    style="color: green; font-size: 24px; font-weight: bold; margin-top: 15px; margin-bottom: 15px;">Gestione Ordini Non Spediti</div>

 
 <% String message = (String) request.getAttribute("message");%>
    <h3 style="text-align: center;"><%= message%></h3>
<div class="container-fluid">
    <div class="table-responsive">
        <table class="table table-hover table-sm">
            <thead style="background-color: #4CAF50; color: white; font-size: 18px;">
                <tr>
                    <th>Id</th>
                    <th>Email</th>
                    <th>StatoSpedizione</th>
                    <th colspan="2" style="text-align: center">Actions</th>
                            
                </tr>
            </thead>
            <tbody style="background-color: white; font-size: 16px;">
                <% List<OrderBean> products = (List<OrderBean>) request.getAttribute("products");
                   if (products != null) {
                       for (OrderBean product : products) { %>
       
                           <tr>
                               <td><a href="./updateOrder.jsp?prodid=<%= product.getId() %>"><%= product.getId() %></a></td>
                               <%-- Limit name to 25 characters --%>
                               <td><%= product.getIdUtente()%></td>
                               <td><%= product.getShipped() %></td>
                               <td>
                                   <form method="get" action="./UpdateOrderSrv">
                                       <input type="hidden" name="prodid" value="<%= product.getId() %>">
                                       <button type="submit" class="btn btn-primary">Update</button>
                                   </form>
                               </td>

                           </tr>
               <%     }
                     } else { %>
                           <tr style="background-color: grey; color: white;">
                               <td colspan="7" style="text-align: center;">No Items Available</td>
                           </tr>
               <%  } %>
            </tbody>
        </table>
    </div>
</div>
<%@ include file="/fragments/footer.html" %>
</body>
</html>
      

