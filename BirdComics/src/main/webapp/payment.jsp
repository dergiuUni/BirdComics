<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page
    import="com.birdcomics.*,java.util.*,javax.servlet.ServletOutputStream,java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Payments</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
    href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" href="css/changes.css">
<script
    src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
    src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<jsp:include page="/fragments/header.jsp" />
<body>
    <div class="container">
        <div class="row"
            style="margin-top: 35px; margin-left: 2px; margin-right: 2px; margin-bottom: 35px">
            <form action="./OrderServlet" method="post"
                class="col-md-6 col-md-offset-3">
                <div style="font-weight: bold;" class="text-center">
                    <div class="form-group">
                        
                        <h2 style="color: green;">Credit Card Payment</h2>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 form-group">
                        <label for="last_name">Name of Card Holder</label> <input
                            type="text" placeholder="Enter Card Holder Name"
                            name="cardholder" class="form-control" id="last_name" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 form-group">
                        <label for="last_name">Enter Credit Card Number</label> <input
                            type="number" placeholder="4242-4242-4242-4242" name="cardnumber"
                            class="form-control" id="last_name" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 form-group">
                        <label for="last_name">Expiry Month</label> <input type="number"
                            placeholder="MM" name="expmonth" class="form-control" size="2"
                            max="12" min="00" id="last_name" required>
                    </div>
                    <div class="col-md-6 form-group">
                        <label for="last_name">Expiry Year</label> <input type="number"
                            placeholder="YYYY" class="form-control" size="4" id="last_name"
                            name="expyear" required>
                    </div>
                </div>
                <div class="row text-center">
                    <div class="col-md-6 form-group">
                        <label for="last_name">Enter CVV</label> <input type="number"
                            placeholder="123" class="form-control" size="3" id="last_name"
                            name="expyear" required> <input type="hidden"
                            name="amount" value="${param.amount}">

                    </div>
                    <div class="col-md-6 form-group">
                        <label>&nbsp;</label>
                        <button type="submit" class="form-control btn btn-success">
                            Pay :Euro ${param.amount}
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
<%@ include file="fragments/footer.html"%>
</body>
</html>
