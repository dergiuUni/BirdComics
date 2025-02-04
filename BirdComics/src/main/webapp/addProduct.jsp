<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Add Product</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/changes.css">
<script src="scripts/validation.js"></script>
</head>
<jsp:include page="./fragments/header.jsp" />
<body>
	<%
	List<String> genres = (List<String>) request.getAttribute("genres");
	%>
	<div class="container">
		<div class="row" style="margin-top: 35px; margin-bottom: 35px;">
			<form name="addProductForm" action="./AddProductSrv" method="post"
				enctype="multipart/form-data" class="col-md-6 col-md-offset-3"
				onsubmit="return validateForm()">
				<div class="text-center" style="font-weight: bold;">
					<h2 style="color: green;">Product Addition Form</h2>
					<%
					String message = request.getParameter("message");
					if (message != null) {
					%>
					<p style="color: blue;"><%=message%></p>
					<%
					}
					%>
				</div>
				<div class="row">
					<div class="col-md-6 form-group">
						<label for="name">Product Name</label> <input type="text"
							placeholder="Enter Product Name" name="name" class="form-control"
							id="name" onkeyup="checkName(this)" required>
						<p id="nameText"></p>
					</div>
				</div>
				<div class="form-group">
					<label for="description">Product Description</label>
					<textarea name="info" class="form-control" id="description"
						onkeyup="checkDescription(this)" required></textarea>
					<p id="descriptionText"></p>
				</div>
				<div class="row">
					<div class="col-md-6 form-group">
						<label for="price">Unit Price</label> <input type="number"
							step="0.01" placeholder="Enter Unit Price" name="price"
							class="form-control" id="price" onkeyup="checkPrice(this)"
							required>
						<p id="priceText"></p>
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
						onchange="checkImageType(this)" accept=".png, .jpeg, .jpg, .gif"
						required>
					<p id="imageText"></p>
				</div>
				<div class="row">
					<div class="col-md-6 text-center" style="margin-bottom: 2px;">
						<a href="adminStock" class="btn btn-info">Cancel</a>
					</div>
					<div class="col-md-6 text-center">
						<button type="submit" class="btn btn-success">Add Product</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	<%@ include file="/fragments/footer.html"%>
</body>
</html>