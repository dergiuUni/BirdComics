<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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


	<div class="container">
		<div class="row"
			style="margin-top: 35px; margin-left: 2px; margin-right: 2px; margin-bottom: 35px;">
			<form name="addProductForm" action="./AddProductSrv" method="post"
				enctype="multipart/form-data" class="col-md-6 col-md-offset-3"
				onsubmit="return validateForm()">
				<div style="font-weight: bold;" class="text-center">
					<h2 style="color: green;">Product Addition Form</h2>
					<%
					String message = request.getParameter("message");
					if (message != null) {
					%>
					<p style="color: blue;">
						<%=message%>
					</p>
					<%
					}
					%>
				</div>
				<div></div>
				<div class="row">
					<div class="col-md-6 form-group">
						<label for="last_name">Product Name</label> <input type="text"
							placeholder="Enter Product Name" name="name" class="form-control"
							id="name" onkeyup="checkName(this)" required>
						<div>
							<p id="nameText"></p>
						</div>
					</div>
					<div class="col-md-6 form-group">
						<label for="producttype">Product Type</label> <select name="type"
							id="producttype" class="form-control" required>
							<option value="avventura">Avventura</option>
							<option value="azione">Azione</option>
							<option value="horror">Horror</option>
							<option value="thriller">Thriller</option>
							<option value="fantasy">Fantasy</option>
							<option value="drammatico">Drammatico</option>
							<option value="fantascienza">Fantascienza</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="last_name">Product Description</label>
					<textarea name="info" class="form-control" id="description"
						onkeyup="checkDescription(this)" required></textarea>
					<div>
						<p id="descriptionText"></p>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6 form-group">
						<label for="last_name">Unit Price</label> <input type="number"
							step="0.01" placeholder="Enter Unit Price" name="price"
							class="form-control" id="price" onkeyup="checkPrice(this)"
							required>
						<div>
							<p id="priceText"></p>
						</div>
					</div>
					<div class="col-md-6 form-group">
						<label for="last_name">Stock Quantity</label> <input type="number"
							placeholder="Enter Stock Quantity" name="quantity"
							class="form-control" id="quantity" onkeyup="checkQuantity(this)"
							required>
						<div>
							<p id="quantityText"></p>
						</div>
					</div>
				</div>
				<div>
					<div class="col-md-12 form-group">
						<label for="last_name">Product Image</label> <input type="file"
							placeholder="Select Image" name="image" class="form-control"
							id="image" onchange="checkImageType(this)"
							accept=".png, .jpeg, .jpg, .gif" required>

						<div>
							<p id="imageText"></p>
						</div>
					</div>
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

