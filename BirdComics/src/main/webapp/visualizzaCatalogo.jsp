<%@page import="com.birdcomics.Bean.ProductBean"%>
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

  if (userRoles == null || !userRoles.contains("GestoreCatalogo")) {
      RequestDispatcher dispatcher = request.getRequestDispatcher("./LoginSrv");
      dispatcher.forward(request, response);
      return;
  }

    
%>


<div class="text-center"
    style="color: green; font-size: 24px; font-weight: bold; margin-top: 15px; margin-bottom: 15px;">Gestione libri</div>


    <form class="search-form" action="GestioneCatalogo" method="get">
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
                    <th>Id</th>
                    <th>Nome</th>
                    <th>Prezzo</th>
                    <th colspan="2" style="text-align: center">Actions</th>
                            
                </tr>
            </thead>
            <tbody style="background-color: white; font-size: 16px;">
                <% List<ProductBean> products = (List<ProductBean>) request.getAttribute("products");
                   if (products != null) {
                       for (ProductBean product : products) { %>
       
                           <tr>
                               <td><img src="./ShowImage?image=<%= product.getImage()%>"
                                    style="width: 50px; height: 50px;"></td>
                               <td><a href="./updateProduct.jsp?prodid=<%= product.getId() %>"><%= product.getId() %></a></td>
                               <%-- Limit name to 25 characters --%>
                               <td><%= product.getName().substring(0, Math.min(product.getName().length(), 25)) + ".." %></td>
                               <td><%= product.getPrice() %></td>
                               <td>
                                   <form method="get" action="./UpdateProductSrv">
                                       <input type="hidden" name="prodid" value="<%= product.getId() %>">
                                       <button type="submit" class="btn btn-primary">Update</button>
                                   </form>
                               </td>
                               <td>
                                   <form method="post" action="./RemoveProductSrv">
                                       <input type="hidden" name="prodid" value="<%= product.getId() %>">
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
      

