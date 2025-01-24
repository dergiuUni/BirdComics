<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.birdcomics.GestioneCatalogo.ProductBean"%>
<!DOCTYPE html>
<html>
<head>
    <title>Update Product</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/changes.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
    <script src="scripts/validation.js"></script> <!-- Includi il tuo file di validazione -->
</head>
<jsp:include page="/fragments/header.jsp" />
<body>
    <%-- Recupera il prodotto e il messaggio dalla richiesta --%>
    <% ProductBean product = (ProductBean) request.getAttribute("product1"); %>
    <% String message = request.getParameter("message"); %>

    <%-- Controlla se il prodotto è nullo, se sì, reindirizza a updateProductById.jsp --%>
    <% if (product == null) {
        response.sendRedirect("updateProductById.jsp?message=Please Enter a valid product Id");
        return;
    } %>

    <div class="container">
        <div class="row" style="margin-top: 35px; margin-left: 2px; margin-right: 2px;">
            <form action="./UpdateProductSrv" method="post" class="col-md-6 col-md-offset-3">
                <div style="font-weight: bold;" class="text-center">
                    <div class="form-group">
                        <img src="./ShowImage?pid=<%=product.getProdId()%>" alt="Product Image" height="100px" />
                        <h2 style="color: green;">Product Update Form</h2>
                    </div>

                    <%-- Visualizza il messaggio se disponibile --%>
                    <% if (message != null && !message.isEmpty()) { %>
                        <p style="color: blue;">
                            <%= message %>
                        </p>
                    <% } %>
                </div>

                <div class="row">
                    <input type="hidden" name="pid" class="form-control" value="<%= product.getProdId() %>" required>
                </div>

                <div class="row">
                    <div class="col-md-6 form-group">
                        <label for="name">Product Name</label>
                        <input type="text" name="name" class="form-control" id="name" value="<%= product.getProdName() %>" onkeyup="checkName(this)" required>
                        <div><p id="nameText"></p></div> <!-- Mostra messaggi di errore qui -->
                    </div>
                    <div class="col-md-6 form-group">
                        <label for="type">Product Type</label>
                        <select name="type" id="type" class="form-control" required>
                            <option value="avventura" <%= "avventura".equalsIgnoreCase(product.getProdType()) ? "selected" : "" %>>Avventura</option>
                            <option value="azione" <%= "azione".equalsIgnoreCase(product.getProdType()) ? "selected" : "" %>>Azione</option>
                            <option value="horror" <%= "horror".equalsIgnoreCase(product.getProdType()) ? "selected" : "" %>>Horror</option>
                            <option value="thriller" <%= "thriller".equalsIgnoreCase(product.getProdType()) ? "selected" : "" %>>Thriller</option>
                            <option value="fantasy" <%= "fantasy".equalsIgnoreCase(product.getProdType()) ? "selected" : "" %>>Fantasy</option>
                            <option value="drammatico" <%= "drammatico".equalsIgnoreCase(product.getProdType()) ? "selected" : "" %>>Drammatico</option>
                            <option value="fantascienza" <%= "fantascienza".equalsIgnoreCase(product.getProdType()) ? "selected" : "" %>>Fantascienza</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="info">Product Description</label>
                    <textarea name="info" class="form-control" id="info" onkeyup="checkDescription(this)" required><%= product.getProdInfo() %></textarea>
                    <div><p id="descriptionText"></p></div> <!-- Mostra messaggi di errore qui -->
                </div>

                <div class="row">
                    <div class="col-md-6 form-group">
                        <label for="price">Unit Price</label>
                        <input type="number" step="0.01" name="price" class="form-control" id="price" value="<%= product.getProdPrice() %>" onkeyup="checkPrice(this)" required>
                        <div><p id="priceText"></p></div> <!-- Mostra messaggi di errore qui -->
                    </div>
                    <div class="col-md-6 form-group">
                        <label for="quantity">Stock Quantity</label>
                        <input type="number" name="quantity" class="form-control" id="quantity" value="<%= product.getProdQuantity() %>" onkeyup="checkQuantity(this)" required>
                        <div><p id="quantityText"></p></div> <!-- Mostra messaggi di errore qui -->
                    </div>
                </div>

                <div class="row text-center">
                    <div class="col-md-4" style="margin-bottom: 2px;">
                        <button formaction="adminStock" class="btn btn-danger">Cancel</button>
                    </div>
                    <div class="col-md-4">
                        <button type="submit" class="btn btn-success">Update Product</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</body>
<jsp:include page="/fragments/footer.html" />
</html>
