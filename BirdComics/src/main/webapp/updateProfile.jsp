<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.birdcomics.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>Update Profile</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" href="css/changes.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>

    <div class="container bg-secondary" style="margin-top: 50px;">

        <div class="row">
            <div class="col-lg-8 offset-lg-2">
                <div class="card mb-4">
                    <div class="card-body">
                        <h2 class="text-center mb-4">Update Profile</h2>
                        <form action="UpdateProfileServlet" method="get">
                            <div class="form-group">
                                <label for="fullName">Full Name</label>
                                <input type="text" id="fullName" name="fullName" class="form-control" value="${param.fullName}" required>
                            </div>
                            <div class="form-group">
                                <label for="phone">Phone</label>
                                <input type="text" id="phone" name="phone" class="form-control" value="${param.phone}" required>
                            </div>
                            <div class="form-group">
                                <label for="address">Address</label>
                                <input type="text" id="address" name="address" class="form-control" value="${param.address}" required>
                            </div>
                            <div class="form-group">
                                <label for="pinCode">PinCode</label>
                                <input type="text" id="pinCode" name="pinCode" class="form-control" value="${param.pinCode}" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Save Changes</button>
                            <a href="userProfile.jsp" class="btn btn-secondary">Cancel</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>

</body>
</html>
