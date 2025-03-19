<%@page import="com.birdcomics.Model.Bean.ProductBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.birdcomics.*"%>
<%@ page import="java.util.*"%>
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
    
    <% ProductBean product = (ProductBean) request.getAttribute("product"); %>
    <% String message = request.getParameter("message"); %>
    <%
	List<String> genres = (List<String>) request.getAttribute("genres");
    //<--recuperare anche generi del product tramite id e impostarli nella lista generi come gia selezionati-->
	%>
	

 
    <div class="container">
        <div class="row" style="margin-top: 35px; margin-left: 2px; margin-right: 2px;">
            <form action="./UpdateProductSrv" method="post"
				enctype="multipart/form-data" class="col-md-6 col-md-offset-3"
				onsubmit="return validateForm()">
                <div style="font-weight: bold;" class="text-center">
                    <div class="form-group">
                        <img src="./ShowImage?image=<%=product.getImage()%>" alt="Product Image" style="width: 200px; height: 280px;"/>
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
                    <input type="hidden" name="pid" class="form-control" value="<%= product.getId() %>" required>
                </div>

                <div class="row">
                    <div class="col-md-6 form-group">
                        <label for="name">Product Name</label>
                        <input type="text" name="name" class="form-control" id="name" value="<%= product.getName() %>" onkeyup="checkName(this)" required>
                        <div><p id="nameText"></p></div> <!-- Mostra messaggi di errore qui -->
                    </div>
                </div>

                <div class="form-group">
                    <label for="info">Product Description</label>
                    <textarea name="info" class="form-control" id="info" onkeyup="checkDescription(this)" required><%= product.getDescription() %></textarea>
                    <div><p id="descriptionText"></p></div> <!-- Mostra messaggi di errore qui -->
                </div>

                <div class="row">
                    <div class="col-md-6 form-group">
                        <label for="price">Unit Price</label>
                        <input type="number" step="0.01" name="price" class="form-control" id="price" value="<%= product.getPrice() %>" onkeyup="checkPrice(this)" required>
                        <div><p id="priceText"></p></div> <!-- Mostra messaggi di errore qui -->
                    </div>
                </div>
                <div class="form-group">
					<label>Genres</label>
					<div class="dropdown">
						<button class="btn btn-default dropdown-toggle" type="button"
							id="genreDropdown" data-toggle="dropdown" aria-haspopup="true"
							aria-expanded="true">
							Select Genres <span class="caret"></span>
						</button>
						<ul class="dropdown-menu" aria-labelledby="genreDropdown">
							<%
							if (genres != null) {
								for (String genre : genres) {
							%>
							<li><label class="checkbox-inline"> <input
									type="checkbox" name="genres" value="<%=genre%>"
									onclick="checkGenres()"> <%=genre%>
							</label></li>
							<%
							}
							}
							%>
						</ul>
					</div>
					<p id="genreText" style="color: red;"></p>
				</div>
                
                <div class="form-group">
					<label for="image">Product Image</label> <input type="file"
						name="image" class="form-control" id="image"
						accept=".png, .jpeg, .jpg, .gif">
					<p id="imageText"></p>
				</div>

                <div class="row text-center">
                    <div class="col-md-4" style="margin-bottom: 2px;">
                        <button formaction="GestioneCatalogo" class="btn btn-danger">Cancel</button>
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
