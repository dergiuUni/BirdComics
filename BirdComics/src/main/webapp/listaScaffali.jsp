<%@page import="java.util.*"%>
<%@page import="com.birdcomics.GestioneMagazzino.*"%>
<!DOCTYPE html>
<html>
<head>
    <title>Magazziniere</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/changes.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>

<jsp:include page="./fragments/header.jsp" />
<body>
    <div class="text-center" style="color: green; font-size: 24px; font-weight: bold; margin-top: 15px; margin-bottom: 15px;">
        Magazziniere
    </div>

    <form class="search-form" action="GestioneScaffali" method="get">
        <input type="text" name="search" placeholder="Cerca uno scaffale...">
        <input type="submit" value="Cerca">
    </form>   

    <h3 style="text-align: center;"><%= request.getAttribute("message") %></h3>

    <div class="container-fluid">
        <div class="table-responsive">
            <table class="table table-hover table-sm">
                <thead style="background-color: #4CAF50; color: white; font-size: 18px;">
                    <tr>
                        <th>IdScaffale</th>
                        <th>IdFumetto</th>
                        <th>NomeFumetto</th>
                        <th>Quantita disponibile</th>
                        <th>Quantita Massima</th>
                        <th style="text-align: center">Azioni</th>
                    </tr>
                </thead>
                <tbody style="background-color: white; font-size: 16px;">
    <% 
        ArrayList<ScaffaliBean> listaScaffali = (ArrayList<ScaffaliBean>) request.getAttribute("listaScaffali");
        if (listaScaffali != null) {
            for (ScaffaliBean scaffali : listaScaffali) { 
    %>
        <tr>
            <td><%= scaffali.getId() %></td>
            <td><%= scaffali.getFumetto().getId() %></td>
            <td><%= scaffali.getFumetto().getName() %></td>
            <td><%= scaffali.getQuantitaOccupata()%></td>
            <td><%= scaffali.getQuantitaMassima() %></td>
            <td style="text-align: center">

                <% 
			        if (scaffali.getQuantitaOccupata() == 0) {
			    %>
	                <form action="AggiungiFumetto" method="post">
	                    <input type="hidden" name="warehouseId" value="<%= scaffali.getFumetto().getId() %>">
	                    <button type="submit" class="btn btn-danger">Aggiungi</button>
	                </form>
                
                <% 
			        }else{
			    %>
			    
	 	             <form action="CancellaScaffale" method="post">
	                    <input type="hidden" name="warehouseId" value="<%= scaffali.getId() %>">
	                    <button type="submit" class="btn btn-danger">Cancella</button>
	                </form>
	                <form action="SpostaFumetto" method="post">
	                    <input type="hidden" name="warehouseId" value="<%= scaffali.getFumetto().getId() %>">
	                    <button type="submit" class="btn btn-danger">Sposta</button>
	                </form>
			    
			    <% 
			        }
			    %>
                
            </td>
        </tr>
    <% 
            }
        } else { 
    %>
        <tr>
            <td colspan="6" style="text-align: center;">Nessun scaffale disponibile.</td>
        </tr>
    <% } %>
</tbody>
            </table>
        </div>
    </div>

    <%@ include file="/fragments/footer.html" %>
</body>
</html>
