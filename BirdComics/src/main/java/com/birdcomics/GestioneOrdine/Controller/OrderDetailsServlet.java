

package com.birdcomics.GestioneOrdine.Controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Bean.OrderBean;
import com.birdcomics.GestioneOrdine.Service.OrdineService;
import com.birdcomics.GestioneOrdine.Service.OrdineServiceImpl;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/OrderDetailsServlet")
public class OrderDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public OrdineService ordineService;  // Riferimento al servizio

    public OrderDetailsServlet(OrdineService ordineService) {
        this.ordineService = ordineService;
    }
    
    public OrderDetailsServlet() {
        super();
        this.ordineService = new OrdineServiceImpl();  // Inizializza il servizio
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = (String) request.getSession().getAttribute("email");


        if (email != null) {
            List<OrderBean> orders;
            try {
                orders = ordineService.getOrdiniPerUtente(email);  // Usa il servizio per ottenere gli ordini dell'utente

                // Passa gli ordini alla JSP
                request.setAttribute("orders", orders);
                
            } catch (SQLException e) {
                e.printStackTrace();
                // Gestire l'errore, ad esempio mostrando un messaggio appropriato
            }

            // Forward alla JSP per la visualizzazione degli ordini
            request.getRequestDispatcher("/orderDetails.jsp").forward(request, response);
        } else {
            // Se l'utente non è loggato, reindirizzalo alla pagina di login
            response.sendRedirect("login.jsp");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);  // Chiama il metodo doGet per gestire le richieste POST
    }
}
