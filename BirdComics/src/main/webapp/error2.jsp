<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Errore</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            text-align: center;
            padding: 50px;
        }
        .error-container {
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 5px;
            padding: 20px;
            max-width: 600px;
            margin: 0 auto;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            color: #cc0000;
        }
        p {
            color: #666;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h2>Errore durante l'esecuzione della richiesta</h2>
        <p>Errore: <%= request.getAttribute("errorMessage") %></p>
        <p>Torna alla <a href="index.jsp">pagina principale</a></p>
    </div>
</body>
</html>
