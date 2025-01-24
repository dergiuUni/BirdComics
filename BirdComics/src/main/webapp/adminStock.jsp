<%@page import="com.birdcomics.GestioneOrdine.OrderServiceDAO"%>
<%@page import="com.birdcomics.GestioneCatalogo.ProductBean"%>
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
  <%  String userType = (String) session.getAttribute("usertype");
     if (userType == null || userType.isEmpty()) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("./adminStock");
        dispatcher.forward(request, response);

        return;
    }
     
     if (userType.isEmpty()) {
         return;
     }
%>



<div class="text-center"
    style="color: green; font-size: 24px; font-weight: bold; margin-top: 15px; margin-bottom: 15px;">Gestione libri</div>


    <form class="search-form" action="adminStock" method="get">
        <input type="text" name="search" placeholder="Cerca un libro..."">
        <input type="submit" value="Submit">
    </form>   
 <% String message = (String) request.getAttribute("message");%>
    <h3 style="text-align: center;"><%= message%></h3>
<div class="container-fluid">
    <div class="table-responsive">
        <table class="table table-hover table-sm">
            <thead style="background-color: #4CAF50; color: white; font-size: 18px;">
                <tr>
                    <th>Image</th>
                    <th>ProductId</th>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Price</th>
                    <th>Sold Qty</th>
                    <th>Stock Qty</th>
                    <th colspan="2" style="text-align: center">Actions</th>
                </tr>
            </thead>
            <tbody style="background-color: white; font-size: 16px;">
                <% List<ProductBean> products = (List<ProductBean>) request.getAttribute("products");
                List<Integer> soldQuantities = (List<Integer>) request.getAttribute("soldQuantities");
                int index = 0;
            	OrderServiceDAO orderDao = new OrderServiceDAO();
                   if (products != null) {
                       for (ProductBean product : products) { %>
       
                           <tr>
                               <td><img src="./ShowImage?pid=<%= product.getProdId() %>"
                                    style="width: 50px; height: 50px;"></td>
                               <td><a href="./updateProduct.jsp?prodid=<%= product.getProdId() %>"><%= product.getProdId() %></a></td>
                               <%-- Limit name to 25 characters --%>
                               <% String name = product.getProdName().substring(0, Math.min(product.getProdName().length(), 25)) + ".."; %>
                               <td><%= name %></td>
                               <td><%= product.getProdType().toUpperCase() %></td>
                               <td><%= product.getProdPrice() %></td>
                               <td><%= soldQuantities.get(index++) %></td>
                               <td><%= product.getProdQuantity() %></td>
                               <td>
                                   <form method="get" action="./UpdateProductSrv">
                                       <input type="hidden" name="prodid" value="<%= product.getProdId() %>">
                                       <button type="submit" class="btn btn-primary">Update</button>
                                   </form>
                               </td>
                               <td>
                                   <form method="post" action="./RemoveProductSrv">
                                       <input type="hidden" name="prodid" value="<%= product.getProdId() %>">
                                       <button type="submit" class="btn btn-danger">Remove</button>
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
      

