<%@page import="com.birdcomics.Bean.ProductBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.birdcomics.*,java.util.*,javax.servlet.ServletOutputStream,java.io.*"%>
<!DOCTYPE html>
<html>
<head>
    <title>Birdcomics</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/changes.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<title>Libro</title>
<jsp:include page="/fragments/header.jsp"/>
<body>

<%
    /* Checking the user credentials */
    ProductBean product = (ProductBean) request.getAttribute("product");
%>

<div class="white-box">
    <div class="prod-container">
        <!-- Left Column / Product Image -->
        <div class="left-column">
            <img src="./ShowImage?image=<%= product.getImage() %>" alt="Product">
        </div>
        
        <!-- Right Column / Product Details -->
        <div class="right-column">
            <div class="product-description">
           <h4 style="display: inline;">Genere:</h4>
                <span></span>
                <h4><%=product.getName()%></h4>
            </div>
            
            <hr>
            
            <!-- Product Pricing and Add to Cart Button -->
            <div class="product-price">
                <span><%=product.getPrice()%>&euro;</span>
                <form method="post" action="AddToCart">
                    <input type="hidden" name="pid" value="<%= product.getId() %>">
                    <input type="number" name="pqty" value="1" min="1" max="">
                    <button type="submit" class="btn btn-success">Add to Cart</button>
                </form>
            </div>
            
            <hr>
            
        </div>
    </div>
</div>

<div class="product-description-down">
    <hr>
    <h4>Descrizione</h4>
    <hr>
    <p><%=product.getDescription()%></p>
</div>

</body>
</html>
<%@ include file="fragments/footer.html"%>